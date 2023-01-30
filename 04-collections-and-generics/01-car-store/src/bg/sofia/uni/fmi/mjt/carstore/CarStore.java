package bg.sofia.uni.fmi.mjt.carstore;

import bg.sofia.uni.fmi.mjt.carstore.car.Car;
import bg.sofia.uni.fmi.mjt.carstore.enums.*;
import bg.sofia.uni.fmi.mjt.carstore.exception.*;

import java.util.*;

public class CarStore {

    private Set<Car> cars;

    public CarStore() {
        this.cars = new HashSet<>();
    }

    /**
     * Adds the specified car in the store.
     * @return true if the car was added successfully to the store
     */
    public boolean add(Car car) {
        if (cars == null) {
            cars = new HashSet<>();
        }
        return cars.add(car);
    }

    /**
     * Adds all of the elements of the specified collection in the store.
     * @return true if the store cars are changed after the execution (i.e. at least one new car is added to the store)
     */
    public boolean addAll(Collection<Car> cars) {
        if (this.cars == null) {
            this.cars = new HashSet<>();
        }
        return this.cars.addAll(cars);
    }

    /**
     * Removes the specified car from the store.
     * @return true if the car is successfully removed from the store
     */
    public boolean remove(Car car) {
        return cars.contains(car) && cars.remove(car);
    }

    /**
     * Returns all cars of a given model.
     * The cars need to be sorted by year of manufacture (in ascending order).
     */
    public Collection<Car> getCarsByModel(Model model) {
        List<Car> asList = new ArrayList<>();

        for (Car car : cars) {
            if (car.getModel() == model) {
                asList.add(car);
            }
        }

        Comparator<Car> comparatorYear = new CustomYearComparator();
        Collections.sort(asList, comparatorYear);

        return asList;
    }

    /**
     * Finds a car from the store by its registration number.
     * @throws CarNotFoundException if a car with this registration number is not found in the store
     **/
    public Car getCarByRegistrationNumber(String registrationNumber) {
        Car car = null;

        for (Car current : cars) {
            if (current.getRegistrationNumber().equals(registrationNumber)) {
                car = current;
                break;
            }
        }

        if (car == null) {
            throw new CarNotFoundException("The car could not be found!");
        }

        return car;
    }

    /**
     * Returns all cars sorted by their default order*
     * Default order - подредени първо по модел (по азбучен ред) и след това по година на производство (в нарастващ ред)
     **/
    public Collection<Car> getCars() {

        List<Car> asList = new ArrayList<>(cars);

        Comparator<Car> comparatorModel = new CustomModelComparator();
        Comparator<Car> comparatorYear = new CustomYearComparator();

        Collections.sort(asList, comparatorModel);
        Collections.sort(asList, comparatorYear);

        return asList;
    }

    /**
     * Returns all cars sorted according to the order induced by the specified comparator.
     */
    public Collection<Car> getCars(Comparator<Car> comparator) {
        List<Car> asList = new ArrayList<>(cars);
        Collections.sort(asList, comparator);

        return asList;
    }

    /**
     * Returns all cars sorted according to the given comparator and boolean flag for order.
     * @param isReversed if true the cars should be returned in reversed order
     */
    public Collection<Car> getCars(Comparator<Car> comparator, boolean isReversed) {
        List<Car> asList = new ArrayList<>(cars);
        Collections.sort(asList, comparator);

        if (isReversed) {
            Collections.reverse(asList);
        }

        return asList;
    }

    /**
     * Returns the total number of cars in the store.
     */
    public int getNumberOfCars() {
        return cars.size();
    }

    /**
     * Returns the total price of all cars in the store.
     */
    public int getTotalPriceForCars() {
        int price = 0;
        for (Car car : cars) {
            price += car.getPrice();
        }
        return price;
    }
}
