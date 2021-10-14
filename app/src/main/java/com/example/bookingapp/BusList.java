package com.example.bookingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapp.Models.Bus;
import com.example.bookingapp.Models.Bus2;

import java.util.List;


public class BusList extends ArrayAdapter<Bus2> {

    private final Activity context;
    private final List<Bus2> busList;

    public BusList(Activity context, List<Bus2> busList) {
        super(context, R.layout.list_bus, busList);
        this.context = context;
        this.busList = busList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_bus, null, true);


        TextView textViewTravelsName = listViewItem.findViewById(R.id.text_view_busName);
        TextView textViewBusNumber = listViewItem.findViewById(R.id.text_view_busNumber);
        TextView textViewDate = listViewItem.findViewById(R.id.text_view_date);
        TextView textViewFrom = listViewItem.findViewById(R.id.text_view_from);
        TextView textViewTo = listViewItem.findViewById(R.id.text_view_to);
        TextView textViewCondition = listViewItem.findViewById(R.id.text_view_condition);


        Bus2 bus = busList.get(position);

        textViewTravelsName.setText(bus.getTravelsName());
        textViewBusNumber.setText("Bus Number       : "+bus.getBusNumber());
        textViewDate.setText("Journey Time      : "+bus.getDate());
        textViewFrom.setText("Bus From            : "+bus.getFrom());
        textViewTo.setText("Bus To                : "+bus.getTo());
        textViewCondition.setText("Bus Condition    : "+bus.getBusCondition());

        return listViewItem;
    }
}
