package bg.uni.sofia.fmi.mjt.dungeon;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;

public abstract interface TestUtils {
	public default Hero createTestHero() {
		return new Hero("hero", 100, 100);
	}

	public default char[][] createTestMap() {
		char[][] map = { "###".toCharArray(),
						 "TS.".toCharArray(),
						 "#EG".toCharArray() };
		return map;
	}
}
