package com.example.bookingapp;

public class BookingDetail {

    public String from;
    public String to;
    public String date;


    public BookingDetail(String from, String to, String date) {
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public BookingDetail(){

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
