package com.example.bookingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bookingapp.Models.Bus;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<com.example.bookingapp.BusAdapter.ArtistViewHolder> {

    private final Context mCtx;
    private final List<Bus> busList;
    private ItemClickListener clickListener;

    public BusAdapter(Context mCtx, List<Bus> busList) {
        this.mCtx = mCtx;
        this.busList = busList;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_buses, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.textViewBusName.setText(bus.travelsName);
        holder.textViewBusNumber.setText("Bus Number : " + bus.busNumber);
        holder.textViewDate.setText("Journey Date : " + bus.date);
        holder.textViewTime.setText("Journey Time : " + bus.time);
        holder.textViewFrom.setText("From : " + bus.from);
        holder.textViewTo.setText("To : " + bus.to);
        holder.textViewCondition.setText("Bus Condition: " + bus.busCondition);
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewBusName, textViewBusNumber, textViewDate, textViewTime, textViewFrom,textViewTo,textViewCondition;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewBusName = itemView.findViewById(R.id.text_view_busName);
            textViewBusNumber = itemView.findViewById(R.id.text_view_busNumber);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewTime = itemView.findViewById(R.id.text_view_time);
            textViewFrom = itemView.findViewById(R.id.text_view_from);
            textViewTo = itemView.findViewById(R.id.text_view_to);
            textViewCondition = itemView.findViewById(R.id.text_view_condition);

            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}

