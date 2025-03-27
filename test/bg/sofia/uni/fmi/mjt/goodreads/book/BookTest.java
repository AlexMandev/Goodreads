package bg.sofia.uni.fmi.mjt.goodreads.book;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {
    @Test
    void testFactoryMethodOf() {
        String[] tokens = {
                "1",
                "book",
                "author",
                "book-description",
                "['Classical', 'Fiction']",
                "4.80",
                "1,000,005",
                "goodreads.com"
        };

        Book book = Book.of(tokens);
        assertEquals(book.ID(), tokens[0], "The ID of the book should be as given in the tokens array.");
        assertEquals(book.title(), tokens[1], "The title of the book should be as given in the tokens array.");
        assertEquals(book.author(), tokens[2], "The author of the book should be as given in the tokens array.");
        assertEquals(book.description(), tokens[3], "The description of the book should be as given in the tokens array.");
        assertIterableEquals(book.genres(), List.of("Classical", "Fiction"),
                "The genres of the book should be as given in the tokens array.");
        assertEquals(book.rating(), 4.80, "The rating of the book should be as given in the tokens array.");
        assertEquals(book.ratingCount(), 1_000_005,
                "The rating count of the book should be as given in the tokens array.");
        assertEquals(book.URL(), "goodreads.com", "The URL of the book should be as given in the tokens array");
    }

    @Test
    void testFactoryMethodOfThrowsForInvalidTokenArray() {
        int validSize = Book.FIELDS_COUNT;
        String[] tokens = new String[validSize + 1];

        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),
                "Book.of should throw for invalid token array size.");
    }

    @Test
    void testFactoryMethodOfThrowsForNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> Book.of(null),
                "Book.of should throw for null argument.");
    }
}
