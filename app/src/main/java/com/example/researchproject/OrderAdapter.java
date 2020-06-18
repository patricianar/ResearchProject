package com.example.researchproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.researchproject.Classes.Order;
import com.example.researchproject.Classes.Product;

import java.text.DecimalFormat;
import java.util.List;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyCustomViewHolder> {
    private Order[] ordersArray;

    public OrderAdapter(Order[] ordersArray) {
        this.ordersArray = ordersArray;
    }

    @NonNull
    @Override
    public MyCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_item_order, parent, false);
        return new MyCustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCustomViewHolder holder, int position) {
        holder.tvOrderId.setText(String.valueOf(ordersArray[position].getId()));
        holder.tvDate.setText(ordersArray[position].getDate_created());
        holder.tvStatus.setText(ordersArray[position].getStatus());
        DecimalFormat decimalFormat = new DecimalFormat("$#.##");
        holder.tvTotal.setText(decimalFormat.format(ordersArray[position].getTotal()));
    }

    @Override
    public int getItemCount() {
        return ordersArray.length;
    }

    class MyCustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvDate, tvStatus, tvTotal;
        Button btnViewDetails;

        MyCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }

    }
}
