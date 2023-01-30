package bg.sofia.uni.fmi.mjt.christmas;

public class Elf extends Thread {

	private int id;
	private Gift gift;
	private Workshop workshop;
	private int totalGiftsCrafted = 0;

	public Elf(int id, Workshop workshop) {
		this.id = id;
		this.workshop = workshop;
	}

	/**
	 * Gets a wish from the backlog and creates the wanted gift.
	 **/
	public void craftGift() {
		gift = workshop.nextGift();
	}

	/**
	 * Returns the total number of gifts that the given elf has crafted.
	 **/
	public int getTotalGiftsCrafted() {
		return totalGiftsCrafted;
	}

	@Override
	public void run() {
		final int sleepTime = 500;

		do {
			craftGift();
			try {
				if (gift == null) {
					Thread.sleep(sleepTime);
					continue;
				} else {
					Thread.sleep(gift.getCraftTime());
					totalGiftsCrafted++;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (gift != null);

		System.out.println(id + ", " + totalGiftsCrafted);
	}

	public int getElfId() {
		return this.id;
	}
}