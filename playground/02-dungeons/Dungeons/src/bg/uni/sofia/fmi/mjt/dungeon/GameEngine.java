package bg.uni.sofia.fmi.mjt.dungeon;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;
import bg.uni.sofia.fmi.mjt.dungeon.actor.Enemy;
import bg.uni.sofia.fmi.mjt.dungeon.actor.Position;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Treasure;

import java.util.Arrays;

public class GameEngine {

    private char[][] map;
    private Hero hero;
    private Enemy[] enemies;
    private Treasure[] treasures;

    public GameEngine(char[][] map, Hero hero, Enemy[] enemies, Treasure[] treasures) {

        setHero(hero);
        this.map = setMap(map);
        this.enemies = setEnemies(enemies);
        this.treasures = setTreasures(treasures);
    }

    public char[][] getMap() {
        return this.map;
    }

    public Position getHeroPosition() {
        return this.hero.getPosition();
    }

    public Hero getHero() {
        return this.hero;
    }

    public String makeMove(Direction direction) {
        char next = peekMove(direction);

        if (next == '.') {
            move(direction);
            return "You moved successfully to the next position.";
        }

        if (next == '#') {
            return "Wrong move. There is an obstacle and you cannot bypass it.";
        }

        if (next == 'T') {
            move(direction);

            String message = treasures[0].collect(hero);
            treasures = Arrays.copyOfRange(treasures, 1, treasures.length);

            return message;
        }

        if (next == 'E') {
            Enemy enemy = enemies[0];
            while (hero.getHealth() > 0 && enemy.getHealth() > 0) {
                enemy.takeDamage(hero.attack());

                if (enemy.getHealth() <= 0) {
                    move(direction);
                    enemies = Arrays.copyOfRange(enemies, 1, enemies.length);
                    return "Enemy died.";
                }

                hero.takeDamage(enemy.attack());
                if (hero.getHealth() <= 0) {
                    return "Hero is dead! Game over!";
                }
            }
        }

        if (next == 'G') {
            return "You have successfully passed through the dungeon. Congrats!";
        }

        return "Unknown command entered.";
    }

    private char peekMove(Direction direction) {
        int x = this.hero.getPosition().getX();
        int y = this.hero.getPosition().getY();

        switch (direction) {
            case LEFT: return this.map[x][y - 1];
            case UP: return this.map[x - 1][y];
            case DOWN: return this.map[x + 1][y];
            case RIGHT: return this.map[x][y + 1];
            default: return '+';
        }
    }

    private void move(Direction direction) {
        int x = this.hero.getPosition().getX();
        int y = this.hero.getPosition().getY();

        int newX = x;
        int newY = y;

        this.map[x][y] = '.';

        switch (direction) {
            case LEFT: newY = y - 1;
                        break;
            case UP: newX = x - 1;
                        break;
            case DOWN: newX = x + 1;
                        break;
            case RIGHT: newY = y + 1;
                        break;
        }

        this.map[newX][newY] = 'H';
        this.hero.getPosition().setPosition(newX, newY);
    }

    private void setHero(Hero hero) {
        this.hero = new Hero(hero.getName(), hero.getHealth(), hero.getMana());

        if (hero.getSpell() != null) {
            this.hero.learn(hero.getSpell());
        }

        if (hero.getWeapon() != null) {
            this.hero.equip(hero.getWeapon());
        }
    }

    private char[][] setMap(char[][] map) {
        int rows = map.length;
        int cols = map[0].length;

        char[][] newMap = new char[rows][cols];

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (map[i][j] == 'S') {
                    this.hero.createPosition(i, j);
                }

                newMap[i][j] = map[i][j];
            }
        }

        return newMap;
    }

    private Enemy[] setEnemies(Enemy[] enemies) {
        Enemy[] newEnemies = new Enemy[enemies.length];

        System.arraycopy(enemies, 0, newEnemies, 0, enemies.length);

        return newEnemies;
    }

    private Treasure[] setTreasures(Treasure[] treasures) {
        Treasure[] newTreasures = new Treasure[treasures.length];

        System.arraycopy(treasures, 0, newTreasures, 0, treasures.length);

        return newTreasures;
    }

}
