package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BookFinder implements BookFinderAPI {
    private final Set<Book> books;
    private final TextTokenizer tokenizer;

    public BookFinder(Set<Book> books, TextTokenizer tokenizer) {
        if (books == null) {
            throw new IllegalArgumentException("The set of books cannot be null.");
        }
        if (tokenizer == null) {
            throw new IllegalArgumentException("The tokenizer cannot be null.");
        }
        this.books = books;
        this.tokenizer = tokenizer;
    }

    public Set<Book> allBooks() {
        return books;
    }

    @Override
    public List<Book> searchByAuthor(String authorName) {
        if (authorName == null || authorName.isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty.");
        }
        return books.stream()
                .filter(book -> book.author().equalsIgnoreCase(authorName))
                .toList();
    }

    @Override
    public Set<String> allGenres() {
        return books.stream()
                .map(Book::genres)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Book> searchByGenres(Set<String> genres, MatchOption option) {
        if (genres == null) {
            throw new IllegalArgumentException("Genres cannot be null.");
        }
        if (option == null) {
            throw new IllegalArgumentException("Match option cannot be null.");
        }
        return books.stream()
                .filter(genrePredicate(genres, option))
                .toList();
    }

    @Override
    public List<Book> searchByKeywords(Set<String> keywords, MatchOption option) {
        if (keywords == null) {
            throw new IllegalArgumentException("Keywords cannot be null.");
        }
        if (option == null) {
            throw new IllegalArgumentException("Option cannot be null.");
        }
        return books.stream()
                .filter(keywordPredicate(keywords, option))
                .toList();
    }

    private boolean matchesCollectionContains(Collection<String> searched, Collection<String> source,
                                              MatchOption option) {
        return switch (option) {
            case MATCH_ALL -> searched.stream().allMatch(source::contains);
            case MATCH_ANY -> searched.stream().anyMatch(source::contains);
        };
    }

    private Predicate<Book> genrePredicate(Set<String> genres, MatchOption option) {
        Set<String> genresToLower = genres.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return book -> {
            List<String> bookGenres = book.genres().stream().map(String::toLowerCase).toList();
            return matchesCollectionContains(genresToLower, bookGenres, option);
        };
    }

    private Predicate<Book> keywordPredicate(Set<String> keywords, MatchOption option) {
        Set<String> keywordsToLower = keywords.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return book -> {
            List<String> allWords = new ArrayList<>(tokenizer.tokenize(book.title()));
            allWords.addAll(tokenizer.tokenize(book.description()));

            return matchesCollectionContains(keywordsToLower, allWords, option);
        };
    }
}