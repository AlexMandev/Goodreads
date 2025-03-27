package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class BookRecommender implements BookRecommenderAPI {

    private final Set<Book> initialBooks;
    private final SimilarityCalculator calculator;

    public BookRecommender(Set<Book> initialBooks, SimilarityCalculator calculator) {
        if (initialBooks == null) {
            throw new IllegalArgumentException("InitialBooks set cannot be null.");
        }
        if (calculator == null) {
            throw new IllegalArgumentException("SimilarityCalculator cannot be null.");
        }
        this.initialBooks = initialBooks;
        this.calculator = calculator;
    }

    /**
     * Uses a HashMap to store the similarities of the books in the set with book origin,
     * so we don't calculate them everytime when comparing two books when sorting.
     * Also uses a comparator to sort the entries in the result map by values.
     */
    @Override
    public SortedMap<Book, Double> recommendBooks(Book origin, int maxN) {
        if (origin == null) {
            throw new IllegalArgumentException("Origin book cannot be null.");
        }
        if (maxN <= 0) {
            throw new IllegalArgumentException("MaxN should be greater than zero.");
        }
        Map<Book, Double> similarities = new HashMap<>();
        for (Book book : initialBooks) {
            if (!book.equals(origin)) {
                similarities.put(book, calculator.calculateSimilarity(book, origin));
            }
        }
        Comparator<Book> similarityComparator = (first, second) -> {
            int doubleCompareResult = Double.compare(similarities.get(second), similarities.get(first));
            if (doubleCompareResult == 0) {
                return first.ID().compareTo(second.ID());
            }
            return doubleCompareResult;
        };
        SortedMap<Book, Double> recommendations = new TreeMap<>(similarityComparator);
        initialBooks.stream()
                .filter(book -> !book.equals(origin))
                .sorted(similarityComparator)
                .limit(maxN)
                .forEach(book -> recommendations.put(book, similarities.get(book)));
        return recommendations;
    }

}