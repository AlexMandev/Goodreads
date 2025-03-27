package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

public class BookFinderTest {

    private static Set<Book> books;
    private static Book book1;
    private static Book book2;
    private static Book book3;
    private static Book book4;
    private static Book book5;
    private static TextTokenizer tokenizer;
    @BeforeAll
    static void setUp() {
        book1 = new Book("Book1", "Book1 secret", "author1",
                "description with some words", List.of("genre1", "genre2"),
                0.0, 0, "");

        book2 = new Book("Book2", "Book2", "author1",
                "another description with some words", List.of("genre1", "genre3", "genre5"),
                0.0, 0, "");

        book3 = new Book("Book3", "Book3", "author2",
                "third description example", List.of("genre1", "genre5"),
                0.0, 0, "");

        book4 = new Book("Book4", "Book4 title", "author2",
                "hello this is a book", List.of("genre1", "genre3"),
                0.0, 0, "");

        book5 = new Book("Book5", "Book5", "author2", "",
                List.of("genre1", "genre4", "genre5"),
                0.0, 0, "");

        books = Set.of(book1, book2, book3, book4, book5);
        tokenizer = mock();
        when(tokenizer.tokenize(book1.description())).thenReturn(List.of("description", "with", "some", "words"));
        when(tokenizer.tokenize(book2.description())).thenReturn(List.of("another", "description", "with", "some", "words"));
        when(tokenizer.tokenize(book3.description())).thenReturn(List.of("third", "description", "example"));
        when(tokenizer.tokenize(book4.description())).thenReturn(List.of("hello", "this", "is", "a", "book"));
        when(tokenizer.tokenize(book5.description())).thenReturn(Collections.emptyList());

        when(tokenizer.tokenize(book1.title())).thenReturn(List.of("book1", "secret"));
        when(tokenizer.tokenize(book2.title())).thenReturn(List.of("book2"));
        when(tokenizer.tokenize(book3.title())).thenReturn(List.of("book3"));
        when(tokenizer.tokenize(book4.title())).thenReturn(List.of("book4", "title"));
        when(tokenizer.tokenize(book5.title())).thenReturn(List.of("book5"));
    }

    @Test
    void testSearchByAuthor() {
        BookFinder bookFinder = new BookFinder(books, tokenizer);

        List<Book> searched = bookFinder.searchByAuthor("author1");
        assertEquals(2, searched.size(),
                "searchByAuthor should return 2 books, when there are 2 books by this author in the set.");
        assertTrue(searched.contains(book1), "searchByAuthor should contain the book by this author");
        assertTrue(searched.contains(book2), "searchByAuthor should contain the book by this author");
    }

