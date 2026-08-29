package com.example.model;

public class ChatMessage {
    private String id;
    private String rideId;
    private String senderName;
    private boolean isDriver;
    private boolean isVerified;
    private String text;
    private String timestamp;
    private boolean isLocationPin;
    private double lat;
    private double lng;

    public ChatMessage(String id, String rideId, String senderName, boolean isDriver, 
                       boolean isVerified, String text, String timestamp) {
        this.id = id;
        this.rideId = rideId;
        this.senderName = senderName;
        this.isDriver = isDriver;
        this.isVerified = isVerified;
        this.text = text;
        this.timestamp = timestamp;
        this.isLocationPin = false;
    }

    public ChatMessage(String id, String rideId, String senderName, boolean isDriver, 
                       boolean isVerified, String text, String timestamp, double lat, double lng) {
        this.id = id;
        this.rideId = rideId;
        this.senderName = senderName;
        this.isDriver = isDriver;
        this.isVerified = isVerified;
        this.text = text;
        this.timestamp = timestamp;
        this.isLocationPin = true;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() { return id; }
    public String getRideId() { return rideId; }
    public String getSenderName() { return senderName; }
    public boolean isDriver() { return isDriver; }
    public boolean isVerified() { return isVerified; }
    public String getText() { return text; }
    public String getTimestamp() { return timestamp; }
    public boolean isLocationPin() { return isLocationPin; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
}
