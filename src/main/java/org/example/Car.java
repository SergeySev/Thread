package org.example.parking;

public class Car extends Thread{
    int carId;
    CarParkingSystem carParkingSystem;
    int count;

    public Car(int carId, CarParkingSystem carParkingSystem, int count) {
        this.carId = carId;
        this.carParkingSystem = carParkingSystem;
        this.count = count;
    }

    @Override
    public void run() {
        while (count > 0) {
            while (!carParkingSystem.enterParking(carId));
            try {
                carParkingSystem.printParking();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            carParkingSystem.exitParking(carId);
            carParkingSystem.printParking();
            count--;
        }
    }

}
