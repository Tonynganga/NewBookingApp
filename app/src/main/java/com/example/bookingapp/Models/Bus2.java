package com.example.bookingapp.Models;

public class Bus2 {
        String busId;
        String travelsName;
        String busNumber;
        String date;
        String from;
        String to;
        String busCondition;

        public Bus2() {
        }

        public Bus2(String busId,String travelsName, String busNumber, String date, String from, String to, String busCondition) {
            this.busId = busId;
            this.travelsName = travelsName;
            this.busNumber = busNumber;
            this.date = date;
            this.from = from;
            this.to = to;
            this.busCondition = busCondition;
        }

        public String getTravelsName() {
            return travelsName;
        }

        public void setTravelsName(String travelsName) {
            this.travelsName = travelsName;
        }

        public String getBusNumber() {
            return busNumber;
        }

        public void setBusNumber(String busNumber) {
            this.busNumber = busNumber;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
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

        public String getBusCondition() {
            return busCondition;
        }

        public void setBusCondition(String busCondition) {
            this.busCondition = busCondition;
        }

        public String getBusId() {
            return busId;
        }

        public void setBusId(String busId) {
            this.busId = busId;
        }
    }


