package com.example.model;

public class FareBid {
    private String id;
    private String rideId;
    private String passengerName;
    private String passengerCollege;
    private double passengerRating;
    private double bidAmount;
    private double askingPrice;
    private String status; // PENDING, ACCEPTED, COUNTERED, REJECTED
    private String timestamp;
    private int secondsRemaining;

    public FareBid(String id, String rideId, String passengerName, String passengerCollege, 
                   double passengerRating, double bidAmount, double askingPrice, 
                   String status, String timestamp, int secondsRemaining) {
        this.id = id;
        this.rideId = rideId;
        this.passengerName = passengerName;
        this.passengerCollege = passengerCollege;
        this.passengerRating = passengerRating;
        this.bidAmount = bidAmount;
        this.askingPrice = askingPrice;
        this.status = status;
        this.timestamp = timestamp;
        this.secondsRemaining = secondsRemaining;
    }

    public String getId() { return id; }
    public String getRideId() { return rideId; }
    public String getPassengerName() { return passengerName; }
    public String getPassengerCollege() { return passengerCollege; }
    public double getPassengerRating() { return passengerRating; }
    public double getBidAmount() { return bidAmount; }
    public double getAskingPrice() { return askingPrice; }
    public String getStatus() { return status; }
    public String getTimestamp() { return timestamp; }
    public int getSecondsRemaining() { return secondsRemaining; }

    public void setBidAmount(double amount) { this.bidAmount = amount; }
    public void setStatus(String status) { this.status = status; }
}
