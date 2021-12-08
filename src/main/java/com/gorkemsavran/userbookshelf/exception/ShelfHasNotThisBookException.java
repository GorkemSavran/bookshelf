package com.gorkemsavran.userbookshelf.exception;

public class ShelfHasNotThisBookException extends RuntimeException {

    public ShelfHasNotThisBookException(String s) {
        super(s);
    }

    public ShelfHasNotThisBookException() {
        super("Shelf has not this book!");
    }
}
