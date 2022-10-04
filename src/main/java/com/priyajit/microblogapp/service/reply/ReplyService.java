package com.priyajit.microblogapp.service.reply;

import java.util.List;

import com.priyajit.microblogapp.dto.ReplyModel;
import com.priyajit.microblogapp.entity.Reply;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.PostNotFoundException;
import com.priyajit.microblogapp.exception.ReplyNotFoundException;
import com.priyajit.microblogapp.exception.UserNotFoundException;

public interface ReplyService {

    public Reply findById(Long replyId) throws ReplyNotFoundException;

    public Reply save(ReplyModel replyModel) throws UserNotFoundException, PostNotFoundException;

    public List<Reply> findByPostId(Long postId) throws PostNotFoundException;

    public Reply updateReply(ReplyModel replyModel)
            throws ReplyNotFoundException, EntityOwnerMismatchException;

    public void deleteById(Long replyId) throws ReplyNotFoundException, EntityOwnerMismatchException;
}
