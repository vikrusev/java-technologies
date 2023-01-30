public class Counter {

    public int countTriples(String str){
        int counter = 0;

        for (int i = 2; i < str.length(); i++) {
            if (str.charAt(i) == str.charAt(i - 1) && str.charAt(i - 1) == str.charAt(i - 2))
                counter++;
        }

        return counter;
    }
}