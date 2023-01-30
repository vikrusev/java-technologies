import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PatternMatcher {


    public static boolean match(String s, String p) {

        if (s == null || p == null) return false;

        if (p.length() == 0) return true;

        p = p
                .replaceAll("\\.", "\\\\.")
                .replace('?', '.')
                .replaceAll("\\*", ".*")
                .replaceAll("\\+", "\\\\+")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)")
                .replaceAll("\\[", "\\\\[")
                .replaceAll("\\|", "\\\\|");


        return Pattern.compile(p).matcher(s).find();

    }
}
