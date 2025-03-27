package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.HashSet;
import java.util.Set;

public class GenresOverlapSimilarityCalculator implements SimilarityCalculator {

    private int intersectionSize(Set<String> first, Set<String> second) {
        int intersection = 0;
        for (String element : first) {
            if (second.contains(element)) {
                intersection++;
            }
        }
        return intersection;
    }

    private double calculateSimilarityOfSets(Set<String> first, Set<String> second) {
        return (double) intersectionSize(first, second) / Math.min(first.size(), second.size());
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        if (first.genres().isEmpty() || second.genres().isEmpty()) {
            return 0.0;
        }
        Set<String> firstSet = new HashSet<>(first.genres());
        Set<String> secondSet = new HashSet<>(second.genres());
        return calculateSimilarityOfSets(firstSet, secondSet);
    }

}