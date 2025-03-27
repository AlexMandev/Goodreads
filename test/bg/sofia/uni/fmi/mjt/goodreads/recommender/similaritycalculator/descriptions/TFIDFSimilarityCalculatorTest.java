package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

public class TFIDFSimilarityCalculatorTest {
    private static final double DELTA = 0.0001;
    private static TextTokenizer tokenizer;
    private static Set<Book> books;
    private static Book book1;
    private static Book book2;
    @BeforeAll
    static void setUp() {
        tokenizer = mock();

        book1 = mock(Book.class);
        when(book1.description()).thenReturn("academy superhero club superhero");
        when(tokenizer.tokenize("academy superhero club superhero"))
                .thenReturn(List.of("academy", "superhero", "club", "superhero"));

        book2 = mock(Book.class);
        when(book2.description()).thenReturn("superhero mission save club");
        when(tokenizer.tokenize("superhero mission save club"))
                .thenReturn(List.of("superhero", "mission", "save", "club"));

        Book book3 = mock(Book.class);
        when(book3.description()).thenReturn("crime murder mystery club");
        when(tokenizer.tokenize("crime murder mystery club"))
                .thenReturn(List.of("crime", "murder", "mystery", "club"));

        books = Set.of(book1, book2, book3);
    }

    @Test
    void testComputeTFNullBookThrows() {
        TFIDFSimilarityCalculator calculator = new TFIDFSimilarityCalculator(books, tokenizer);
        assertThrows(IllegalArgumentException.class, () -> calculator.computeTF(null),
                "ComputeTF should throw for null book.");
    }

    @Test
    void testComputeTF() {
        TFIDFSimilarityCalculator calculator = new TFIDFSimilarityCalculator(books, tokenizer);

        Map<String, Double> tfScores = calculator.computeTF(book1);

        assertEquals(tfScores.get("academy"), 0.25, DELTA,
                "TF score of academy should be 0.25.");
        assertEquals(tfScores.get("superhero"), 0.50, DELTA,
                "TF score of superhero should be 0.50.");
        assertEquals(tfScores.get("club"), 0.25, DELTA,
                "TF score of club should be 0.25.");
    }

    @Test
    void testComputeIDF() {
        TFIDFSimilarityCalculator calculator = new TFIDFSimilarityCalculator(books, tokenizer);
        Map<String, Double> idfScores = calculator.computeIDF(book1);

        assertEquals(Math.log10(3), idfScores.get("academy"), DELTA,
                "IDF score of academy should equal log10(3).");
        assertEquals(Math.log10(1.5), idfScores.get("superhero"), DELTA,
                "IDF score of superhero should equal log10(3/2).");
        assertEquals(Math.log10(1), idfScores.get("club"), DELTA,
                "IDF score of club should equal log10(1).");
    }

    @Test
    void testComputeIDFNullBookThrows() {
        TFIDFSimilarityCalculator calculator = new TFIDFSimilarityCalculator(books, tokenizer);
        assertThrows(IllegalArgumentException.class, () -> calculator.computeIDF(null),
                "ComputeIDF should throw for null book.");
    }

    @Test
    void testConstructorNullBooks() {
        assertThrows(IllegalArgumentException.class, () -> new TFIDFSimilarityCalculator(null, tokenizer),
                "Construct should throw for null set of books.");
    }

    @Test
    void testConstructorNullTokenizer() {
        assertThrows(IllegalArgumentException.class, () -> new TFIDFSimilarityCalculator(books, null),
                "Constructor should throw for null tokenizer.");
    }

    @Test
    void testComputeTFIDFFormula() {
        TFIDFSimilarityCalculator calculator = new TFIDFSimilarityCalculator(books, tokenizer);

        Map<String, Double> tfScores = calculator.computeTF(book1);
        Map<String, Double> idfScores = calculator.computeIDF(book1);

        Map<String, Double> tfidfScores = calculator.computeTFIDF(book1);

        assertEquals(tfScores.get("superhero") * idfScores.get("superhero"),
                tfidfScores.get("superhero"), "TF-IDF score should be the product of the TF and IDF scores.");
        assertEquals(tfScores.get("academy") * idfScores.get("academy"),
                tfidfScores.get("academy"), "TF-IDF score should be the product of the TF and IDF scores.");
        assertEquals(tfScores.get("club") * idfScores.get("club"),
                tfidfScores.get("club"), "TF-IDF score should be the product of the TF and IDF scores.");

    }

    @Test
    void testComputeTFIDFThrowsForNullBook() {
        TFIDFSimilarityCalculator calculator = new TFIDFSimilarityCalculator(books, tokenizer);
        assertThrows(IllegalArgumentException.class, () -> calculator.computeTFIDF(null),
                "ComputeTFIDF should throw for null book.");
    }

    @Test
    void testCalculateSimilarity() {
        TFIDFSimilarityCalculator calculator = new TFIDFSimilarityCalculator(books, tokenizer);
        double similarity = calculator.calculateSimilarity(book1, book2);

        assertTrue(similarity > 0, "Books with common words should have positive similarity.");
    }
}
