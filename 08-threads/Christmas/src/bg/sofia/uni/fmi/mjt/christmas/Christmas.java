package bg.sofia.uni.fmi.mjt.christmas;

public class Christmas {

    private Workshop workshop;

    /**
     * The number of kids that are going to send wishes to Santas's workshop.
     **/
    private int numberOfKids;

    /**
     * Christmas will start in {@code christmasTime} milliseconds.
     **/
    private int christmasTime;

    public Christmas(Workshop workshop, int numberOfKids, int christmasTime) {
        this.workshop = workshop;
        this.numberOfKids = numberOfKids;
        this.christmasTime = christmasTime;
    }

    public static void main(String[] args) {
        Christmas christmas = new Christmas(new Workshop(), 50, 2000);
        christmas.celebrate();
    }

    public void celebrate() {
        for (int i = 0; i < this.numberOfKids; ++i) {
            Kid kid = new Kid(getWorkshop());
            kid.start();
            try {
            	kid.join();
            } catch (InterruptedException e) {
            	e.printStackTrace();
            }
        }
	    for (Elf elf : workshop.getElves()) {
			elf.start();
	    }

        for (Elf elf : workshop.getElves()) {
		    System.out.println(elf.getElfId() + " " + elf.getTotalGiftsCrafted());
	    }
    }

    public Workshop getWorkshop() {
        return workshop;
    }
}