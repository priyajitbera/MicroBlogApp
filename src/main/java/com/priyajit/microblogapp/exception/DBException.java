package com.priyajit.microblogapp.exception;

public class DBException extends Exception {

    public DBException(String message) {
        super("Exception occur while persist, message: " + message);
    }

}
