//      8.8 / 10.0  ~grader.sapera.org~

public class PatternMatcher {

    public static boolean match(String s, String p) {

        /*StringBuilder regex = new StringBuilder();
        regex.append(".*");

        for (int i = 0; i < p.length(); ++i) {
            switch (p.charAt(i)) {
                case '?': regex.append(".");
                            break;
                case '*': regex.append("(\\*|(.*))");
                            break;
                default: regex.append(p.charAt(i));
                            break;
            }
        }

        regex.append(".*");
        System.out.println(regex);

        return s.matches(regex.toString());*/

        /*
            THIS GETS 8.8 / 10.0 FROM THE GRADER*/
        if (p.length() == 0) return true;
        if (s.length() == 0) return match(s, p.substring(1));

        if (p.charAt(0) == '?') return match(s.substring(1), p.substring(1));
        if (p.charAt(0) == '*') {
            if (s.charAt(0) != '*') {
                return match(s, p.substring(1));
            }
            else {
                return match(s.substring(1), p.substring(1));
            }
        }

        int n = s.length();
        int m = p.length();
        int j = 0;
        int i = 0;

        while (j < n && s.charAt(j) != p.charAt(i)) {
            j++;
        }

        if (j == n) return false;

        while (j < n && i < m && s.charAt(j) == p.charAt(i)) {
            i++;
            j++;

            if (p.length() > i && p.charAt(i) == '?') {
                i++;
                j++;

                if (i == m && m > n || j > n) return false;
            }
        }

        if (i == m - 1 && p.charAt(i) == '*') return true;
        if (i == m) return true;
        if (j == n) return match(s, p.substring(1));

        if (p.length() > i && p.charAt(i) == '*') {
            return match(s.substring(j), p.substring(i + 1));
        }

        return match(s.substring(j), p.substring(j));
    }
}