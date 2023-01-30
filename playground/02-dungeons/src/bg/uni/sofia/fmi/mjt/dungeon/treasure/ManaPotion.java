package bg.uni.sofia.fmi.mjt.dungeon.treasure;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;

public class ManaPotion implements Treasure {

    private int manaPoints;

    public ManaPotion(int manaPoints) {
        this.manaPoints = manaPoints;
    }

    public int heal() {
        return this.manaPoints;
    }

    @Override
    public String collect(Hero hero) {
        hero.takeMana(this.manaPoints);

        return "Mana potion found! " + this.manaPoints + " mana points added to your hero!";
    }

}
