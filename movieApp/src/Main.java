import Algorithms.IAlgoKMPSearch;
import Algorithms.IAlgoRabinKarpSearch;
import Algorithms.IAlgoMatchingStrings;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Example usage of the algorithms from your JAR file
        IAlgoMatchingStrings naiveSearch = new IAlgoRabinKarpSearch();
        IAlgoMatchingStrings kmpSearch = new IAlgoKMPSearch();

        String text = "onions, lettuce, gluten free bun";
        String patternSmall = "onion";
        String patternLarge = "gluten free bun";

        int matchesNaive = naiveSearch.search(text, patternSmall);
        int matchesKMP = kmpSearch.search(text, patternLarge);

        System.out.println("Naive Search Matches: " + matchesNaive);
        System.out.println("KMP Search Matches: " + matchesKMP);
    }
}