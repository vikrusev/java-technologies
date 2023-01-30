package bg.sofia.uni.fmi.mjt.carstore.exception;

public class CarNotFoundException extends RuntimeException  {

    public CarNotFoundException(String message) {
        super(message);
    }

}
