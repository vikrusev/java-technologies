package bg.sofia.uni.fmi.mjt.grep.utils;

import java.io.File;
import java.util.HashMap;

import bg.sofia.uni.fmi.mjt.grep.enums.PropertiesNames;
import static bg.sofia.uni.fmi.mjt.grep.enums.PropertiesNames.*;

public class CommandParameters extends HashMap<PropertiesNames, Object> {

    public CommandParameters(String command) throws IllegalArgumentException {
        // set defaults of optional parameters
        this.put(PROP_WHOLE, false);
        this.put(PROP_IGNORE, false);
        this.put(PROP_PATH_DST, "");

        // fill all the rest
        fillProperties(command);
    }

    /*
     * Fills the {this} object with the parameters passed through the
     * @param command - the passed command by the user
     *
     * throws IllegalArgumentException if:
     * 1. the first word is not 'grep'
     * 2. not enough parameters are supplied in total or after the optional parameters
     */
    private void fillProperties(String command) throws IllegalArgumentException {

        // remove unwanted whitespaces
        command = command.trim().replaceAll( "\\s+", " ");

        // we don't want any magic numbers
        final int minParams = 4;
        String[] params = command.split(" ");

        // params[0] must always be 'grep'
        if (!params[0].equals("grep")) {
            throw new IllegalArgumentException("Unrecognised command. Did you mean 'grep' or 'quit'?");
        }

        if (params.length < minParams) {
            throw new IllegalArgumentException("Incorrect syntax.");
        }

        // needed to determine which is the next index of the array {params} based on how many optionals are passed
        int index = 1;

        // check the first optional parameters
        if (params[1].charAt(0) == '-') {
            ++index;
            this.setProperty(params[1].charAt(1));

            if (params[2].charAt(0) == '-') {
                ++index;
                this.setProperty(params[2].charAt(1));
            }
        }

        if (params.length < minParams + index - 1) {
            throw new IllegalArgumentException("Not enough parameters after the optional '-w' and/or '-i'.");
        }

        this.put(PROP_TO_FIND, params[index++]);
        this.put(PROP_PATH_SRC, params[index++]);

        // the 'maxThreads' parameter must be a positive number
        try {
            int maxThreads = Integer.parseInt(params[index++]);
            if (maxThreads > 0) {
                this.put(PROP_MAX_THREADS, maxThreads);
            }
            else {
                throw new IllegalArgumentException();
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The 'maxThread' parameter must be positive integer.");
        }


        // if we have a destination path add it
        if (params.length > index) {
            this.put(PROP_PATH_DST, params[index].replace("/", File.separator));
        }
    }

    /*
     *  Sets an optional parameter
     *  @param c - the parameter 'name'
     *  throws IllegalArgumentException if letter is not recognised
     */
    private void setProperty(Character c) throws IllegalArgumentException {
        if (c == 'w') {
            this.put(PROP_WHOLE, true);
        }
        else if (c == 'i') {
            this.put(PROP_IGNORE, true);
        }
        else {
            throw new IllegalArgumentException("An optional argument could not be recognised. " +
                                                "Only '-w' and '-i' are allowed.");
        }
    }

}
