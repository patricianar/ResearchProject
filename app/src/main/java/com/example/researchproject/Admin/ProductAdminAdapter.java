package com.example.researchproject.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.researchproject.Classes.Product;
import com.example.researchproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.MyCustomViewHolder> {
    List<Product> productList;

    public ProductAdminAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_item_admin, parent, false);
        MyCustomViewHolder myCustomViewHolder = new MyCustomViewHolder(view);
        return myCustomViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCustomViewHolder holder, int position) {
        holder.tvProductName.setText(productList.get(position).getName());
        holder.tvProductQty.setText(String.valueOf(productList.get(position).getInventory_level()));
        holder.tvProductPrice.setText(String.valueOf(productList.get(position).getPrice()));
        Picasso.get().load(productList.get(position).getUrl_image()).into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public List<Product> getData() {
        return productList;
    }

    public void removeItem(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Product product, int position) {
        productList.add(position, product);
        notifyItemInserted(position);
    }

    public class MyCustomViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName, tvProductQty, tvProductPrice;
        ImageView imgProduct;
        CardView cardView;
        RatingBar ratingBar;

        public MyCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductQty = itemView.findViewById(R.id.tvProductQty);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            cardView = itemView.findViewById(R.id.cardViewProdAdmin);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingBar.setRating(5);

            final FragmentManager manager =  ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.prodDetailFrag, ProductDetailFragment.newInstance(productList.get(getAdapterPosition())));
                    transaction.commit();
                }
            });
        }
    }
}
