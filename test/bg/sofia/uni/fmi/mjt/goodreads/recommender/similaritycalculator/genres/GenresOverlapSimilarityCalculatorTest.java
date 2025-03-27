package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GenresOverlapSimilarityCalculatorTest {

    private static SimilarityCalculator calc;
    private static double DELTA = 0.0001;

    @BeforeAll
    static void setUp() {
        calc = new GenresOverlapSimilarityCalculator();
    }

    @Test
    void testCalculateSimilarityWithZeroOverlappingGenres() {
        Book first = new Book("1", "first", "", "",
                List.of("Classical", "Fiction"), 0.0, 0, "");

        Book second = new Book("2", "second", "", "",
                List.of("History", "Educational"), 0.0, 0, "");

        assertEquals(0.0, calc.calculateSimilarity(first, second), DELTA,
                "Similarity should be 0.0 for books with no overlapping genres.");
    }

    @Test
    void testCalculateSimilarityIdenticalGenreSet() {
        Book first = new Book("1", "first", "", "",
                List.of("Genre1", "Genre2", "Genre3"), 0.0, 0, "");

        Book second = new Book("2", "second", "", "",
                List.of("Genre3", "Genre1", "Genre2"), 0.0, 0, "");

        assertEquals(1.0, calc.calculateSimilarity(first, second), DELTA,
                "Similarity should be 1.0 for books with same genres.");
    }

    @Test
    void testCalculateSimilarityFormula() {
        Book first = new Book("1", "first", "", "",
                List.of("Genre1", "Genre2", "Genre3", "Genre4", "Genre5", "Genre6", "Genre7", "Genre8"),
                0.0, 0, "");

        Book second = new Book("2", "second", "", "",
                List.of("Genre1", "Genre2", "Genre3", "Genre4", "Genre5", "Genre60", "Genre70"),
                0.0, 0, "");

        assertEquals(5.0/7.0, calc.calculateSimilarity(first, second), DELTA,
                "Similarity should equal the number of common genres, divided by the size" +
                        "of the smaller set.");
    }

    @Test
    void testCalculateNullBookThrows() {
        Book first = new Book("1", "first", "", "",
                List.of("Genre"),
                0.0, 0, "");

        assertThrows(IllegalArgumentException.class, () -> calc.calculateSimilarity(first, null));
    }

    @Test
    void testCalculateEmptyGenreSet() {
        Book first = new Book("1", "first", "", "",
                List.of("Genre"),
                0.0, 0, "");
        Book second = new Book("2", "second", "", "",
                Collections.emptyList(),
                0.0, 0, "");

        assertEquals(0.0, calc.calculateSimilarity(first, second), DELTA,
                "Similarity should equal 0.0 if at least one of the books' genre list is empty.");
    }
}
