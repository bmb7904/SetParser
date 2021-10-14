package bernardi;

/**
 *
 * Very simple CFG example:
 *
 *
 * 1.) S --> a S b
 * 2.) S --> ε
 *
 *
 */
public class SimpleParser {
    private String string;
    private int currentIndex;

    public SimpleParser(String s) {
        this.string = s;
        this.currentIndex = 0;
    }

    public boolean parse() {
        if(S() == true) {
            if(this.currentIndex == this.string.length()) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function will try to match the argument given with the char at the current index of the String. I need to
     * check that the index is NOT out of bounds before calling the charAt() method, because invalid Strings will cause
     * this to go out of bounds. If the index is ever out of bounds, we know that we have an invalid string and can just
     * return false.
     * @param c
     * @return
     */
    private boolean match(char c) {
        // first check if valid index
        if(this.currentIndex <= this.string.length() - 1) {
            if (c == this.string.charAt(this.currentIndex)) {
                this.currentIndex++;
                return true;
            }

        }
        return false;
    }

    private boolean S() {

        if(match('a')) {
            System.out.println("index = " + this.currentIndex + " char = " + this.string.charAt(this.currentIndex));
            System.out.println("Calling S()");
            boolean temp = S();

            // return false if not true
            if(!temp) return false;
            System.out.println("S() returned true");

            if(match('b')) {
                System.out.println("b matched at " + this.currentIndex);
            }
            else {
                System.out.println("Didn't match b!");
                System.out.println("index = " + this.currentIndex);
                return false;
            }
        }
        return true; // skip -- Epsilon Production
    }
}
