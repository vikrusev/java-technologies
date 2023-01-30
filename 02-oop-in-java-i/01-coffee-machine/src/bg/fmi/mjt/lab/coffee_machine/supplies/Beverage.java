package bg.fmi.mjt.lab.coffee_machine.supplies;

public interface Beverage {
    /**
     * Returns the name of the beverage
     * Espresso,Cappuccino or Mochaccino
     */
    String getName();

    /**
     * Returns the quantity of milk (in milliliters) that the beverage
     * requires in order to be made
     */
    double getMilk();

    /**
     * Returns the quantity of coffee (in grams) that the beverage
     * requires in order to be made
     */
    double getCoffee();

    /**
     * Returns the quantity of water (in milliliters) that the beverage
     * requires in order to be made
     */
    double getWater();

    /**
     * Returns the quantity of cacao (in grams) that the beverage
     * requires in order to be made
     */
    double getCacao();
}