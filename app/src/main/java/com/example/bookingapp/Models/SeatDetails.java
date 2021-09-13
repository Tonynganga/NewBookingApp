package com.example.bookingapp.Models;

public class SeatDetails {
    public String total_cost;
    public String total_seats;

    public SeatDetails(String total_cost, String total_seats) {
        this.total_cost = total_cost;
        this.total_seats = total_seats;
    }

    public SeatDetails() {
    }
}
