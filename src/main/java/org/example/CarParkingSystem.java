package org.example.parking;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class CarParkingSystem {
    private AtomicIntegerArray parking;

    public CarParkingSystem(int capacity) {
        this.parking = new AtomicIntegerArray(capacity);
    }

    public boolean enterParking(int carId) {
        for (int i = 0; i < parking.length(); i++) {
            if (parking.compareAndSet(i, 0, carId)) {
                System.out.println("Car " + carId + " has parked in the place with number " + (i + 1));
                return true;
            }
        }
        return false;
    }

    public void exitParking(int carId) {
        for (int i = 0; i < parking.length(); i++) {
            if (parking.compareAndSet(i, carId, 0)) {
                System.out.println("Car " + carId + " has EXIT from parking from the place with number " + (i + 1));
                return;
            }
        }
    }
    public void printParking() {
        System.out.println(parking);
    }
}
