package bg.uni.sofia.fmi.mjt.dungeon.treasure;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;

public class Spell implements Treasure {

    private String name;
    private int damage;
    private int manaCost;

    public Spell(String name, int damage, int manaCost) {
        this.name = name;
        this.damage = damage;
        this.manaCost = manaCost;
    }

    public String getName() {
        return this.name;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getManaCost() {
        return this.manaCost;
    }

    @Override
    public String collect(Hero hero) {
        hero.learn(this);

        return "Spell found! Damage points: " + this.damage + ", Mana cost: " + this.manaCost;
    }
}
