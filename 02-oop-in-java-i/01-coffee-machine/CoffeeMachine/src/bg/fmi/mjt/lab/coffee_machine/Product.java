package bg.fmi.mjt.lab.coffee_machine;

import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;

public class Product {

    private String name = null;
    private int quantity = 0;

    private int fortuneIndex;
    private static final String[] fortunes = {"If at first you don't succeed call it version 1.0.",
            "Today you will make magic happen!",
            "Have you tried turning it off and on again?",
            "Life would be much more easier if you had the source code."};

    Product(Beverage beverage, int quantity, int fortuneIndex) {
        this.name = beverage.getName();
        this.quantity = quantity;
        this.fortuneIndex = fortuneIndex;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getLuck() {
        if(fortuneIndex == -1) return null;
        else return fortunes[fortuneIndex];
    }
}
