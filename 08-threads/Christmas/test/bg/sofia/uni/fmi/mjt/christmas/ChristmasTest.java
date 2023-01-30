package bg.sofia.uni.fmi.mjt.christmas;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class ChristmasTest {

	private Workshop workshop;
	private Christmas christmas;

	@Before
	public void setUp() {
		final int numberOfWishes = 10;
		final int christmasTime = 1000;

		workshop = new Workshop();
		christmas = new Christmas(workshop, numberOfWishes, christmasTime);
	}

	@Test
	public void testGetGiftMethod() {
		assertTrue(Gift.getGift() instanceof Gift);
	}

	@Test
	public void testGetTypeMethod() {
		assertNotNull(Gift.getGift().getType());
	}

	@Test
	public void testGetCraftTimeMethod() {
		assertNotNull(Gift.getGift().getCraftTime());
	}

	@Test
	public void testCelebrateMethod() {
		final int numberOfWishes = 10;
		christmas.celebrate();

		assertEquals(numberOfWishes, workshop.getWishCount());
	}

	@Test
	public void testGetWorkshopMethod() {
		assertTrue(christmas.getWorkshop().equals(workshop));
	}

	@Test
	public void testWorkshop() {
		assertNull(workshop.nextGift());
		assertFalse(workshop.isChristmasTime);

		final int wishes = 1;
		workshop.postWish(Gift.getGift());

		assertEquals(workshop.getWishCount(), wishes);
	}

	@Test
	public void testElf() {
		final int wishes = 10;

		for (int wish = 0; wish < wishes; wish++) {
			workshop.postWish(Gift.getGift());
		}

		Elf elf = workshop.getElves()[0];
		elf.run();

		workshop.isChristmasTime = true;

		assertEquals(elf.getTotalGiftsCrafted(), wishes);
	}
}