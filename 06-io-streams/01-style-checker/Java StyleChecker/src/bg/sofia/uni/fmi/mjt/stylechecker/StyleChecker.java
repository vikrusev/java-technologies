package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Properties;

import java.lang.reflect.Method;

import static bg.sofia.uni.fmi.mjt.stylechecker.FixMeMessages.*;

/**
 * Used for static code checks of Java files.
 *
 * Depending on a stream from user-defined configuration or default values, it
 * checks if the following rules are applied:
 * <ul>
 * <li>there is only one statement per line;</li>
 * <li>the line lengths do not exceed 100 (or user-defined number of) characters;</li>
 * <li>the import statements do not use wildcards;</li>
 * <li>each opening block bracket is on the same line as the declaration.</li>
 * </ul>
 */
public class StyleChecker {

    /**
     *  properties - stores the settings provided by default or the user
     *  invokeMethods - stores the desired Style-Check methods (the ones which setting is set to true)
     */
    private Properties properties = new Properties();
    private ArrayList<String> invokeMethods = new ArrayList<>();

    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    // sorcery, this little boy was the reason to get 1/10 of the points :(
    private boolean ujas = false;

    /**
     * Creates a StyleChecker with properties having the following default values:
     * <ul>
     * <li>{@code wildcard.import.check.active=true}</li>
     * <li>{@code statements.per.line.check.active=true}</li>
     * <li>{@code opening.bracket.check.active=true }</li>
     * <li>{@code length.of.line.check.active=true}</li>
     * <li>{@code line.length.limit=100}</li>
     * </ul>
     **/
    public StyleChecker() {
        setDefaultProperties();
        findMethodsToBeInvoked();
    }

    /**
     * Creates a StyleChecker with custom configuration, based on the content from
     * the given {@code inputStream}. If the stream does not contain any of the
     * properties, the missing ones get their default values.
     *
     * @param inputStream - the user defined properties
     */
    public StyleChecker(InputStream inputStream) {
        setDefaultProperties();
        setUserInputProperties(inputStream);

        findMethodsToBeInvoked();
    }

    private void setDefaultProperties() {
        this.properties.setProperty("line.length.limit", "100");
        this.properties.setProperty("statements.per.line.check.active", "true");
        this.properties.setProperty("length.of.line.check.active", "true");
        this.properties.setProperty("wildcard.import.check.active", "true");
        this.properties.setProperty("opening.bracket.check.active", "true");
    }

    private void setUserInputProperties(InputStream inputStream) {
        try {
            this.properties.load(inputStream);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Finds all properties set to true
     * And adds them to the invokeMethods array with "Check" as a prefix
     * All methods in StyleChecker that check certain rule must be formed like this:
     *      { propertyName[0] }{ "Check" }
     *      where propertyName[0] is the first word from each property
     */
    private void findMethodsToBeInvoked() {
        for (String name : properties.stringPropertyNames()) {
            if (properties.getProperty(name).equals("true")) {
                invokeMethods.add(name.split("\\.")[0] + "Check");
            }
        }
    }

    /**
     * For each line from the given {@code source} InputStream writes fix_me comment
     * for the violated rules (if any) with an explanation of the style error
     * followed by the line itself in the {@code output}.
     *
     * This method takes every line from {@code source} and runs ONLY the test methods
     * that should be invoked based on the supplied properties
     *
     * If the array invokeMethods is empty - we have all settings set to 'false'
     * Else - for each line from 'source' we run all tests that are in 'invokeMethods'
     *
     * @param source - the source code
     * @param output - the code with added a fix_me comment
     */
    public void checkStyle(InputStream source, OutputStream output) {


        try (BufferedReader sourceCode = new BufferedReader(new InputStreamReader(source))) {
            String line = sourceCode.readLine();
            boolean iteration = false;

            while (line != null) {
                if (!this.invokeMethods.isEmpty()) {
                    checkLineStyle(line);
                }

                if (!iteration && !this.ujas) {
                    iteration = true;
                    writeToThisOutput(line);
                }
                else {
                    writeToThisOutput("\r\n" + line);
                }

                line = sourceCode.readLine();
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        finally {
            try {
                (this.output).writeTo(output);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * This method uses java Reflection
     * It creates a method variable which holds the test method taken from invokeMethods array
     * Then, if not null, we invoke it
     *
     * @param line - the current line of the source code, @NotNull
     */
    private void checkLineStyle(String line) {
        Method method = null;

        for (String methodName : this.invokeMethods) {
            if (line.toLowerCase().contains("import") && methodName.equals("lengthCheck")) {
                continue;
            }
            try {
                method = this.getClass().getDeclaredMethod(methodName, String.class);

                /*
                 *  If the object method is .getDeclaredMethod(), we access this.class methods
                 *  thus we access it. Hence, the line below is not necessary.
                 */
                method.setAccessible(true);

            } catch (SecurityException | NoSuchMethodException e) {
                System.err.println(e.getMessage());
            }

            try {
                if (method != null) {
                    method.invoke(this, line);
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Receives a line as a String and writes it into this.output
     *
     * @param line - a raw line from sourceCode or a line that has been put through the checks
     *             it is guaranteed to be @NotNull
     */
    private void writeToThisOutput(String line) {
        try {
            this.output.write(line.getBytes());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * The following 4 check methods are called using reflection.
     * For this program they are ALWAYS used if some properties are set to true
     *
     * Also, they change this.output, which is lastly copied to the final desired output
     *
     * @param line - accepted by all methods below - that is the line to be style-checked
     */
    private void statementsCheck(String line) {

        String cleanLine = removeColumnsForStatementsCalculation(line);

        int numberOfStatements = cleanLine.length() == 0 ? 0 : cleanLine.split(";").length;

        if (numberOfStatements > 1) {
            this.ujas = true;
            writeToThisOutput("\r\n" + multipleStatementsError.getMessage());
        }

    }

    private void lengthCheck(String line) {

        int maxLen = Integer.parseInt(this.properties.getProperty("line.length.limit"));

        String temp = String.format("// FIXME Length of line should not exceed %d characters", maxLen);

        if (line.trim().length() > maxLen) {
            this.ujas = true;
            writeToThisOutput("\r\n" + temp);
        }

    }

    private void wildcardCheck(String line) {

        if (line.toLowerCase().contains("import")) {
            String[] importParts = line.split("\\.");
            String lastPart = importParts[importParts.length - 1].replace(";", "");
            lastPart = lastPart.trim();

            if (lastPart.equals("*")) {
                this.ujas = true;
                writeToThisOutput("\r\n" + wildcardsError.getMessage());
            }
        }
    }

    private void openingCheck(String line) {
        if (line.trim().charAt(0) == '{') {
            this.ujas = true;
            writeToThisOutput("\r\n" + sameLineBracketsError.getMessage());
        }
    }


    /**
     * @param str - @NotNull
     * @return - a clean String, with no duplicate ';' and no ';' in the beginning
     */
    private String removeColumnsForStatementsCalculation(String str) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < str.length() - 1 ; ++i) {
            if (i == 0) {
                result.append(str.charAt(i));
            }
            else {
                if (str.charAt(i - 1) != str.charAt(i)) {
                    result.append(str.charAt(i));
                }
            }
        }

        if (result.length() != 0) {
            return result.charAt(0) == ';' ? result.substring(1) : result.toString();
        }

        return result.toString();
    }
}