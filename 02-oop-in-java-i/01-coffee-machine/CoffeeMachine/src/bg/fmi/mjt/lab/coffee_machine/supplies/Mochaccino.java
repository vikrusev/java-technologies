package bg.fmi.mjt.lab.coffee_machine.supplies;

public class Mochaccino implements Beverage {

    public String getName() {
        return "Mochaccino";
    }

    public double getMilk() {
        return 150.0;
    }

    public double getCoffee() {
        return 18.0;
    }

    public double getWater() {
        return 0.0;
    }

    public double getCacao() {
        return 10.0;
    }

}
