package bg.uni.sofia.fmi.mjt.dungeon.treasure;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;

public class HealthPotion implements Treasure {

    private int healingPoints;

    public HealthPotion(int healingPoints) {
        this.healingPoints = healingPoints;
    }

    public int heal() {
        return this.healingPoints;
    }

    @Override
    public String collect(Hero hero) {
        hero.takeHealing(this.healingPoints);

        return "Health potion found! " + this.healingPoints + " health points added to your hero!";
    }

}
