package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TFIDFSimilarityCalculator implements SimilarityCalculator {

    private final Set<Book> books;
    private final TextTokenizer tokenizer;
    private final Map<String, Integer> wordBookOccurrences;

    public TFIDFSimilarityCalculator(Set<Book> books, TextTokenizer tokenizer) {
        if (books == null) {
            throw new IllegalArgumentException("Books cannot be null.");
        }
        if (tokenizer == null) {
            throw new IllegalArgumentException("TextTokenizer cannot be null.");
        }
        this.books = books;
        this.tokenizer = tokenizer;
        this.wordBookOccurrences = calculateWordBookOccurrences();
    }

    /*
     * Do not modify!
     */
    @Override
    public double calculateSimilarity(Book first, Book second) {
        Map<String, Double> tfIdfScoresFirst = computeTFIDF(first);
        Map<String, Double> tfIdfScoresSecond = computeTFIDF(second);

        return cosineSimilarity(tfIdfScoresFirst, tfIdfScoresSecond);
    }

    public Map<String, Double> computeTFIDF(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        Map<String, Double> tfScores = computeTF(book);
        Map<String, Double> idfScores = computeIDF(book);

        Map<String, Double> tfidfScores = new HashMap<>();

        for (var entry : tfScores.entrySet()) {
            tfidfScores.put(entry.getKey(), entry.getValue() * idfScores.get(entry.getKey()));
        }

        return tfidfScores;
    }

    public Map<String, Double> computeTF(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }

        List<String> descriptionWords = tokenizer.tokenize(book.description());
        Map<String, Integer> wordOccurrences = new HashMap<>();

        for (String word : descriptionWords) {
            wordOccurrences.put(word, wordOccurrences.getOrDefault(word, 0) + 1);
        }

        Map<String, Double> tfScores = new HashMap<>();
        wordOccurrences.forEach((word, occurrences) ->
                tfScores.put(word, (double) occurrences / descriptionWords.size()));

        return tfScores;
    }

    public Map<String, Double> computeIDF(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }

        List<String> descriptionWords = tokenizer.tokenize(book.description());
        Map<String, Double> idfScores = new HashMap<>();

        for (String word : descriptionWords) {
            int occ = wordBookOccurrences.get(word);
            idfScores.put(word, Math.log10((double) books.size() / occ));
        }

        return idfScores;
    }

    private double cosineSimilarity(Map<String, Double> first, Map<String, Double> second) {
        double magnitudeFirst = magnitude(first.values());
        double magnitudeSecond = magnitude(second.values());

        return dotProduct(first, second) / (magnitudeFirst * magnitudeSecond);
    }

    private double dotProduct(Map<String, Double> first, Map<String, Double> second) {
        Set<String> commonKeys = new HashSet<>(first.keySet());
        commonKeys.retainAll(second.keySet());

        return commonKeys.stream()
                .mapToDouble(word -> first.get(word) * second.get(word))
                .sum();
    }

    private double magnitude(Collection<Double> input) {
        double squaredMagnitude = input.stream()
                .map(v -> v * v)
                .reduce(0.0, Double::sum);

        return Math.sqrt(squaredMagnitude);
    }

    /**
     * Calculates in how many books' description each word occurs,
     * so we don't calculate it each time and tokenize every book's
     * description for the IDF score
     */
    private Map<String, Integer> calculateWordBookOccurrences() {
        Map<String, Integer> result = new HashMap<>();

        for (Book book : books) {
            List<String> tokenizedDescription = tokenizer.tokenize(book.description());
            Set<String> descriptionUniqueWords = new HashSet<>(tokenizedDescription);
            for (String word : descriptionUniqueWords) {
                result.put(word, result.getOrDefault(word, 0) + 1);
            }
        }

        return result;
    }
}