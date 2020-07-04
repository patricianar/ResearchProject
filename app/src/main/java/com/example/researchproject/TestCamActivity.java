package com.example.researchproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;


import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.researchproject.Admin.AddProductFragment;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TestCamActivity extends AppCompatActivity {
    private String TAG = "TestCamActivity";
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Executor cameraExecutor;
    ImageCapture imageCapture;
    private PreviewView previewView;
    Camera camera;
    String encodedImage;


    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_cam);



        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TestCamActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {
                   // PROFILE_PIC_COUNT = 1;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    //PROFILE_PIC_COUNT = 1;
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    //PROFILE_PIC_COUNT = 0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();

//        if (allPermissionsGranted()) {
//            startCamera(); //start camera if permission has been granted by user
//        } else {
//            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
//        }

        // Setup the listener for take photo button
        //camera_capture_button.setOnClickListener { takePhoto() }

        //outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor();
        previewView = findViewById(R.id.viewFinder);
        ImageView camCaptureBtn = findViewById(R.id.camera_capture_button);

        camCaptureBtn.setOnClickListener(v -> {
            takePhoto();
        });
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        imageCapture = new ImageCapture.Builder()
                .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                .build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.createSurfaceProvider());

        //camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview);
        camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture);
    }

    private void takePhoto() {
        //SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CANADA);
        //File file = new File(getBatchDirectoryName(), mDateFormat.format(new Date()) + ".jpg");

        //File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + System.currentTimeMillis() + ".png");
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/temp.png");
Log.e("file", file.toString());
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TestCamActivity.this, "Image Saved successfully", Toast.LENGTH_SHORT).show();

                        getSupportFragmentManager().beginTransaction().add(R.id.pictureFrame, PictureFragment.newInstance(Uri.fromFile(file).toString())).addToBackStack(null).commit();
                        try {
                            String url = "https://myprojectstore.000webhostapp.com/";
//                            String url = "http://100.25.155.48/";
                            // Setting image on image view using Bitmap
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));


                           //Converting Bitmap to string
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 10, baos);
                            byte[] imageBytes = baos.toByteArray();
                            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                            VolleyService request = new VolleyService(TestCamActivity.this);
                            request.executePostRequest(url, new VolleyService.VolleyCallback() {
                                @Override
                                public void getResponse(String response) {
                                    try {
                                        if(response.equals("true")){
                                            //Toast.makeText(getContext(), "New Product has been added!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            //Toast.makeText(getContext(), "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                                        }
                                        Log.e(TAG, response);
                                    } catch (Exception ex) {
                                        Log.e(TAG, ex.getMessage());
                                    }
                                }
                            }, "searchByImage", encodedImage);

                        } catch (IOException e) {
                            // Log the exception
                            e.printStackTrace();
                        }

finish();
                    }
                });
            }

            @Override
            public void onError(@NonNull ImageCaptureException error) {
                error.printStackTrace();
                Toast.makeText(TestCamActivity.this, "error " + error, Toast.LENGTH_SHORT).show();
                Log.e("camera", error + "");
            }
        });
    }

    public String getBatchDirectoryName() {
        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {
        }
        return app_folder_path;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri filePath = null;
            if (data != null) {
                if (requestCode == REQUEST_CAMERA) {
                    filePath = data.getData();
                    Log.e("path", data.getExtras().get("data") + "   hi");

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    //ImageView.setImageBitmap(photo);

                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getApplicationContext(), photo);


                    getSupportFragmentManager().beginTransaction().add(R.id.pictureFrame, PictureFragment.newInstance(tempUri.toString())).addToBackStack(null).commit();
                    try {
//                        String url = "https://myprojectstore.000webhostapp.com/";
                         String url = "http://100.25.155.48/";
                        // Setting image on image view using Bitmap
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), tempUri);


                        //Converting Bitmap to string
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 10, baos);
                        byte[] imageBytes = baos.toByteArray();
                        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        VolleyService request = new VolleyService(TestCamActivity.this);
                        request.executePostRequest(url, new VolleyService.VolleyCallback() {
                            @Override
                            public void getResponse(String response) {
                                try {
                                    if(response.equals("true")){
                                        //Toast.makeText(getContext(), "New Product has been added!", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        //Toast.makeText(getContext(), "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.e(TAG, response);
                                } catch (Exception ex) {
                                    Log.e(TAG, ex.getMessage());
                                }
                            }
                        }, "searchByImage", encodedImage);

                    } catch (IOException e) {
                        // Log the exception
                        e.printStackTrace();
                    }

//                    try {
//                        // Setting image on image view using Bitmap
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
////                        imgProd.setImageBitmap(bitmap);
//
//                        getSupportFragmentManager().beginTransaction().add(R.id.pictureFrame, PictureFragment.newInstance(filePath.toString())).addToBackStack(null).commit();
//
//
//                        //Converting Bitmap to string
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                        byte[] imageBytes = baos.toByteArray();
//                        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//                    } catch (IOException e) {
//                        // Log the exception
//                        e.printStackTrace();
//                    }
                }
            }
        }
    }

    private Uri getImageUri(Context applicationContext, Bitmap photo)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(TestCamActivity.this.getContentResolver(), photo, "Title", null);
        return Uri.parse(path);
    }
}