package com.priyajit.microblogapp.exception;

public class ReplyNotFoundException extends Exception {

    public ReplyNotFoundException(Long replyId) {
        super("reply not found with replyId=" + replyId);
    }
}
