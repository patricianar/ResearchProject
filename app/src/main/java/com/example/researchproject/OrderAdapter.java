package com.example.researchproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.researchproject.Classes.Order;
import java.text.DecimalFormat;

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
        holder.tvOrderId.setText("Order Id: " + ordersArray[position].getId());
        holder.tvDate.setText("Order Date: " + ordersArray[position].getDate_created());
        holder.tvStatus.setText("Status: " + ordersArray[position].getStatus());
        DecimalFormat decimalFormat = new DecimalFormat("$#.##");
        holder.tvTotal.setText("Total: " + decimalFormat.format(ordersArray[position].getTotal()));
    }

    @Override
    public int getItemCount() {
        return ordersArray.length;
    }

    class MyCustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvDate, tvStatus, tvTotal;
        ConstraintLayout orderLayout;

        MyCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            orderLayout = itemView.findViewById(R.id.orderLayout);

            final FragmentManager manager =  ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();
            orderLayout.setOnClickListener(v -> {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.frameOrder, OrderDetailFragment.newInstance(ordersArray[getAdapterPosition()]));
                transaction.addToBackStack(null);
                transaction.commit();
            });
        }
    }
}
