package com.example.bookingapp.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class SeatDetails implements Serializable {
    public String total_cost;
    public String total_seats;
    public ArrayList<Integer> bookedSeats;

    public SeatDetails(String total_cost, String total_seats,ArrayList<Integer> bookedSeats) {
        this.total_cost = total_cost;
        this.total_seats = total_seats;
        this.bookedSeats=bookedSeats;
    }

    public SeatDetails() {
    }

}
