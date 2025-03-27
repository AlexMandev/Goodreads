package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CompositeSimilarityCalculatorTest {

    @Test
    void testCalculateSimilarity() {
        Book first = new Book("", "", "", "", Collections.emptyList(),
                0.0, 0, "");
        Book second = new Book("", "", "", "", Collections.emptyList(),
                0.0, 0, "");

        SimilarityCalculator firstCalculator = mock();
        when(firstCalculator.calculateSimilarity(first, second)).thenReturn(0.3);

        SimilarityCalculator secondCalculator = mock();
        when(secondCalculator.calculateSimilarity(first, second)).thenReturn(0.7);

        Map<SimilarityCalculator, Double> map = new HashMap<>();
        map.put(firstCalculator, 0.5);
        map.put(secondCalculator, 0.8);

        SimilarityCalculator calculator = new CompositeSimilarityCalculator(map);

        assertEquals(0.3 * 0.5 + 0.7 * 0.8, calculator.calculateSimilarity(first, second),
                "Similarity should equal calculators multiplied by their weights.");
    }

    @Test
    void testConstructNullMap() {
        assertThrows(IllegalArgumentException.class, () -> new CompositeSimilarityCalculator(null),
                "Should throw when the calculator map is null.");
    }
}
