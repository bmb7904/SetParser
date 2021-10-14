package bernardi;

import java.util.Scanner;

/**
 * A recursive descent parser that will parse sets. Sets can be composed of numbers and other sets, enclosed within
 * curly braces. E.G) {1,2,3,4,10} and {{1}, {243, 22}, {0,3,6,9,11,44}} and {} (The empty set). This program will
 * accept a string from a user and we assume that the string has already been scanned and tokenized, with white
 * space removed. Also, any number is replaced by a 'n' token. I.E.) {{1,2}, 3} -> {{n,n},n}. The parser is defined by
 * the following context free grammar:
 *
 *                                   set  -> { list }
 *                                   list -> head tail | ε
 *                                   head -> number | set
 *                                   tail -> , head tail | ε
 *
 * I have tested every conceivable case I could think of, and the parser works as intended, except for certain Strings
 * which were not failing, when they should have been failing. These were strings where the first element inside left
 * curly brace is ',' and they took the following form: {,n} {,n,n} {,n,n,n} and so on. In order for these to fail,
 * and have the parser return false, I realized that the list() method cannot ever try to match a ',' char. If
 * it does, the String cannot ever be valid, and thus the list() method should return false. This feels like a hack,
 * because I wanted each method to contain solely the rules according to the grammar, but I could not think of any other
 * solution to this problem, and I wanted the parser to work perfectly. As of now, I believe it does.
 *
 * @author Brett Bernardi
 */
public class Parser {
    // The string to parsed
    private String string;
    // The pointer that will keep track of each token in string as we parse
    private int currentIndex;

    /**
     * Constructor for Parser class
     * @param s
     */
    public Parser(String s) {
        this.string = s;
        this.currentIndex = 0;
    }

    /**
     * Function that will return true if the parser algorithm returns from parsing the ENTIRE string
     * and no errors were found.
     * @return
     */
    public boolean parse() {
        // If set is true
        if(set()) {
            // We need to check if we are at end of String, if not, parse error
            if(this.currentIndex == this.string.length()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Will match the argument char with the current token in the string. If they match, AND the current index is in
     * within the bounds of the string, return true and increment the pointer.
     * @param c
     * @return
     */
    public boolean match(char c) {
        // first check if valid index. If we are at an invalid index and match() is called, we have
        // a parse error.
        if(this.currentIndex <= this.string.length() - 1) {
            if (c == this.string.charAt(this.currentIndex)) {
                // "consume" token and return true
                this.currentIndex++;
                return true;
            }
        }
        return false;
    }

    /**
     * set  -> { list }
     *
     * Because set's production is very straightforward, it MUST take the form of { list }, and all other deviations
     * are a parse error.
     * @return
     */
    public boolean set() {

        if(match('{')) {

            // return false if list() isn't true. Parse Error!
            if(!list()) {
                return false;
            }

            // If list is true, it must be followed by '}' token
            if(match('}')) {
                return true;
            }
            // Parse Error if we don't end with '}'
            else {
                return false;
            }
        }
        // Parse error if this doesn't start with '{'
        else {
            return false;
        }

    }

    /**
     * List --> head tail | ε
     *
     * When list() is called, it can either be a head tail OR ε. According to my
     * understanding of this grammar (which may be wrong), in order for a list() to
     * give the epsilon production, you must get the epsilon production from the tail()
     * production. Head can fail and list can still give the epsilon production, like the empty set: {} as an example,
     * but tail CANNOT fail, otherwise, parse error. So I only check if tail fails, to return false for this function.
     * @return
     */
    public boolean list() {
        // Here is my "hack" that checks for the current token being a ',' while calling the list method.
        // We have a false head and a true tail, and return a true list, so long as the tail is the epsilon production
        // only. If we have a false head and true tail because tail parsed ", head tail", then that is a parse error.
        // From this list() method, I cannot tell if tail return epsilon or the full production of ", head tail", so to
        // check for that, I can check for a comma here.
        if(this.string.charAt(this.currentIndex) == ',') {
            return false;
        }
        boolean headTrue = head();
        boolean tailTrue = tail();

        // If tail returns false, this cannot be an epsilon production.
        if(!tailTrue) {
            return false;
        }

        // Either we have a valid "head tail", or we have the epsilon production
        return true;
    }
    /**
     * head -> number | set
     *
     * Will return true if a terminal 'n' is found, or if the set() method returns true, which means
     * a valid set was found.
     */
    public boolean head() {
        // first check if we are a number, if so consume and return true. Valid head
        if(match('n')) {
            // Matched and consumed 'n'. Return True!
            return true;
        }
        // if not 'n', it MUST be a set, and we can thus return the boolean of the set
        // ()  method.
        return set();
    }

    /**
     * tail -> , head tail | ε
     *
     * Will return true if a valid tail is found, according to the production. If the tail is not valid,
     * an epsilon production used, and will return true, so long as the conditions are met.
     * @return
     */
    public boolean tail() {
        if(match(',')) {
            // match and consumed ',' token

            // Now check for head. Parse error if head() is false, so return false.
            if(!head()) {
                return false;
            }

            // Now check for tail. Parse error if tail() is false. Return false.
            if(!tail()) {
                return false;
            }

            return true; // good tail if reached

        }
        return true; // epsilon production
    }

    public static void main(String[] args) {
        System.out.print("Please enter a string to be parsed: ");
        String userInput = (new Scanner(System.in)).next();

        Parser parser = new Parser(userInput);

        if(parser.parse()) System.out.println("Your string is valid!");
        else System.out.println("Your string is invalid!");
    }
}