    @Test
    void testSearchByAuthorNull() {
        BookFinder bookFinder = new BookFinder(books, tokenizer);
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByAuthor(null),
                "Should throw for null author.");
    }

    @Test
    void testSearchByGenresMatchAll() {
        BookFinder bookFinder = new BookFinder(books, tokenizer);
        List<Book> searched = bookFinder.searchByGenres(Set.of("genre1", "genre5"), MatchOption.MATCH_ALL);

        assertEquals(3, searched.size());
        assertTrue(searched.contains(book2), "Result should contain a book with the searched genres.");
        assertTrue(searched.contains(book3), "Result should contain a book with the searched genres.");
        assertTrue(searched.contains(book5), "Result should contain a book with the searched genres.");
    }

    @Test
    void testSearchByGenresMatchAny() {
        BookFinder bookFinder = new BookFinder(books, tokenizer);
        List<Book> searched = bookFinder.searchByGenres(Set.of("genre1", "genre5", "genre3"), MatchOption.MATCH_ANY);

        assertEquals(5, searched.size(),
                "Result should contain all books, when every contains a searched genre with MATCH_ANY.");
    }

    @Test
    void testSearchByGenresNullKeywordsSet() {
        BookFinder bookFinder = new BookFinder(books, tokenizer);
        assertThrows(IllegalArgumentException.class,
                () -> bookFinder.searchByGenres(null, MatchOption.MATCH_ALL),
                "Should throw when keywords set is null.");
    }

    @Test
    void testSearchByGenresNullMatchOption() {
        BookFinder bookFinder = new BookFinder(books, tokenizer);
        assertThrows(IllegalArgumentException.class,
                () -> bookFinder.searchByGenres(Set.of("word"), null),
                "Should throw when MatchOption is null.");
    }

    @Test
    void testSearchByKeywordsMatchAllInTheDescription() {
        BookFinder bookFinder = new BookFinder(books, tokenizer);
        List<Book> searched = bookFinder.searchByKeywords(Set.of("description", "words"), MatchOption.MATCH_ALL);

        assertEquals(2, searched.size(),
                "Search result should contain all books with the keywords.");
        assertTrue(searched.contains(book1),
                "Search result should contain a book with the given keywords in its description");
        assertTrue(searched.contains(book2),
                "Search result should contain a book with the given keywords in its description");
    }

    @Test
    void testSearchByKeywordsMatchAnyInTheDescription() {
        BookFinder bookFinder = new BookFinder(books, tokenizer);
        List<Book> searched = bookFinder.searchByKeywords(Set.of("description", "words"), MatchOption.MATCH_ANY);

        assertEquals(3, searched.size(),
                "Should return all books that contain at least one of the keywords when MATCH_ANY");
        assertTrue(searched.contains(book1),
                "Search result should contain a book with at least one keyword");
        assertTrue(searched.contains(book2),
                "Search result should contain a book with at least one keyword");
        assertTrue(searched.contains(book3),
                "Search result should contain a book with at least one keyword");
    }

    @Test
    void testSearchByKeywordsMatchAllInTitleAndDescription() {
        BookFinder bookFinder = new BookFinder(books, tokenizer);
        List<Book> searched = bookFinder.searchByKeywords(Set.of("Description", "secret"), MatchOption.MATCH_ALL);

        assertEquals(1, searched.size(),
                "Search result should still return a book, if the keywords are split in" +
                        "the description and title.");
        assertTrue(searched.contains(book1),
                "Search result should still return a book, if the keywords are split in" +
                        "the description and title.");
    }

    @Test
    void testSearchByKeywordsNullKeyWords() {
        BookFinder bookFinder = new BookFinder(books, tokenizer);
        assertThrows(IllegalArgumentException.class,
                () -> bookFinder.searchByKeywords(null, MatchOption.MATCH_ALL),
                "Should throw if keywords set is null.");
    }
    @Test
    void testSearchByKeywordsNullMatchOption() {
        BookFinder bookFinder = new BookFinder(books, tokenizer);
        assertThrows(IllegalArgumentException.class,
                () -> bookFinder.searchByKeywords(Set.of("word"), null),
                "Should throw if MatchOption is null.");
    }

    @Test
    void testGetAllGenres() {
        BookFinder bookFinder = new BookFinder(books, tokenizer);
        Set<String> genres = bookFinder.allGenres();

        assertEquals(5, genres.size(),
                "Genres said should contain all different genres.");
        assertTrue(genres.contains("genre1"), "Genres said should contain all different genres.");
        assertTrue(genres.contains("genre2"), "Genres said should contain all different genres.");
        assertTrue(genres.contains("genre3"), "Genres said should contain all different genres.");
        assertTrue(genres.contains("genre4"), "Genres said should contain all different genres.");
        assertTrue(genres.contains("genre5"), "Genres said should contain all different genres.");
    }

    @Test
    void testConstructorNullBooksSet() {
        assertThrows(IllegalArgumentException.class, () -> new BookFinder(null, tokenizer),
                "Should throw for null book set.");
    }

    @Test
    void testConstructorNullBooksTokenizer() {
        assertThrows(IllegalArgumentException.class, () -> new BookFinder(books, null),
                "Should throw for null tokenizer.");
    }
}
