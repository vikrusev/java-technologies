package bg.sofia.uni.fmi.mjt.grep;

import bg.sofia.uni.fmi.mjt.grep.utils.CommandParameters;
import org.junit.Test;

public class CommandParametersTests {

    @Test(expected = IllegalArgumentException.class)
    public void test() {
        String command = "grep";

        new CommandParameters(command);
    }

    private CommandParameters setDefaultProperties() {
        return null;
    }
}
