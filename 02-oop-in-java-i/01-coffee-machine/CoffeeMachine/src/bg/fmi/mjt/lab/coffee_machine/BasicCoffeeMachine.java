package bg.fmi.mjt.lab.coffee_machine;

import bg.fmi.mjt.lab.coffee_machine.container.Container;
import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;

public class BasicCoffeeMachine implements CoffeeMachine {

    private final double DEFAULT_WATER = 600.0;
    private final double DEFAULT_COFFEE = 600.0;
    private final double DEFAULT_MILK = 0.0;
    private final double DEFAULT_CACAO = 0.0;

    private Container container = new Container(DEFAULT_WATER, DEFAULT_COFFEE, DEFAULT_MILK, DEFAULT_CACAO);

    private boolean canBrew(Beverage beverage) {

        return beverage.getName().equals("Espresso") &&
                beverage.getWater() <= container.getCurrentWater()  &&
                beverage.getCoffee()<= container.getCurrentCoffee() &&
                beverage.getMilk()  <= container.getCurrentMilk()   &&
                beverage.getCacao() <= container.getCurrentCacao();
    }

    public BasicCoffeeMachine() {

    }

    @Override
    public Product brew(Beverage beverage) {
        if(canBrew(beverage)) {
            container.use(beverage, 1);
            return new Product(beverage, 1, -1);
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
    public void refill() {
        container.refill(DEFAULT_WATER, DEFAULT_COFFEE, DEFAULT_MILK, DEFAULT_CACAO);
    }

}
