package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TextUtilsTest {

    @Test
    void testGetGenresEmpty() {
        assertTrue(TextUtils.getGenresDatasetFormat("[]").isEmpty(),
                "Should return empty list for empty brackets.");
    }

    @Test
    void testGetGenres() {
        List<String> genres = TextUtils.getGenresDatasetFormat("['Classical', 'Fiction']");
        assertEquals(genres.getFirst(), "Classical", "First genre should be 'Classical'.");
        assertEquals(genres.get(1), "Fiction", "Second genre should be 'Fiction'.");
    }
}
