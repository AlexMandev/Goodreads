package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres.GenresOverlapSimilarityCalculator;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

public class BookRecommenderTest {

    @Test
    void testRecommendBooksWith5BooksReturnsHighestSimilarity() {
        Book book1 = new Book("Book1", "Book1", "", "", Collections.emptyList(),
                0.0, 0, "");
        Book book2 = new Book("Book2", "Book2", "", "", Collections.emptyList(),
                0.0, 0, "");
        Book book3 = new Book("Book3", "Book3", "", "", Collections.emptyList(),
                0.0, 0, "");
        Book book4 = new Book("Book4", "Book4", "", "", Collections.emptyList(),
                0.0, 0, "");
        Book book5 = new Book("Book5", "Book5", "", "", Collections.emptyList(),
                0.0, 0, "");

        Book originBook = new Book("originBook", "originBook", "", "", Collections.emptyList(),
                0.0, 0, "");

        SimilarityCalculator calculator = mock();
        when(calculator.calculateSimilarity(book1, originBook)).thenReturn(0.9);
        when(calculator.calculateSimilarity(originBook, book1)).thenReturn(0.9);

        when(calculator.calculateSimilarity(book2, originBook)).thenReturn(0.8);
        when(calculator.calculateSimilarity(originBook, book2)).thenReturn(0.8);

        when(calculator.calculateSimilarity(book3, originBook)).thenReturn(0.7);
        when(calculator.calculateSimilarity(originBook, book3)).thenReturn(0.7);

        when(calculator.calculateSimilarity(book4, originBook)).thenReturn(0.0);
        when(calculator.calculateSimilarity(originBook, book4)).thenReturn(0.0);

        when(calculator.calculateSimilarity(book5, originBook)).thenReturn(0.0);
        when(calculator.calculateSimilarity(originBook, book5)).thenReturn(0.0);

        BookRecommender bookRecommender = new BookRecommender(Set.of(book1, book2, book3, book4, book5), calculator);
        SortedMap<Book, Double> recs = bookRecommender.recommendBooks(originBook, 3);

        assertSame(recs.pollFirstEntry().getKey(), book1,
                "The first book in the map should be with the highest similarity.");
        assertSame(recs.pollFirstEntry().getKey(), book2,
                "The second book in the map should be with the second highest similarity.");
        assertSame(recs.pollFirstEntry().getKey(), book3,
                "The third book in the map should be with the third highest similarity.");
    }

    @Test
    void testRecommendBooksNullOriginBookThrows() {
        Book book1 = new Book("Book1", "Book1", "", "", Collections.emptyList(),
                0.0, 0, "");
        BookRecommender recommender = new BookRecommender(Set.of(book1), new GenresOverlapSimilarityCalculator());

        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(null, 5),
                "RecommendBooks should throw for invalid origin book.");
    }

    @Test
    void testRecommendBooksInvalidMaxNThrows() {
        Book book1 = new Book("Book1", "Book1", "", "", Collections.emptyList(),
                0.0, 0, "");
        BookRecommender recommender = new BookRecommender(Set.of(book1), new GenresOverlapSimilarityCalculator());

        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(book1, -3),
                "RecommendBooks should throw for invalid maxN value.");
    }

    @Test
    void testConstructorNullSetThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new BookRecommender(null, new GenresOverlapSimilarityCalculator()),
                "Should throw when creating BookRecommender with invalid book set.");
    }

    @Test
    void testConstructorNullCalculator() {
        Book book1 = new Book("Book1", "Book1", "", "", Collections.emptyList(),
                0.0, 0, "");
        assertThrows(IllegalArgumentException.class,
                () -> new BookRecommender(Set.of(book1), null),
                "Should throw when creating BookRecommender with invalid similarity calculator.");
    }
}
