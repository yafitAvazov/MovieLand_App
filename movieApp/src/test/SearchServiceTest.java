package services;

import Algorithms.IAlgoKMPSearch;
import Algorithms.IAlgoRabinKarpSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SearchServiceTest {

    private SearchService searchService;

    @BeforeEach
    public void setUp() {
        searchService = new SearchService();
    }

    @Test
    public void testSetSearchStrategy_WithKMPSearch() {
        String text = "This is a test text for KMP Search";
        String word = "KMP";

        searchService.setSearchStrategy(new IAlgoKMPSearch());
        int result = searchService.setAutomaticStrategy(text, word);

        assertTrue(result > 0);
    }

    @Test
    public void testSetSearchStrategy_WithRabinKarpSearch() {
        String text = "This is a test text for Rabin Karp Search";
        String word = "Rabin";

        searchService.setSearchStrategy(new IAlgoRabinKarpSearch());
        int result = searchService.setAutomaticStrategy(text, word);
        assertTrue(result > 0);
    }

    @Test
    public void testSetAutomaticStrategy_UsesRabinKarpForShortWord() {
        String text = "This is a short text for Rabin Karp Search";
        String shortWord = "short";

        int result = searchService.setAutomaticStrategy(text, shortWord);
        assertTrue(result > 0);
    }

    @Test
    public void testSetAutomaticStrategy_WordNotFound() {
        String text = "This is a test text";
        String word = "missing";

        int result = searchService.setAutomaticStrategy(text, word);

        assertEquals(0, result);
    }

    @Test
    public void testSetAutomaticStrategy_EmptyWord() {
        String text = "This is a test text";
        String word = "";

        int result = searchService.setAutomaticStrategy(text, word);

        assertEquals(0, result);
    }

    @Test
    public void testSetAutomaticStrategy_EmptyText() {
        String text = "";
        String word = "test";

        int result = searchService.setAutomaticStrategy(text, word);

        assertEquals(0, result);
    }
}
