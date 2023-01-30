package bg.uni.sofia.fmi.mjt.dungeon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.HealthPotion;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.ManaPotion;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Spell;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Weapon;

public class TreasureTest implements TestUtils {

	@Test
	public void testEquipWeapon() {
		Hero hero = createTestHero();

		Weapon weapon = new Weapon("weapon1", 10);
		String message = weapon.collect(hero);

		assertEquals("Weapon found! Damage points: 10", message);
		assertEquals(weapon.getName(), hero.getWeapon().getName());
		assertEquals(weapon.getDamage(), hero.getWeapon().getDamage());
	}

	@Test
	public void testLearnSpell() {
		Hero hero = createTestHero();

		Spell spell = new Spell("spell1", 10, 10);
		String message = spell.collect(hero);

		assertEquals("Spell found! Damage points: 10, Mana cost: 10", message);
		assertEquals(spell.getName(), hero.getSpell().getName());
		assertEquals(spell.getDamage(), hero.getSpell().getDamage());
		assertEquals(spell.getManaCost(), hero.getSpell().getManaCost());
	}

	@Test
	public void testPotions() {
		Hero hero = createTestHero();

		hero.learn(new Spell("spell1", 10, hero.getMana()));
		hero.attack();
		int mana = hero.getMana();

		ManaPotion manaPotion = new ManaPotion(5);
		String message = manaPotion.collect(hero);

		assertEquals("Mana potion found! 5 mana points added to your hero!", message);
		assertEquals(mana + manaPotion.heal(), hero.getMana());

		hero.takeDamage(10);
		int health = hero.getHealth();

		HealthPotion healthPotion = new HealthPotion(5);
		String messageHealthPotion = healthPotion.collect(hero);

		assertEquals("Health potion found! 5 health points added to your hero!", messageHealthPotion);
		assertEquals(health + healthPotion.heal(), hero.getHealth());
	}

	@Test
	public void testPotionsAtMaxPoints() {
		Hero hero = createTestHero();
		int startHealth = hero.getHealth();
		int startMana = hero.getMana();

		HealthPotion healthPotion = new HealthPotion(50);
		healthPotion.collect(hero);
		assertEquals(startHealth, hero.getHealth());

		ManaPotion manaPotion = new ManaPotion(50);
		manaPotion.collect(hero);

		assertEquals(startMana, hero.getMana());
	}

	@Test
	public void testPotionsAtZeroPoints() {
		Hero hero = createTestHero();

		hero.learn(new Spell("spell1", 10, hero.getMana()));
		hero.attack();
		assertEquals(0, hero.getMana());

		ManaPotion manaPotion = new ManaPotion(10);
		manaPotion.collect(hero);
		assertEquals(manaPotion.heal(), hero.getMana());

		hero.takeDamage(hero.getHealth());
		assertEquals(0, hero.getHealth());
		assertFalse(hero.isAlive());

		HealthPotion healthPotion = new HealthPotion(50);
		healthPotion.collect(hero);

		assertEquals(0, hero.getHealth());
		assertFalse(hero.isAlive());
	}

}
