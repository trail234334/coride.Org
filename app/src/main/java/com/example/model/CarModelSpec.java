package com.example.model;

public class CarModelSpec {
    private String brand;
    private String model;
    private double avgKmPerLiter;
    private String fuelType;

    public CarModelSpec(String brand, String model, double avgKmPerLiter, String fuelType) {
        this.brand = brand;
        this.model = model;
        this.avgKmPerLiter = avgKmPerLiter;
        this.fuelType = fuelType;
    }

    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getAvgKmPerLiter() { return avgKmPerLiter; }
    public String getFuelType() { return fuelType; }

    public String getFullName() {
        return brand + " " + model + " (" + avgKmPerLiter + " km/L " + fuelType + ")";
    }
}
