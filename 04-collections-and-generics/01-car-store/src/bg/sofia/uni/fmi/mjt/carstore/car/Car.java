package bg.sofia.uni.fmi.mjt.carstore.car;

import bg.sofia.uni.fmi.mjt.carstore.enums.*;

import java.util.Random;

public abstract class Car {

    private Model model;
    private int year;
    private int price;
    private EngineType engineType;
    private Region region;

    private String plateNumber;

    Car(Model model, int year, int price, EngineType engineType, Region region) {
        this.model = model;
        this.year = year;
        this.price = price;
        this.engineType = engineType;
        this.region = region;

        this.plateNumber = region.getPrefix() + region.getCurrentRegionNumber() + randomChar() + randomChar();
    }

    private char randomChar() {
        Random r = new Random();
        return (char)(r.nextInt(26) + 'A');
    }

    /**
     * Returns the model of the car.
     */
    public Model getModel() {
        return this.model;
    }

    /**
     * Returns the year of manufacture of the car.
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Returns the price of the car.
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Returns the engine type of the car.
     */
    public EngineType getEngineType() {
        return this.engineType;
    }

    /**
     * Returns the region of the car.
     */
    public Region getRegion() {
        return this.region;
    }

    /**
     * Returns the unique registration number of the car.
     */
    public String getRegistrationNumber() {
        return plateNumber;
    }

}