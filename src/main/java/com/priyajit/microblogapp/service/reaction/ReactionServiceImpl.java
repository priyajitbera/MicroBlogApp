package com.priyajit.microblogapp.service.reaction;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.priyajit.microblogapp.dto.ReactionModel;
import com.priyajit.microblogapp.entity.Post;
import com.priyajit.microblogapp.entity.Reaction;
import com.priyajit.microblogapp.entity.Reply;
import com.priyajit.microblogapp.entity.User;
import com.priyajit.microblogapp.exception.DBException;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.PostNotFoundException;
import com.priyajit.microblogapp.exception.ReactionNotFoundException;
import com.priyajit.microblogapp.exception.ReplyNotFoundException;
import com.priyajit.microblogapp.exception.UserNotFoundException;
import com.priyajit.microblogapp.repository.ReactionRepo;
import com.priyajit.microblogapp.service.post.PostService;
import com.priyajit.microblogapp.service.reply.ReplyService;
import com.priyajit.microblogapp.service.user.UserService;

@Service
public class ReactionServiceImpl implements ReactionService {

    @Autowired
    private ReactionRepo reactionRepo;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    // ~ Implementation of the methods of the ReactionService interface
    // --------------------------------------------------------------------------------------------------//

    @Override
    public Reaction findById(Long reactionId) throws ReactionNotFoundException {
        Optional<Reaction> reactionOpt = reactionRepo.findById(reactionId);
        if (!reactionOpt.isPresent()) {
            throw new ReactionNotFoundException(reactionId);
        }
        return reactionOpt.get();
    }

    @Override
    public List<Reaction> findByPostId(Long postId) throws PostNotFoundException {
        Post post = postService.findByPostId(postId);
        List<Reaction> reactions = reactionRepo.findByPost(post);
        return reactions;
    }

    @Override
    public List<Reaction> findByReplyId(Long replyId) throws ReplyNotFoundException {
        Reply reply = replyService.findById(replyId);
        List<Reaction> reactions = reactionRepo.findByReply(reply);
        System.out.println(reactions);
        return reactions;
    }

    @Override
    public Reaction save(ReactionModel reactionModel)
            throws UserNotFoundException, PostNotFoundException, ReplyNotFoundException, DBException {

        // validate if reaction type is not one of the availables
        if (!reactionModel.getType().equals(Reaction.LIKE)
                && !reactionModel.getType().equals(Reaction.CELEBRATE)
                && !reactionModel.getType().equals(Reaction.LOVE)) {
            return null;
        }

        // find the user by given userId
        User user = userService.findByUserId(reactionModel.getUserId());

        // create reaction obj and set the values
        Reaction reaction = new Reaction();
        reaction.setType(reactionModel.getType());
        reaction.setUser(user);

        // reaction can be made to post or to a reply
        // if postId is provided then consider for a post
        // else if replyId is provided then consider for a reply
        if (reactionModel.getPostId() != null) {
            Post post = postService.findByPostId(reactionModel.getPostId());
            reaction.setPost(post);
        } else if (reactionModel.getReplyId() != null) {
            Reply reply = replyService.findById(reactionModel.getReplyId());
            // validate if no reply found with given replyId
            reaction.setReply(reply);
        }

        try {
            return reactionRepo.save(reaction);
        } catch (Exception ex) {
            throw new DBException(ex.getMessage());
        }
    }

    @Override
    public Reaction updateReaction(ReactionModel reactionModel)
            throws ReactionNotFoundException, DBException, EntityOwnerMismatchException {
        Reaction reaction = findById(reactionModel.getReactionId());

        // validate whether the entity owner is same as the current authenticated user
        if (!reaction.matchOwnerWithAutenticatedUser()) {
            throw new EntityOwnerMismatchException();
        }

        reaction.setType(reactionModel.getType());

        try {
            return reactionRepo.save(reaction);
        } catch (Exception ex) {
            throw new DBException(ex.getMessage());
        }
    }

    @Override
    public void deleteById(Long reactionId)
            throws ReactionNotFoundException, EntityOwnerMismatchException, DBException {
        Reaction reaction = findById(reactionId);

        // validate whether the entity owner is same as the current authenticated user
        if (!reaction.matchOwnerWithAutenticatedUser()) {
            throw new EntityOwnerMismatchException();
        }

        try {
            reactionRepo.delete(reaction);
        } catch (Exception ex) {
            throw new DBException(ex.getMessage());
        }

    }

}
