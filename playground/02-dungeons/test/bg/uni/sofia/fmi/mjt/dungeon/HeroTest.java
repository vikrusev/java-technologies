package bg.uni.sofia.fmi.mjt.dungeon;

import static org.junit.Assert.*;

import org.junit.Test;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Spell;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Weapon;

public class HeroTest implements TestUtils {

	@Test
	public void testHeroAttributes() {
		Hero hero = createTestHero();

		assertEquals("hero", hero.getName());
		assertEquals(100, hero.getMana());
		assertEquals(100, hero.getMana());
		assertNull(hero.getWeapon());
		assertNull(hero.getSpell());
	}

	@Test
	public void testHeroEquipWeapons() {
		Hero hero = createTestHero();

		Weapon weakWeapon = new Weapon("weak weapon", 10);
		hero.equip(weakWeapon);

		assertEquals(weakWeapon.getName(), hero.getWeapon().getName());
		assertEquals(weakWeapon.getDamage(), hero.getWeapon().getDamage());

		Weapon strongWeapon = new Weapon("strong weapon", 100);
		hero.equip(strongWeapon);

		assertEquals(strongWeapon.getName(), hero.getWeapon().getName());
		assertEquals(strongWeapon.getDamage(), hero.getWeapon().getDamage());

		hero.equip(weakWeapon);
		assertEquals(strongWeapon.getName(), hero.getWeapon().getName());
		assertEquals(strongWeapon.getDamage(), hero.getWeapon().getDamage());
	}

	@Test
	public void testHeroLearnSpells() {
		Hero hero = createTestHero();

		Spell weakSpell = new Spell("weak spell", 10, 5);
		hero.learn(weakSpell);

		assertEquals(weakSpell.getName(), hero.getSpell().getName());
		assertEquals(weakSpell.getDamage(), hero.getSpell().getDamage());
		assertEquals(weakSpell.getManaCost(), hero.getSpell().getManaCost());

		Spell strongSpell = new Spell("strong spell", 100, 20);
		hero.learn(strongSpell);

		assertEquals(strongSpell.getName(), hero.getSpell().getName());
		assertEquals(strongSpell.getDamage(), hero.getSpell().getDamage());
		assertEquals(strongSpell.getManaCost(), hero.getSpell().getManaCost());

		hero.learn(weakSpell);
		assertEquals(strongSpell.getName(), hero.getSpell().getName());
		assertEquals(strongSpell.getDamage(), hero.getSpell().getDamage());
		assertEquals(strongSpell.getManaCost(), hero.getSpell().getManaCost());
	}

	@Test
	public void testHeroHealing() {
		Hero hero = createTestHero();
		int maxHealth = hero.getHealth();

		hero.takeHealing(5);
		assertEquals(maxHealth, hero.getHealth());

		hero.takeDamage(10);
		hero.takeHealing(5);
		assertEquals(maxHealth - 5, hero.getHealth());

		hero.takeDamage(hero.getHealth());
		hero.takeHealing(5);
		assertEquals(0, hero.getHealth());
		assertFalse(hero.isAlive());
	}

	@Test
	public void testHeroManaHealing() {
		Hero hero = createTestHero();
		hero.learn(new Spell("apell", 5, 10));
		int maxMana = hero.getMana();

		hero.takeMana(5);
		assertEquals(maxMana, hero.getMana());

		hero.attack();
		hero.takeMana(5);
		assertEquals(maxMana - 5, hero.getMana());
	}

	@Test
	public void testHeroWeaponAttack() {
		Hero hero = createTestHero();
		hero.equip(new Weapon("weapon", 10));

		int attackDamage = hero.attack();

		assertEquals(10, attackDamage);
	}

	@Test
	public void testHeroSpellAttack() {
		Hero hero = createTestHero();
		hero.learn(new Spell("spell", 10, 80));

		int attackDamage = hero.attack();
		assertEquals(10, attackDamage);
		assertEquals(20, hero.getMana());

		int secondAttackDamage = hero.attack();
		assertEquals(0, secondAttackDamage);
		assertEquals(20, hero.getMana());
	}

	@Test
	public void testHeroMixedAttack() {
		Hero hero = createTestHero();
		hero.equip(new Weapon("weapon", 20));
		hero.learn(new Spell("spell", 10, 10));

		int weaponAttack = hero.attack();
		assertEquals(20, weaponAttack);
		assertEquals(100, hero.getMana());

		hero.learn(new Spell("stronger spell", 30, 80));
		int spellAttack = hero.attack();
		assertEquals(30, spellAttack);
		assertEquals(20, hero.getMana());

		int secondWeaponAttack = hero.attack();
		assertEquals(20, secondWeaponAttack);
		assertEquals(20, hero.getMana());
	}

	@Test
	public void testHeroGetDamage() {
		Hero hero = createTestHero();
		assertEquals(100, hero.getHealth());
		
		hero.takeDamage(50);
		assertEquals(50, hero.getHealth());
		assertTrue(hero.isAlive());
		
		hero.takeDamage(60);
		assertEquals(0, hero.getHealth());
		assertFalse(hero.isAlive());
	}

}
