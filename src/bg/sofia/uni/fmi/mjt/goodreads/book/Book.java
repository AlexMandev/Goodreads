package bg.sofia.uni.fmi.mjt.goodreads.book;

import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextUtils;

import java.util.List;

public record Book(
        String ID,
        String title,
        String author,
        String description,
        List<String> genres, // ["Mistery", "Thriller", "Mystery Thriller"]
        double rating,
        int ratingCount,
        String URL
) {
    public static final int FIELDS_COUNT = 8;
    private static final String INTEGER_DELIMITER = ",";
    private static final int ID_INDEX = 0;
    private static final int TITLE_INDEX = 1;
    private static final int AUTHOR_INDEX = 2;
    private static final int DESCRIPTION_INDEX = 3;
    private static final int GENRES_INDEX = 4;
    private static final int RATING_INDEX = 5;
    private static final int RATING_COUNT_INDEX = 6;
    private static final int URL_INDEX = 7;

    public static Book of(String[] tokens) {
        if (tokens == null) {
            throw new IllegalArgumentException("Tokens cannot be null.");
        }
        if (tokens.length != FIELDS_COUNT) {
            throw new IllegalArgumentException("Invalid tokens array size.");
        }
        return new Book(tokens[ID_INDEX], tokens[TITLE_INDEX],
                tokens[AUTHOR_INDEX], tokens[DESCRIPTION_INDEX],
                TextUtils.getGenresDatasetFormat(tokens[GENRES_INDEX]), Double.parseDouble(tokens[RATING_INDEX]),
                Integer.parseInt(tokens[RATING_COUNT_INDEX].replace(INTEGER_DELIMITER, "")),
                tokens[URL_INDEX]);
    }
}