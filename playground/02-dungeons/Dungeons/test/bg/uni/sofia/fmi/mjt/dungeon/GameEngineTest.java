package bg.uni.sofia.fmi.mjt.dungeon;

import static org.junit.Assert.assertEquals;

import bg.uni.sofia.fmi.mjt.dungeon.Direction;
import bg.uni.sofia.fmi.mjt.dungeon.GameEngine;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.*;
import org.junit.Before;
import org.junit.Test;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Enemy;
import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;

public class GameEngineTest {

    private Hero hero;
    private char[][] map;
    private Enemy[] enemies;
    private Treasure[] treasures;
    private GameEngine gameEngine;

    @Before
    public void setup() {
        hero = new Hero("hero", 100, 100);
        map = new char[][]{"###".toCharArray(),
                "TS.".toCharArray(),
                "#EG".toCharArray()};
        enemies = new Enemy[]{new Enemy("enemy", 100, 0, new Weapon("enemy weapon", 30), null)};
        treasures = new Treasure[]{new Weapon("strong weapon", 50)};
        gameEngine = new GameEngine(map, hero, enemies, treasures);
    }

    @Test
    public void testMoveToEmptyBlock() {
        String moveMessage = gameEngine.makeMove(Direction.RIGHT);

        assertEquals("You moved successfully to the next position.", moveMessage);
        assertEquals('.', gameEngine.getMap()[1][1]);
        assertEquals('H', gameEngine.getMap()[1][2]);
    }

    @Test
    public void testMoveToObstacle() {
        gameEngine.makeMove(Direction.RIGHT);
        String moveMessage = gameEngine.makeMove(Direction.UP);

        assertEquals("Wrong move. There is an obstacle and you cannot bypass it.", moveMessage);
        assertEquals('H', gameEngine.getMap()[1][2]);
    }

    @Test
    public void testMoveToTreasure() {
        String moveMessage = gameEngine.makeMove(Direction.LEFT);

        assertEquals("Weapon found! Damage points: 50", moveMessage);
        assertEquals('.', gameEngine.getMap()[1][1]);
        assertEquals('H', gameEngine.getMap()[1][0]);

        assertEquals("strong weapon", gameEngine.getHero().getWeapon().getName());
        assertEquals(50, gameEngine.getHero().getWeapon().getDamage());
    }

    @Test
    public void testHeroSpellAttack() {
        hero.learn(new Spell("spell", 10, 80));

        int attackDamage = hero.attack();
        assertEquals(10, attackDamage);
        assertEquals(20, hero.getMana());

        int secondAttackDamage = hero.attack();
        assertEquals(0, secondAttackDamage);
        assertEquals(20, hero.getMana());
    }

    @Test
    public void testPotions() {
        hero.learn(new Spell("spell", 10, hero.getMana()));
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

}