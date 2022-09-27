package com.priyajit.microblogapp.service.reply;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.priyajit.microblogapp.dto.ReplyModel;
import com.priyajit.microblogapp.entity.Post;
import com.priyajit.microblogapp.entity.Reply;
import com.priyajit.microblogapp.entity.User;
import com.priyajit.microblogapp.exception.DBException;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.PostNotFoundException;
import com.priyajit.microblogapp.exception.ReplyNotFoundException;
import com.priyajit.microblogapp.exception.UserNotFoundException;
import com.priyajit.microblogapp.repository.ReplyRepo;
import com.priyajit.microblogapp.service.post.PostService;
import com.priyajit.microblogapp.service.user.UserService;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyRepo replyRepo;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    // ~ Implementation of the methods of the ReplyService interface
    // --------------------------------------------------------------------------------------------------//

    @Override
    public Reply findById(Long replyId) throws ReplyNotFoundException {
        Optional<Reply> replyOpt = replyRepo.findById(replyId);
        if (!replyOpt.isPresent()) {
            throw new ReplyNotFoundException(replyId);
        }
        return replyOpt.get();
    }

    @Override
    public Reply save(ReplyModel replyModel) throws UserNotFoundException, PostNotFoundException, DBException {
        User user = userService.findByUserId(replyModel.getUserId());
        Post post = postService.findByPostId(replyModel.getPostId());
        Reply reply = new Reply();
        reply.setUser(user);
        reply.setPost(post);
        reply.setReply(replyModel.getReply());
        reply.setCreationDate(replyModel.getCreationDate());

        try {
            return replyRepo.save(reply);
        } catch (Exception ex) {
            throw new DBException(ex.getMessage());
        }

    }

    @Override
    public List<Reply> findByPostId(Long postId) throws PostNotFoundException {
        Post post = postService.findByPostId(postId);
        return replyRepo.findByPost(post);
    }

    @Override
    public Reply updateReply(ReplyModel replyModel)
            throws ReplyNotFoundException, EntityOwnerMismatchException, DBException {
        Reply reply = findById(replyModel.getReplyId());

        // validate whether the entity owner is same as the current authenticated user
        if (!reply.matchOwnerWithAutenticatedUser()) {
            throw new EntityOwnerMismatchException();
        }

        reply.setReply(replyModel.getReply());
        reply.setEdited(true);
        try {
            return replyRepo.save(reply);
        } catch (Exception ex) {
            throw new DBException(ex.getMessage());
        }
    }

    @Override
    public void deleteById(Long replyId) throws ReplyNotFoundException, EntityOwnerMismatchException, DBException {
        Reply reply = findById(replyId);

        // validate whether the entity owner is same as the current authenticated user
        if (!reply.matchOwnerWithAutenticatedUser()) {
            throw new EntityOwnerMismatchException();
        }

        try {
            replyRepo.delete(reply);
        } catch (Exception ex) {
            throw new DBException(ex.getMessage());
        }
    }

}
