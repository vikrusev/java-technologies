public class Anagram {

    private static boolean isLetter(char c){
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    private static boolean exists(StringBuilder word, char c){
        for(int i = 0; i < word.length(); ++i) {

            if(Character.toLowerCase(word.charAt(i)) == Character.toLowerCase(c)){
                word.deleteCharAt(i);
                return true;
            }
        }
        return false;
    }

    private static void cleanWord(StringBuilder word){

        for(int i = 0; i < word.length(); ++i){
            if(!isLetter(word.charAt(i))) {
                word.deleteCharAt(i);
                i--;
            }
        }

    }

    public boolean isAnagram(String str){
        String[] words = str.split(" ");
        boolean isAnagram = true;

        StringBuilder first = new StringBuilder(words[0]);
        StringBuilder second = new StringBuilder(words[1]);

        cleanWord(first);
        cleanWord(second);

        System.out.println("Clean word: " + first);
        System.out.println("Clean word: " + second);

        for(char c : first.toString().toCharArray() ){

            if(!exists(second, c)){
                isAnagram = false;
                break;
            }
        }

        return second.length() == 0 && isAnagram;
    }
}