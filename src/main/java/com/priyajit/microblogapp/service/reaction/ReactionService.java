package com.priyajit.microblogapp.service.reaction;

import java.util.List;

import com.priyajit.microblogapp.dto.ReactionModel;
import com.priyajit.microblogapp.entity.Reaction;
import com.priyajit.microblogapp.exception.DBException;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.PostNotFoundException;
import com.priyajit.microblogapp.exception.ReactionNotFoundException;
import com.priyajit.microblogapp.exception.ReplyNotFoundException;
import com.priyajit.microblogapp.exception.UserNotFoundException;

public interface ReactionService {

    public Reaction save(ReactionModel reactionModel)
            throws UserNotFoundException, PostNotFoundException, ReplyNotFoundException, DBException;

    public Reaction findById(Long reactionId) throws ReactionNotFoundException;

    public List<Reaction> findByPostId(Long postId) throws PostNotFoundException;

    public List<Reaction> findByReplyId(Long replyId) throws ReplyNotFoundException;

    public Reaction updateReaction(ReactionModel reactionModel) throws ReactionNotFoundException, DBException;

    public void deleteById(Long reactionId) throws ReactionNotFoundException, EntityOwnerMismatchException, DBException;
}
