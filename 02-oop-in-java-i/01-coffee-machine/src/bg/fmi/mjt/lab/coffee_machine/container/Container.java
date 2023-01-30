package bg.fmi.mjt.lab.coffee_machine.container;

import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;

public class Container {

    private double CURRENT_WATER = 0.0;
    private double CURRENT_COFFEE = 0.0;
    private double CURRENT_MILK = 0.0;
    private double CURRENT_CACAO = 0.0;

    public Container(double water, double coffee, double milk, double cacao) {
        refill(water, coffee, milk, cacao);
    }

    public void refill(double water, double coffee, double milk, double cacao) {
        this.CURRENT_WATER = water;
        this.CURRENT_COFFEE = coffee;
        this.CURRENT_MILK = milk;
        this.CURRENT_CACAO = cacao;
    }

    public void use(Beverage beverage, int quantity) {
        this.CURRENT_WATER  -= beverage.getWater()  * quantity;
        this.CURRENT_COFFEE -= beverage.getCoffee() * quantity;
        this.CURRENT_MILK   -= beverage.getMilk()   * quantity;
        this.CURRENT_CACAO  -= beverage.getCacao()  * quantity;
    }

    /**
     * Returns the current quantity (in milliliters) of the water in the container
     */
    public double getCurrentWater() {
        return CURRENT_WATER;
    }

    /**
     * Returns the current quantity (in grams) of the coffee in the container
     */
    public double getCurrentCoffee() {
        return CURRENT_COFFEE;
    }

    /**
     * Returns the current quantity (in milliliters) of the milk in the container
     */
    public double getCurrentMilk() {
        return CURRENT_MILK;
    }

    /**
     * Returns the current quantity (in grams) of the cacao in the container
     */
    public double getCurrentCacao(){
        return CURRENT_CACAO;
    }
}
