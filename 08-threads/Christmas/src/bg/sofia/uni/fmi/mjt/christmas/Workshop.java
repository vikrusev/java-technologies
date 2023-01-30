package bg.sofia.uni.fmi.mjt.christmas;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Workshop {

    private final int maxElves = 20;

    private Queue<Gift> backlog;
    private Elf[] elves;
    private List<Gift> readyGifts;

    private int totalWishes;

    public boolean isChristmasTime;

    public Workshop() {
        backlog = new LinkedBlockingQueue<>();
        elves = new Elf[maxElves];

        totalWishes = 0;
        isChristmasTime = false;

        readyGifts = new ArrayList<>();

        for (int i = 0; i < maxElves; ++i) {
            elves[i] = new Elf(i, this);
        }
    }

    /**
     * Adds a gift to the elves' backlog.
     **/
    public synchronized void postWish(Gift gift) {
        backlog.add(gift);
        totalWishes++;
    }

    /**
     * Returns the next gift from the elves' backlog that has to be manufactured.
     **/
    public synchronized Gift nextGift() {
        return backlog.poll();
    }

    /**
     * Returns an array of the elves working in Santa's workshop.
     **/
    public Elf[] getElves() {
        return this.elves;
    }

    /**
     * Returns the total number of wishes sent to Santa's workshop by the kids.
     **/
    public int getWishCount() {
        return this.totalWishes;
    }

    public Queue<Gift> getBacklog() {
        return this.backlog;
    }

	public boolean makeGiftReady(Gift gift) {
    	return readyGifts.add(gift);
	}
}