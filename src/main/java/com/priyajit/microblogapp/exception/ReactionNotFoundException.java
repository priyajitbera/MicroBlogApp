package com.priyajit.microblogapp.exception;

public class ReactionNotFoundException extends Exception {

    public ReactionNotFoundException(Long reactionId) {
        super("not reaction found with reactionId=" + reactionId);
    }
}
