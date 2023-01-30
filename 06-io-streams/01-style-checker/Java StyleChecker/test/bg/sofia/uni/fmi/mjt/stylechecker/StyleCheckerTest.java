package bg.sofia.uni.fmi.mjt.stylechecker;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static bg.sofia.uni.fmi.mjt.stylechecker.FixMeMessages.*;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class StyleCheckerTest {

    private String getCheckerResponse(String inputCode, String properties) {

        ByteArrayInputStream propertiesInput = new ByteArrayInputStream(properties.getBytes());

        StyleChecker checker = new StyleChecker(propertiesInput);

        ByteArrayInputStream input = new ByteArrayInputStream(inputCode.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        checker.checkStyle(input, output);
        String actual = new String(output.toByteArray());

        return actual.trim();
    }

    private void assertStyleCheckerEquals(String inputCode, String expectedOutput, String message, String properties) {

        assertEquals(message, expectedOutput, this.getCheckerResponse(inputCode, properties));

    }


    @Test
    public void numberOfStatementsWrong_DefaultSettingsTest() {

        assertStyleCheckerEquals(
                "doSomething(); counter++; doSomethingElse(counter);",
                multipleStatementsError.getMessage() + "\r\ndoSomething(); counter++; doSomethingElse(counter);",
                "More than one number of statements per line should not be allowed on default settings",
                "");

    }

    @Test
    public void numberOfStatementsWrong_TrueSettingsTest() {

        assertStyleCheckerEquals(
                "doSomething(); counter++; doSomethingElse(counter);",
                multipleStatementsError.getMessage() + "\r\ndoSomething(); counter++; doSomethingElse(counter);",
                "More than one number of statements per line should not be allowed on setting set to true",
                "statements.per.line.check.active=true");

    }

    @Test
    public void numberOfStatementsWrong_WithoutSettingTest() {

        assertStyleCheckerEquals(
                "doSomething(); counter++; doSomethingElse(counter);",
                "doSomething(); counter++; doSomethingElse(counter);",
                "More than one number of statements per line should be allowed when settings are off",
                "statements.per.line.check.active=false");

    }

    @Test
    public void numberOfStatementsCorrect_DefaultSettingsTest() {

        String codeString = "doSomething();\r\ncounter++;\r\ndoSomethingElse(counter);";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "Correct syntax of statements should not be labeled as wrong when on default settings",
                "");

    }

    @Test
    public void numberOfStatementsCorrect_TrueSettingsTest() {

        String codeString = "doSomething();\r\ncounter++;\r\ndoSomethingElse(counter);";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "Correct syntax of statements should not be labeled as wrong when setting is set to true",
                "statements.per.line.check.active=true");

    }

    @Test
    public void numberOfStatementsCorrect_FalseSettingsTest() {

        String codeString = "doSomething();\r\ncounter++;\r\ndoSomethingElse(counter);";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "Correct syntax of statements should not be labeled as wrong when setting is set to false",
                "statements.per.line.check.active=false");

    }

    @Test
    public void multipleSemicolons_DefaultSettingsTest() {

        String codeString = "doSomething();;;\r\ncounter++;\r\ndoSomethingElse(counter);;";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "Multiple semicolons should not be considered as different statements",
                "");

    }

    @Test
    public void multipleSemicolonsWithMultipleStatements_DefaultSettingsTest() {

        String codeString = "doSomething();;;counter++;\r\ndoSomethingElse(counter);;";

        assertStyleCheckerEquals(
                codeString,
                multipleStatementsError.getMessage() + "\r\n" + codeString,
                "More than one number of statements per line should not be allowed on default settings",
                "");

    }

    @Test
    public void multipleSemicolonsWithMultipleStatements_TrueSettingsTest() {

        String codeString = "doSomething();;;counter++;\r\ndoSomethingElse(counter);;";

        assertStyleCheckerEquals(
                codeString,
                multipleStatementsError.getMessage() + "\r\n" + codeString,
                "More than one number of statements per line should not be allowed when setting is set to true",
                "statements.per.line.check.active=true");

    }

    @Test
    public void multipleSemicolonsWithMultipleStatements_FalseSettingsTest() {

        String codeString = "doSomething();;;counter++;\r\ndoSomethingElse(counter);;";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "More than one number of statements per line should be allowed when setting is set to false",
                "statements.per.line.check.active=false");

    }

    /*
    @Ignore
    @Test
    public void fakeMultipleStatements_DefaultSettingsTest() {

        String codeString = "String a = \"some code; some other code()\";";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "Semicolons in strings should not be considered as separate statements on default settings",
                "");

    }

    @Ignore
    @Test
    public void fakeMultipleStatementsForLoopTest() {

        String codeString = "for(int i = 0; i < 10; i++){\nSystem.out.println(\"ok\");}";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "Multiple statements in for loops are fine",
                "");

    }*/


    @Test
    public void wildcards_DefaultSettingsTest() {

        String codeString = "import java.lang.*;";

        assertStyleCheckerEquals(
                codeString,
                wildcardsError.getMessage() + "\r\n" + codeString,
                "Wildcards should not be allowed on default settings",
                "");

    }

    @Test
    public void wildcards_TrueSettingsTest() {

        String codeString = "import java.lang.*;";

        assertStyleCheckerEquals(
                codeString,
                wildcardsError.getMessage() + "\r\n" + codeString,
                "Wildcards should not be allowed when setting is set to true",
                "wildcard.import.check.active=true");

    }

    @Test
    public void wildcards_FalseSettingsTest() {

        String codeString = "import java.lang.*;";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "Wildcards should be allowed when setting is set to false",
                "wildcard.import.check.active=false");

    }


    @Test
    public void wildcardsCorrectTest() {

        String codeString = "import java.lang.String;";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "Normal imports should be allowed",
                "");

    }

    @Test
    public void wildcardsCorrectTestExceedingLengthLimit() {

        String codeString = "import java.lang.StringIWishICouldTurnBackTimeToTheGoodOldDaysWhenOurMommaSangUs" +
                "ToSleepButNowWereStressedOutWishWeCouldTurnBackTimeToTheGoodOldDaaaaaaaayyyyssss;";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "Normal imports, exceeding limit should be allowed",
                "");

    }

    /*
    @Ignore
    @Test
    public void fakeWildcardTest() {

        String codeString = "String a = \"import java.lang.*\";";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "Wildcards inside strings are fine",
                "");

    }

    @Ignore
    @Test
    public void fakeWildcardMultipleLinesStringTest() {

        String codeString = "String a = \"something\" + \n\"import java.lang.*\";";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "Wildcards inside multiple line strings are fine",
                "");
    }*/

    @Test
    public void openingBracketOnNextLine_DefaultSettingsTest() {

        String codeString = "public static void main(String[] args)\r\n{\r\nSystem.out.println(\"Hello\");\r\n}";

        assertStyleCheckerEquals(
                codeString,
                "public static void main(String[] args)\r\n" + sameLineBracketsError.getMessage()
                        + "\r\n{\r\nSystem.out.println(\"Hello\");\r\n}",
                "Opening bracket on new line should not be allowed on default settings",
                "");

    }

    @Test
    public void openingBracketOnNextLine_TrueSettingsTest() {

        String codeString = "public static void main(String[] args)\n{\nSystem.out.println(\"Hello\");\n}";

        assertStyleCheckerEquals(
                codeString,
                "public static void main(String[] args)\r\n" + sameLineBracketsError.getMessage()
                        + "\r\n{\r\nSystem.out.println(\"Hello\");\r\n}",
                "Opening bracket on new line should not be allowed when setting is set to true",
                "opening.bracket.check.active=true");

    }

    @Test
    public void openingBracketOnNextLine_FalseSettingsTest() {

        String codeString = "public static void main(String[] args)\r\n{\r\nSystem.out.println(\"Hello\");\r\n}";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "Opening bracket on new line should be allowed when setting is set to false",
                "opening.bracket.check.active=false");

    }

    @Test
    public void openingBracketOnSameLineTest() {

        String codeString = "public static void main(String[] args) {\r\nSystem.out.println(\"Hello\");\r\n}";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "Opening bracket on same line has to be allowed",
                "");
    }


    @Test
    public void lineSize_DefaultSettingsTest() {

        String codeString = "String a = \"3iou24cmfrifeifjf2jfrf3ejdhcr"
                            + "i34uxri43uddewdekeldcfjcojrwecihweiehmiechreirhcihecunehrunehcuiercnenurhn\";";

        assertStyleCheckerEquals(
                codeString,
                tooLargeLineError.getMessage() + "\r\n" + codeString,
                "More than 100 characters are not allowed on default settings",
                "");
    }

    @Test
    public void lineSize_TrueSettingsTest() {

        String codeString = "String a = \"3iou24cmfrifeifjf2jfrf3ejdhcri34uxri43uddewdekel"
                            + "dcfjcojrwecihweiehmiechreirhcihecunehrunehcuiercnenurhn\";";

        assertStyleCheckerEquals(
                codeString,
                tooLargeLineError.getMessage() + "\r\n" + codeString,
                "More than 100 characters are not allowed when setting is set to true and maxSize = 100",
                "length.of.line.check.active=true\r\nline.length.limit=100");
    }

    @Test
    public void lineSize_FalseSettingsTest() {

        String codeString = "String a = \"3iou24cmfrifeifjf2jfrf3ejdhcri34uxri43uddewdekeldcfj"
                            + "cojrwecihweiehmiechreirhcihecunehrunehcuiercnenurhn\";";

        assertStyleCheckerEquals(
                codeString,
                codeString,
                "More than 100 characters are allowed on settings = false and maxSize = whatever",
                "length.of.line.check.active=false\r\nline.length.limit=1");
    }


    @Test
    public void multipleErrorsPerLineTest() {

        String codeString = "String a = \"3iou24cmfrifeifjf2jfrf3ejdhcri34uxri43uddewdeke"
                            + "ldcfjcojrwecihweiehmiechreirhcihecunehrunehcuiercnenurhn\"; int a = 5; int b = 7;";

        assertThat("Multiple errors per line can happen", this.getCheckerResponse(codeString, ""), anyOf(
                containsString(multipleStatementsError.getMessage() +
                                "\r\n" + tooLargeLineError.getMessage() +
                                "\r\n" + codeString),
                containsString(tooLargeLineError.getMessage() +
                                "\r\n" + multipleStatementsError.getMessage()
                                + "\r\n" + codeString)));

    }

}
