package com.example.model;

public class FuelCalculation {
    private double tripDistanceKm;
    private double kmPerLiter;
    private double fuelPricePerLiter;
    private int passengersCount;
    private double fuelRequiredLiters;
    private double totalFuelCost;
    private double costPerPassenger;
    private float gaugeNeedleAngle; // 0 to 180 degrees
    private double co2EmissionsSavedKg;

    public FuelCalculation(double tripDistanceKm, double kmPerLiter, double fuelPricePerLiter, int passengersCount) {
        this.tripDistanceKm = tripDistanceKm;
        this.kmPerLiter = kmPerLiter > 0 ? kmPerLiter : 15.0;
        this.fuelPricePerLiter = fuelPricePerLiter;
        this.passengersCount = passengersCount > 0 ? passengersCount : 1;

        this.fuelRequiredLiters = tripDistanceKm / this.kmPerLiter;
        this.totalFuelCost = this.fuelRequiredLiters * fuelPricePerLiter;
        this.costPerPassenger = totalFuelCost / (this.passengersCount + 1); // Driver + Passengers

        // Calculate gauge needle angle (0 deg = super eco, 180 deg = high consumption)
        // Normal range 5 km/L (180deg) to 30 km/L (0deg)
        double normalized = 1.0 - Math.min(1.0, Math.max(0.0, (this.kmPerLiter - 5.0) / 25.0));
        this.gaugeNeedleAngle = (float) (normalized * 180.0);

        // CO2 saved vs driving solo
        this.co2EmissionsSavedKg = (tripDistanceKm * 0.12) * (this.passengersCount);
    }

    public double getTripDistanceKm() { return tripDistanceKm; }
    public double getKmPerLiter() { return kmPerLiter; }
    public double getFuelPricePerLiter() { return fuelPricePerLiter; }
    public int getPassengersCount() { return passengersCount; }
    public double getFuelRequiredLiters() { return fuelRequiredLiters; }
    public double getTotalFuelCost() { return totalFuelCost; }
    public double getCostPerPassenger() { return costPerPassenger; }
    public float getGaugeNeedleAngle() { return gaugeNeedleAngle; }
    public double getCo2EmissionsSavedKg() { return co2EmissionsSavedKg; }
}
