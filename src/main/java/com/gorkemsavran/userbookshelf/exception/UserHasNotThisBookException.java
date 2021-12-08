package com.gorkemsavran.userbookshelf.exception;

public class UserHasNotThisBookException extends RuntimeException {

    public UserHasNotThisBookException(String s) {
        super(s);
    }

    public UserHasNotThisBookException() {
        super("User has not this book!");
    }
}