package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextTokenizer {
    private final Set<String> stopwords;

    public TextTokenizer(Reader stopwordsReader) {
        try (var br = new BufferedReader(stopwordsReader)) {
            stopwords = br.lines().collect(Collectors.toSet());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load dataset", ex);
        }
    }

    public List<String> tokenize(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input text cannot be null.");
        }

        String cleanedText = input.replaceAll(TextUtils.PUNCTUATION_REGEX, "");
        cleanedText = cleanedText.replaceAll(TextUtils.WHITESPACE_REGEX, " ").trim();
        return Stream.of(cleanedText.split(" "))
                .map(String::toLowerCase)
                .filter(word -> !stopwords.contains(word))
                .toList();
    }

    public Set<String> stopwords() {
        return stopwords;
    }

}