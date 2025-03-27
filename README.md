# 📚 Goodreads Book Recommender

> A Java application for book enthusiasts that recommends and discovers books based on machine learning principles

## Project Description

This application was developed as part of the Modern Java Technologies course at FMI. It implements a book recommendation engine that analyzes a dataset of 10,000 top-rated books to suggest similar titles based on genre overlap and textual similarity.

By applying fundamental machine learning approaches, the system can find books that match a user's preferences and provide intelligent search functionality.

## Core Algorithms

The recommendation system is built on two key algorithms:

- **TF-IDF (Term Frequency-Inverse Document Frequency)**: Evaluates the importance of words in book descriptions
- **Overlap Coefficient**: Measures similarity between sets of genres

## Key Components

The project consists of several modules:

### Data Processing
- Dataset parsing with OpenCSV
- Text tokenization with stopword removal

### Recommendation Engine
- Genre-based similarity calculation
- Description-based similarity using TF-IDF
- Composite similarity scoring with weighted results

### Search Functionality
- Author-based search
- Genre-based filtering (with ALL/ANY matching)
- Keyword discovery in titles and descriptions

## Tech Stack

- Java 17
- OpenCSV library
- JUnit testing framework
- Java Collections Framework
- Java Stream API

## Input Dataset

The system works with a modified Kaggle dataset containing 10,000 books with the following attributes:

| Field | Description |
|-------|-------------|
| Book | Title of the book |
| Author | Name of the author |
| Description | Book synopsis/summary |
| Genres | List of genres in format "[genre1, genre2, ...]" |
| Avg_Rating | Average user rating |
| Num_Ratings | Number of ratings received |
| URL | Link to book page |

### Example Usage

```java
// Load books from CSV
Set<Book> library = BookLoader.load(new FileReader("goodreads_data.csv"));

// Create a book recommender with composite similarity calculator
SimilarityCalculator calculator = new CompositeSimilarityCalculator(Map.of(
    new GenresOverlapSimilarityCalculator(), 0.7,
    new TFIDFSimilarityCalculator(library), 0.3
));
BookRecommenderAPI recommender = new BookRecommender(library, calculator);

// Get book recommendations
Book referenceBook = /* select a book */;
SortedMap<Book, Double> recommendations = recommender.recommendBooks(referenceBook, 5);

// Search for books
BookFinderAPI finder = new BookFinder(library);
List<Book> fantasyBooks = finder.searchByGenres(Set.of("Fantasy"), MatchOption.MATCH_ANY);
```

## Academic Context

This project demonstrates practical application of:
- Object-oriented design principles
- Java Collections Framework optimization
- Text processing techniques
- Statistical similarity algorithms
- Functional programming with Streams API
