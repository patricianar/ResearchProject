package com.example.researchproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.researchproject.Classes.ProductOrdered;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyCustomViewHolder> {
    private List<ProductOrdered>  productsList;

    public OrderDetailAdapter(List<ProductOrdered> productsList) {
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public MyCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_item_shipped, parent, false);
        return new MyCustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCustomViewHolder holder, int position) {
        holder.tvProductId.setText(String.valueOf(productsList.get(position).getId()));
        holder.tvProductName.setText(productsList.get(position).getName());
        holder.tvQty.setText(String.valueOf(productsList.get(position).getQuantity()));
        DecimalFormat decimalFormat = new DecimalFormat("$#.##");
        holder.tvTotal.setText(decimalFormat.format(productsList.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    class MyCustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductId, tvProductName, tvQty, tvTotal;

        MyCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductId = itemView.findViewById(R.id.tvProductId);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }
}
