package org.example.parking;

public class ParkingService {
    static CarParkingSystem carParkingSystem = new CarParkingSystem(3);
    public static void main(String[] args) {

        createCar(5);
    }
    public static void createCar(int count) {
        for (int i = 0; i < count; i++) {
            Car car = new Car(i + 1, carParkingSystem, 5);
            car.start();
        }
    }
}

