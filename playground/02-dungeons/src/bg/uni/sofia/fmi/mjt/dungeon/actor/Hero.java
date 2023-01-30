package bg.uni.sofia.fmi.mjt.dungeon.actor;

import bg.uni.sofia.fmi.mjt.dungeon.treasure.Spell;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Weapon;

public class Hero implements Actor {

    private String name;
    private int health;
    private int mana;
    private int maxHealth;
    private int maxMana;
    private boolean alive = true;
    private Weapon weapon = null;
    private Spell spell = null;

    private Position position;

    public Hero(String name, int health, int mana) {
        this.name = name;
        this.health = health;
        this.mana = mana;

        this.maxHealth = health;
        this.maxMana = mana;
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

    public Position getPosition() {
        return this.position;
    }

    public void createPosition(int x, int y) {
        this.position = new Position(x, y);
    }

    @Override
    public boolean isAlive() {
        return this.alive;
    }

    public void takeHealing(int healingPoints) {
        if (this.alive) {
            this.health = Math.min(this.maxHealth, this.health + healingPoints);
        }
    }

    @Override
    public void takeDamage(int damagePoints) {
        this.health -= damagePoints;

        if (this.health <= 0) {
            this.health = 0;
            this.alive = false;
        }
    }

    public void takeMana(int manaPoints) {
        if (this.alive) {
            this.mana = Math.min(this.maxMana, this.mana + manaPoints);
        }
    }

    public void equip(Weapon weapon) {
        if (weapon == null) {
            return;
        }

        if (this.weapon == null) {
            this.weapon = new Weapon(weapon.getName(), weapon.getDamage());
            return;
        }

        if (this.weapon.getDamage() < weapon.getDamage()) {
            this.weapon = weapon;
        }
    }

    @Override
    public Weapon getWeapon() {
        return this.weapon;
    }

    public void learn(Spell spell) {
        if (spell == null) {
            return;
        }

        if (this.spell == null) {
            this.spell = new Spell(spell.getName(), spell.getDamage(), spell.getManaCost());
            return;
        }

        if (this.spell.getDamage() < spell.getDamage()) {
            this.spell = spell;
        }
    }

    @Override
    public Spell getSpell() {
        return this.spell;
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
