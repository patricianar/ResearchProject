package com.example.researchproject.Customer;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.researchproject.Classes.Product;
import com.example.researchproject.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.MyCustomViewHolder> {
    private List<Product> productList;
    private CallbackCart listener;
    private SharedPreferences sharedPrefCart, sharedPrefUser;

    public void setListener(CallbackCart listener) {
        this.listener = listener;
    }

    public ProductCartAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_item_cart, parent, false);
        sharedPrefCart = parent.getContext().getSharedPreferences("Cart", MODE_PRIVATE);
        sharedPrefUser = parent.getContext().getSharedPreferences("User", MODE_PRIVATE);
        MyCustomViewHolder myCustomViewHolder = new MyCustomViewHolder(view);
        return myCustomViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCustomViewHolder holder, int position) {
        holder.tvProductName.setText(productList.get(position).getName());
        holder.tvProductPrice.setText(String.format("$%s", productList.get(position).getPrice()));
        Picasso.get().load(productList.get(position).getUrl_image()).into(holder.imgProduct);
        int qty = sharedPrefCart.getInt(String.valueOf(productList.get(position).getId()), 0);
        holder.tvProductQty.setText(String.valueOf(qty));
        double total = qty * productList.get(position).getPrice();
        holder.tvTotalPrice.setText(String.format("$%.2f", total));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void removeItem(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyCustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvProductQty, tvTotalPrice;
        ImageView imgProduct;
        Button btnMinus, btnPlus;
        ConstraintLayout constrainLayout;
        int qty;
        double total;

        public MyCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQty = itemView.findViewById(R.id.tvQty);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            constrainLayout = itemView.findViewById(R.id.constrainLayout);

            final SharedPreferences.Editor editorCart = sharedPrefCart.edit();
            final SharedPreferences.Editor editorUser = sharedPrefUser.edit();

            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int totalItems = sharedPrefUser.getInt("Items", 0);
                    qty = Integer.parseInt(tvProductQty.getText().toString());
                    qty--;
                    if (qty > 0) {
                        tvProductQty.setText(String.valueOf(qty));
                        total = qty * productList.get(getAdapterPosition()).getPrice();
                        tvTotalPrice.setText(String.format("$%.2f", total));
                        editorCart.putInt(String.valueOf(productList.get(getAdapterPosition()).getId()), qty);
                        editorCart.apply();
                        editorUser.putInt("Items", totalItems - 1);
                        editorUser.apply();
                        Log.e("user", totalItems + "");

                        //update grand total
                        if (listener != null) {
                            listener.onRemoveClick(productList.get(getAdapterPosition()).getPrice());
                        }
                    } else {
                        //ask to remove product
                        Snackbar snackbar = Snackbar.make(constrainLayout, "Are you sure you want to delete the item from your cart?", Snackbar.LENGTH_LONG)
                                .setAction("YES", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //delete from shared preferences
                                        editorCart.remove(String.valueOf(productList.get(getAdapterPosition()).getId()));
                                        editorCart.apply();
                                        editorUser.putInt("Items", totalItems - 1);
                                        editorUser.apply();
                                        Log.e("user", totalItems + "");
                                        //update grand total
                                        if (listener != null) {
                                            listener.onRemoveClick(productList.get(getAdapterPosition()).getPrice());
                                        }
                                        removeItem(getAdapterPosition());
                                    }
                                });
                        snackbar.show();
                    }
                }
            });

            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int totalItems = sharedPrefUser.getInt("Items", 0);
                    qty = Integer.parseInt(tvProductQty.getText().toString());
                    qty++;
                    tvProductQty.setText(String.valueOf(qty));
                    total = qty * productList.get(getAdapterPosition()).getPrice();
                    tvTotalPrice.setText(String.format("$%.2f", total));
                    editorCart.putInt(String.valueOf(productList.get(getAdapterPosition()).getId()), qty);
                    editorCart.apply();
                    editorUser.putInt("Items", totalItems + 1);
                    editorUser.apply();
                    //update grand total
                    if (listener != null) {
                        listener.onAddClick(productList.get(getAdapterPosition()).getPrice());
                    }
                }
            });

        }

    }

    public interface CallbackCart {
        void onAddClick(double total);

        void onRemoveClick(double total);
    }
}
