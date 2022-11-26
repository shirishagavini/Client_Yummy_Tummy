package com.example.yummytummyclient.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yummytummyclient.MenuItemClass;
import com.example.yummytummyclient.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyMenuAdapter extends RecyclerView.Adapter<MyMenuAdapter.ViewHolder> {
    List<MenuItemClass> list;
    Context context;

    public MyMenuAdapter(List<MenuItemClass> list, Context context) {
        this.list = list;
        this.context = context;
        Log.e("manan",""+list.size());
    }

    @NonNull
    @Override
    public MyMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMenuAdapter.ViewHolder holder, int position) {
        MenuItemClass currItem  = list.get(position);
        holder.itemPricee.setText(currItem.getItemName());
        holder.itemName.setText(""+currItem.getPrice());
        Log.e("ell",currItem.getUrl());
        try{
        Picasso.get().load(currItem.getUrl()).placeholder(R.drawable.food_display).into(holder.imageView);
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemPricee,itemName;
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemPricee = itemView.findViewById(R.id.foodTextView);
            itemName  = itemView.findViewById(R.id.priceTextView);
            imageView = itemView.findViewById(R.id.foodImageView);
        }

        @Override
        public void onClick(View view) {
        }
    }
}
