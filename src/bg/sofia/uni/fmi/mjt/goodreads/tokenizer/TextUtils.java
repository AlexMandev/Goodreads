package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class TextUtils {
    private static final String GENRE_SPLIT_REGEX = ", ";

    public static final String PUNCTUATION_REGEX = "\\p{Punct}";
    public static final String WHITESPACE_REGEX = "\\s+";

    public static List<String> getGenresDatasetFormat(String genres) {
        String removedParentheses = genres.substring(1, genres.length() - 1);
        if (removedParentheses.isEmpty() || removedParentheses.isBlank()) {
            return Collections.emptyList();
        }
        return Stream.of(removedParentheses.split(GENRE_SPLIT_REGEX))
                .map(s -> s.substring(1, s.length() - 1))
                .toList();
    }
}
