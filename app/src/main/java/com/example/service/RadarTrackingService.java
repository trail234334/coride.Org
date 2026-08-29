package com.example.service;

public class RadarTrackingService {

    public static class RadarPositionState {
        public double distanceMeters;
        public float bearingDegrees;
        public String statusText;
        public boolean isCloseProximity; // < 25m
        public String estimatedWalkTime;

        public RadarPositionState(double distanceMeters, float bearingDegrees, String statusText, boolean isCloseProximity, String estimatedWalkTime) {
            this.distanceMeters = distanceMeters;
            this.bearingDegrees = bearingDegrees;
            this.statusText = statusText;
            this.isCloseProximity = isCloseProximity;
            this.estimatedWalkTime = estimatedWalkTime;
        }
    }

    public static RadarPositionState calculateRadarState(double currentLat, double currentLng, double targetLat, double targetLng, float tickSeconds) {
        // Compute distance using Haversine formula
        double earthRadius = 6371000.0; // meters
        double dLat = Math.toRadians(targetLat - currentLat);
        double dLng = Math.toRadians(targetLng - currentLng);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(currentLat)) * Math.cos(Math.toRadians(targetLat)) *
                   Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        // Compute bearing angle
        double y = Math.sin(dLng) * Math.cos(Math.toRadians(targetLat));
        double x = Math.cos(Math.toRadians(currentLat)) * Math.sin(Math.toRadians(targetLat)) -
                   Math.sin(Math.toRadians(currentLat)) * Math.cos(Math.toRadians(targetLat)) * Math.cos(dLng);
        float bearing = (float) ((Math.toDegrees(Math.atan2(y, x)) + 360) % 360);

        boolean close = distance < 25.0;
        String status;
        if (close) {
            status = "Arriving! In pickup zone (" + Math.round(distance) + "m)";
        } else if (distance < 100) {
            status = "Walking towards you (" + Math.round(distance) + "m away)";
        } else {
            status = "Approaching pickup spot (" + Math.round(distance) + "m away)";
        }

        int walkSecs = (int) (distance / 1.4); // 1.4 m/s walking speed
        String walkTime = walkSecs < 60 ? walkSecs + " sec walk" : Math.round(walkSecs / 60.0) + " min walk";

        return new RadarPositionState(distance, bearing, status, close, walkTime);
    }
}
