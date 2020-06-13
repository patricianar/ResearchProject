package com.example.researchproject.Customer;

import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.MyCustomViewHolder> {
    List<Product> productList;
    private Callback listener;
    SharedPreferences sharedPref;

    public void setListener(Callback listener) {
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
        sharedPref = parent.getContext().getSharedPreferences("Cart", MODE_PRIVATE);
        MyCustomViewHolder myCustomViewHolder = new MyCustomViewHolder(view);
        return myCustomViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCustomViewHolder holder, int position) {
        holder.tvProductName.setText(productList.get(position).getName());
        holder.tvProductPrice.setText(String.format("$%s", productList.get(position).getPrice()));
        Picasso.get().load(productList.get(position).getUrl_image()).into(holder.imgProduct);
        int qty = sharedPref.getInt(String.valueOf(productList.get(position).getId()), 0);
        holder.tvProductQty.setText(String.valueOf(qty));
        double total = qty * productList.get(position).getPrice();
        holder.tvTotalPrice.setText(String.format("$%.2f", total));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyCustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvProductQty, tvTotalPrice;
        ImageView imgProduct;
        Button btnMinus, btnPlus;
        int qty;
        double total;

        public MyCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQty = itemView.findViewById(R.id.tvProductQty);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);

            final SharedPreferences.Editor editor = sharedPref.edit();

            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    qty = Integer.parseInt(tvProductQty.getText().toString());
                    qty--;
                    if(qty > 0){
                        tvProductQty.setText(String.valueOf(qty));
                        total = qty * productList.get(getAdapterPosition()).getPrice();
                        tvTotalPrice.setText(String.format("$%.2f", total));
                        editor.putInt(String.valueOf(productList.get(getAdapterPosition()).getId()), qty);
                        editor.apply();
                    }
                    else{
                        //ask to remove product
                    }
                }
            });

            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    qty = Integer.parseInt(tvProductQty.getText().toString());
                    qty++;
                    tvProductQty.setText(String.valueOf(qty));
                    total = qty * productList.get(getAdapterPosition()).getPrice();
                    tvTotalPrice.setText(String.format("$%.2f", total));
                    editor.putInt(String.valueOf(productList.get(getAdapterPosition()).getId()), qty);
                    editor.apply();
                }
            });
        }
    }

    public interface Callback {
        public void onAddClick(int id);
    }
}
