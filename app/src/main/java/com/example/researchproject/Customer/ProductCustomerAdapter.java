package com.example.researchproject.Customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.researchproject.Classes.Product;
import com.example.researchproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductCustomerAdapter extends RecyclerView.Adapter<ProductCustomerAdapter.MyCustomViewHolder> {
    List<Product> productList;
    private Callback listener;

    public void setListener(Callback listener)    {
        this.listener = listener;
    }

    public ProductCustomerAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_item_customer, parent, false);
        MyCustomViewHolder myCustomViewHolder = new MyCustomViewHolder(view);
        return myCustomViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCustomViewHolder holder, int position) {
        holder.tvProductName.setText(productList.get(position).getName());
        holder.tvProductPrice.setText(String.format("$%s", productList.get(position).getPrice()));
        Picasso.get().load(productList.get(position).getUrl_image()).into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyCustomViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName, tvProductPrice;
        ImageView imgProduct;
        Button btnAddToCart;

        public MyCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);

            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        //change number on cart icon and add product to cart(firebase)
                        listener.onAddClick(productList.get(getAdapterPosition()).getId());
                    }
                }
            });
        }
    }

    public interface Callback
    {
        public void onAddClick(int id);
    }
}
