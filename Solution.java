import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        /*
         * Enter your code here. Read input from STDIN. Print output to STDOUT. Your
         * class should be named Solution.
         */
        Scanner sc = new Scanner(System.in);
        Set<Character> uniqueChars = new HashSet<>();

        while (sc.hasNextLine()) {
            String txt = sc.nextLine();

            sc.stream().forEach(c -> {
                if (uniqueChars.contains(c)) {
                    System.out.println("No");
                    return;
                }
                uniqueChars.add(c);
            });

            System.out.println(uniqueChars.join(" "));

        }

    }
}