package com.example.market_price;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GoAdapter extends RecyclerView.Adapter<GoAdapter.GoiViewHolder> {

    LayoutInflater layoutInflater;
    List<GoiDetail> goiDetails;


    public GoAdapter(Context context, List<GoiDetail> details) {


        this.layoutInflater = LayoutInflater.from(context);
        this.goiDetails = details;
    }

    @NonNull
    @Override
    public GoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goitem, parent, false);
        return new GoiViewHolder(view);
    }


    @SuppressLint("setTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GoiViewHolder holder, int position) {

        holder.itemName.setText(goiDetails.get(position).getGroceryName());
        holder.itemPlace.setText(goiDetails.get(position).getGroceryPlace());
        holder.itemPrice.setText("₹" + goiDetails.get(position).getGroceryPrice());


        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis((goiDetails.get(position).getGroceryTime()));
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
        holder.itemDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return goiDetails.size();
    }


    public static class GoiViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPlace, itemPrice, itemDate;

        public GoiViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.ItemNameView);
            itemPlace = itemView.findViewById(R.id.ItemPlace);
            itemPrice = itemView.findViewById(R.id.ItemPrice);
            itemDate = itemView.findViewById(R.id.ItemDate);
        }
    }
}
