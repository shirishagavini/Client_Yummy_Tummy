package com.example.yummytummyclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yummytummyclient.OrderDetails;
import com.example.yummytummyclient.R;

import java.util.List;



public class CompeletOrdersAdapter extends RecyclerView.Adapter<CompeletOrdersAdapter.ViewHolder> {

    List<OrderDetails> list;
    private Context context;


    public CompeletOrdersAdapter(List<OrderDetails> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CompeletOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incompelete_order_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetails currEle = list.get(position);
        holder.itemName.setText(currEle.getItemName());
        int total = currEle.getPrice()* currEle.getQuantity();
        holder.cost.setText(""+currEle.getPrice()+" * "+ currEle.getQuantity()+" = Rs "+total);
        holder.orderDate.setText(currEle.getTimeStamp());
        holder.paymentMode.setText(""+currEle.getQuantity());
        holder.orderId.setText(currEle.getDate());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CheckBox itemCheck;
        public TextView orderId,paymentMode,orderDate,cost,itemName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderIdNumberTextView);
            paymentMode = itemView.findViewById(R.id.modeTextView);
            orderDate = itemView.findViewById(R.id.date);
            cost = itemView.findViewById(R.id.costing);
            itemName = itemView.findViewById(R.id.itemNameValue);
            itemCheck = itemView.findViewById(R.id.checkbox);
            itemCheck.setVisibility(View.INVISIBLE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}


