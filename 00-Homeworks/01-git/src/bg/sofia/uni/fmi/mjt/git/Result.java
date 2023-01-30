package bg.sofia.uni.fmi.mjt.git;

public class Result {

    private final boolean isSuccessful;
    private final String message;

    public Result(String message, boolean isSuccessful) {
        this.message = message;
        this.isSuccessful = isSuccessful;
    }

    //returns a boolean wheather the operation is a failure or success
    public boolean isSuccessful() {
        return this.isSuccessful;
    }

    //returns the message of the operation
    public String getMessage() {
        return this.message;
    }

}
