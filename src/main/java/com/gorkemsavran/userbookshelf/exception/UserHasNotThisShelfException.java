package com.gorkemsavran.userbookshelf.exception;

public class UserHasNotThisShelfException extends RuntimeException {

    public UserHasNotThisShelfException(String s) {
        super(s);
    }

    public UserHasNotThisShelfException() {
        super("User has not this shelf!");
    }
}
