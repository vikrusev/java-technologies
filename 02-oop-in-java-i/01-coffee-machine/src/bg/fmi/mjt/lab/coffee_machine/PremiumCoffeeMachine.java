package bg.fmi.mjt.lab.coffee_machine;

import bg.fmi.mjt.lab.coffee_machine.container.Container;
import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;

import java.util.Arrays;

public class PremiumCoffeeMachine implements CoffeeMachine {

    private int hasBrewed = 0;
    private boolean autoRefill = false;

    private final double DEFAULT_WATER = 1000.0;
    private final double DEFAULT_COFFEE = 1000.0;
    private final double DEFAULT_MILK = 1000.0;
    private final double DEFAULT_CACAO = 300.0;

    private final String[] names = {"Espresso", "Mochaccino", "Cappuccino"};

    private Container container = new Container(DEFAULT_WATER, DEFAULT_COFFEE, DEFAULT_MILK, DEFAULT_CACAO);

    private boolean canBrew(Beverage beverage, int quantity) {

        if( quantity > 0 && quantity <= 3 &&
            Arrays.asList(names).contains(beverage.getName()) &&
            beverage.getWater()  * quantity <= container.getCurrentWater() &&
            beverage.getCoffee() * quantity <= container.getCurrentCoffee() &&
            beverage.getMilk()   * quantity <= container.getCurrentMilk() &&
            beverage.getCacao()  * quantity <= container.getCurrentCacao())
        {
            return true;
        }
        else if(quantity > 0 && quantity <= 3 && autoRefill) {
            refill();
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * @param autoRefill - if true, it will automatically refill the container
     * if there are not enough ingredients to make the coffee drink
     */
    public PremiumCoffeeMachine(boolean autoRefill) {
        if(autoRefill) this.autoRefill = true;
    }

    public PremiumCoffeeMachine() {

    }

    /**
     * If quantity is <= 0 or the quantity is not supported for
     * the particular Coffee Machine the method returns null
     */
    public Product brew(Beverage beverage, int quantity){
        if(canBrew(beverage, quantity)) {
            container.use(beverage, quantity);
            return new Product(beverage, quantity, (this.hasBrewed++)%4);
        }
        else {
            return null;
        }
    }

    @Override
    public Product brew(Beverage beverage) {
        if(canBrew(beverage, 1)){
            container.use(beverage, 1);
            return new Product(beverage, 1, (this.hasBrewed++)%4);
        }
        else {
            return null;
        }
    }

    @Override
    public Container getSupplies() {
        return container;
    }

    @Override
    public void refill(){
        container.refill(DEFAULT_WATER, DEFAULT_COFFEE, DEFAULT_MILK, DEFAULT_CACAO);
    }

}
