package bg.uni.sofia.fmi.mjt.dungeon.actor;

import bg.uni.sofia.fmi.mjt.dungeon.treasure.Spell;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Weapon;

public class Enemy implements Actor {

    private String name;
    private int health;
    private int mana;
    private boolean alive = true;
    private Weapon weapon;
    private Spell spell;

    public Enemy(String name, int health, int mana, Weapon weapon, Spell spell) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        this.weapon = weapon;
        this.spell = spell;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public int getMana() {
        return this.mana;
    }

    @Override
    public boolean isAlive() {
        return this.alive;
    }

    @Override
    public Weapon getWeapon() {
        return this.weapon;
    }

    @Override
    public Spell getSpell() {
        return this.spell;
    }

    @Override
    public void takeDamage(int damagePoints) {
        this.health -= damagePoints;

        if (this.health <= 0) {
            this.health = 0;
            this.alive = false;
        }
    }

    @Override
    public int attack() {
        if (this.weapon == null && this.spell == null) {
            return 0;
        }

        if (this.spell == null) {
            return this.weapon.getDamage();
        }

        if (this.weapon == null) {
            if (this.mana >= this.spell.getManaCost()) {
                this.mana -= this.spell.getManaCost();

                return this.spell.getDamage();
            } else {
                return 0;
            }
        }

        if (this.spell.getDamage() > this.weapon.getDamage()) {
            if (this.mana >= this.spell.getManaCost()) {
                this.mana -= this.spell.getManaCost();

                return this.spell.getDamage();
            }
        }

        return this.weapon.getDamage();
    }

}
