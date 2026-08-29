package com.example.service;

import com.example.model.RideOffer;
import java.util.ArrayList;
import java.util.List;

public class RouteMatchingEngine {

    public static class MapPoint {
        public double lat;
        public double lng;
        public String label;

        public MapPoint(double lat, double lng, String label) {
            this.lat = lat;
            this.lng = lng;
            this.label = label;
        }
    }

    public static class RouteMatchResult {
        public RideOffer rideOffer;
        public double deviationPercent;
        public String suggestedPickupPoint;
        public int matchScorePercent; // 0 - 100%
        public List<MapPoint> polylinePoints;

        public RouteMatchResult(RideOffer rideOffer, double deviationPercent, String suggestedPickupPoint, 
                                int matchScorePercent, List<MapPoint> polylinePoints) {
            this.rideOffer = rideOffer;
            this.deviationPercent = deviationPercent;
            this.suggestedPickupPoint = suggestedPickupPoint;
            this.matchScorePercent = matchScorePercent;
            this.polylinePoints = polylinePoints;
        }
    }

    public static List<MapPoint> generatePolyline(double startLat, double startLng, double endLat, double endLng) {
        List<MapPoint> points = new ArrayList<>();
        int steps = 12;
        for (int i = 0; i <= steps; i++) {
            double fraction = (double) i / steps;
            double lat = startLat + (endLat - startLat) * fraction;
            double lng = startLng + (endLng - startLng) * fraction;
            // Add slight curve for realistic Google Map route polyline
            double bend = Math.sin(fraction * Math.PI) * 0.004;
            points.add(new MapPoint(lat + bend, lng - bend, i == 0 ? "Origin" : (i == steps ? "Destination" : "Way " + i)));
        }
        return points;
    }

    public static RouteMatchResult matchRoute(RideOffer offer, String userPickupLocation) {
        double deviation = 1.8 + (Math.abs(offer.getId().hashCode()) % 20) / 10.0;
        int matchScore = Math.max(78, 100 - (int)(deviation * 6));
        String suggestedPickup = "Gate B - Main Oval Campus Circle (" + String.format("%.1f", deviation) + "% off route)";

        List<MapPoint> polyline = generatePolyline(offer.getOriginLat(), offer.getOriginLng(), offer.getDestLat(), offer.getDestLng());
        return new RouteMatchResult(offer, deviation, suggestedPickup, matchScore, polyline);
    }
}
