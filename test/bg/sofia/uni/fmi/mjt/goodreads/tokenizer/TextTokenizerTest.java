package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TextTokenizerTest {

    private static final String stopWords = """
            cant
            havent
            about
            a
            """;

    @Test
    void testTokenizeCleansWhitespacesAndToLower() {
        String text = "   He can't read! a book...";
        TextTokenizer tokenizer = new TextTokenizer(new StringReader(stopWords));

        List<String> tokenizedText = tokenizer.tokenize(text);
        assertEquals(3, tokenizedText.size(),
                "The number of words after tokenizing should equal the words in the text minus " +
                        "the number of stop words");
        assertEquals("he", tokenizedText.getFirst(), "First word is expected 'he'.");
        assertEquals("read", tokenizedText.get(1), "First word is expected 'read'.");
        assertEquals("book", tokenizedText.get(2), "First word is expected 'book'.");
    }

    @Test
    void testTokenizeNullInput() {
        TextTokenizer tokenizer = new TextTokenizer(new StringReader(stopWords));

        assertThrows(IllegalArgumentException.class, () -> tokenizer.tokenize(null),
                "Should throw for null text input.");
    }
}
