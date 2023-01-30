package bg.sofia.uni.fmi.mjt.christmas;

import java.util.Random;

public class Kid extends Thread {

    private Workshop workshop;

    public Kid(Workshop workshop) {
        this.workshop = workshop;
    }

    /**
     * Sends a wish for the given gift to Santa's workshop.
     **/
    public void makeWish(Gift gift) {
        workshop.postWish(gift);
    }

    @Override
    public void run() {
       try {
           Thread.sleep(new Random().nextInt(100));
           makeWish(Gift.getGift());
       }
       catch (InterruptedException e) {
			e.printStackTrace();
       }
    }

}