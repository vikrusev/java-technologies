package bg.sofia.uni.fmi.mjt.carstore;

import bg.sofia.uni.fmi.mjt.carstore.car.Car;

import java.util.Comparator;

public class CustomModelComparator implements Comparator<Car> {

    @Override
    public int compare(Car car1, Car car2) {
        if (car2 != null) {
            return car1.getModel().name().compareTo(car2.getModel().name());
        }
        else return 0;
    }
}
