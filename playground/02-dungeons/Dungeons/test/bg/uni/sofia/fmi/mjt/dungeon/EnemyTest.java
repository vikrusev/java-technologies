package bg.uni.sofia.fmi.mjt.dungeon;

import static org.junit.Assert.*;

import org.junit.Test;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Enemy;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Spell;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Weapon;

public class EnemyTest implements TestUtils {

	@Test
	public void testEnemyAttributes() {
		Enemy enemy = new Enemy("enemy", 100, 100, new Weapon("weapon", 10), new Spell("spell", 20, 20));

		assertEquals("enemy", enemy.getName());
		assertEquals(100, enemy.getMana());
		assertEquals(100, enemy.getMana());

		assertEquals("weapon", enemy.getWeapon().getName());
		assertEquals(10, enemy.getWeapon().getDamage());

		assertEquals("spell", enemy.getSpell().getName());
		assertEquals(20, enemy.getSpell().getDamage());
		assertEquals(20, enemy.getSpell().getManaCost());
	}

	@Test
	public void testEnemyWeaponAttack() {
		Enemy enemy = new Enemy("enemy", 100, 100, new Weapon("weapon", 10), null);

		int attackDamage = enemy.attack();

		assertEquals(10, attackDamage);
	}

	@Test
	public void testHeroSpellAttack() {
		Enemy enemy = new Enemy("enemy", 100, 100, null, new Spell("spell", 20, 80));

		int attackDamage = enemy.attack();
		assertEquals(20, attackDamage);
		assertEquals(20, enemy.getMana());

		int secondAttackDamage = enemy.attack();
		assertEquals(0, secondAttackDamage);
		assertEquals(20, enemy.getMana());
	}

	@Test
	public void testEnemyMixedAttack() {
		Enemy enemy = new Enemy("enemy", 100, 100, new Weapon("weapon", 10), new Spell("spell", 20, 80));

		int spellAttack = enemy.attack();
		assertEquals(20, spellAttack);
		assertEquals(20, enemy.getMana());

		int secondAttack = enemy.attack();
		assertEquals(10, secondAttack);
		assertEquals(20, enemy.getMana());

		enemy = new Enemy("enemy", 100, 100, new Weapon("weapon", 30), new Spell("spell", 20, 80));
		int weaponAttack = enemy.attack();
		assertEquals(30, weaponAttack);
		assertEquals(100, enemy.getMana());
	}

	@Test
	public void testEnemyGetDamage() {
		Enemy enemy = new Enemy("enemy", 100, 0, null, null);
		assertEquals(100, enemy.getHealth());

		enemy.takeDamage(50);
		assertEquals(50, enemy.getHealth());
		assertTrue(enemy.isAlive());

		enemy.takeDamage(60);
		assertEquals(0, enemy.getHealth());
		assertFalse(enemy.isAlive());
	}

}
