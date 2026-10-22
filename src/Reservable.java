public interface Reservable {
    /** Reserves the book with the given id. */
    void reserveBook(String bookId);
}