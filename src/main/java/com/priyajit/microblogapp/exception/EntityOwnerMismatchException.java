package com.priyajit.microblogapp.exception;

public class EntityOwnerMismatchException extends Exception {

    public EntityOwnerMismatchException() {
        super("entity is not owned by currently authenticated user");
    }
}
