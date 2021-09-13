package com.example.bookingapp.Models;

public class PaymentDetails {

    public String amount;
    public String date;
    public String seats;

    public PaymentDetails() {
    }

    public PaymentDetails( String amount, String date, String seats) {
        this.amount = amount;
        this.date = date;
        this.seats = seats;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }
}
