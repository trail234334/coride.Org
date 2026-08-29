package com.example.calculator;

import com.example.model.CarModelSpec;
import java.util.ArrayList;
import java.util.List;

public class CarDatabase {
    private static final List<CarModelSpec> CARS = new ArrayList<>();

    static {
        CARS.add(new CarModelSpec("Maruti Suzuki", "Swift 1.2L DualJet", 22.5, "Petrol"));
        CARS.add(new CarModelSpec("Hyundai", "i20 N Line 1.0L Turbo", 20.0, "Petrol"));
        CARS.add(new CarModelSpec("Tata", "Nexon EV Max", 55.0, "EV (km/eL)"));
        CARS.add(new CarModelSpec("Honda", "City 1.5L i-VTEC", 18.4, "Petrol"));
        CARS.add(new CarModelSpec("Maruti Suzuki", "Baleno 1.2L K12N", 22.3, "Petrol"));
        CARS.add(new CarModelSpec("Tata", "Punch 1.2L Revotron", 18.8, "Petrol"));
        CARS.add(new CarModelSpec("Hyundai", "Creta 1.5L CRDi", 21.0, "Diesel"));
        CARS.add(new CarModelSpec("Mahindra", "XUV700 AX7", 13.5, "Diesel"));
        CARS.add(new CarModelSpec("Toyota", "Innova Crysta 2.4L", 15.2, "Diesel"));
        CARS.add(new CarModelSpec("Volkswagen", "Virtus 1.0L TSI", 19.4, "Petrol"));
    }

    public static List<CarModelSpec> getAllCars() {
        return new ArrayList<>(CARS);
    }

    public static List<CarModelSpec> searchCars(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllCars();
        }
        String lower = query.toLowerCase().trim();
        List<CarModelSpec> results = new ArrayList<>();
        for (CarModelSpec car : CARS) {
            if (car.getFullName().toLowerCase().contains(lower) || 
                car.getBrand().toLowerCase().contains(lower) || 
                car.getModel().toLowerCase().contains(lower)) {
                results.add(car);
            }
        }
        return results;
    }

    public static CarModelSpec getDefaultCar() {
        return CARS.get(0); // Honda Civic
    }
}
