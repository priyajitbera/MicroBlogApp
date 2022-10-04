package com.priyajit.microblogapp.service.post;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.priyajit.microblogapp.dto.PostModel;
import com.priyajit.microblogapp.entity.Post;
import com.priyajit.microblogapp.entity.Reaction;
import com.priyajit.microblogapp.entity.Reply;
import com.priyajit.microblogapp.entity.User;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.PostNotFoundException;
import com.priyajit.microblogapp.exception.UserNotFoundException;
import com.priyajit.microblogapp.repository.PostRepo;
import com.priyajit.microblogapp.repository.ReactionRepo;
import com.priyajit.microblogapp.repository.ReplyRepo;
import com.priyajit.microblogapp.service.user.UserService;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepo postRepo;

    @Autowired
    UserService userService;

    @Autowired
    ReplyRepo replyRepo;

    @Autowired
    ReactionRepo reactionRepo;

    // ~ Implementation of the methods of the PostService interface
    // --------------------------------------------------------------------------------------------------//

    @Override
    public Post save(PostModel postModel) throws UserNotFoundException {
        User user = userService.findByUserId(postModel.getUserId());
        Post post = new Post();
        post.setCaption(postModel.getCaption());
        post.setUser(user);
        post.setCreationDate(new Date());

        return postRepo.save(post);
    }

    @Override
    public Post findByPostId(Long postId) throws PostNotFoundException {
        Optional<Post> postOpt = postRepo.findById(postId);
        if (!postOpt.isPresent()) {
            throw new PostNotFoundException(postId);
        }
        return postOpt.get();
    }

    @Override
    public List<Post> findByUserId(Long userId) throws UserNotFoundException {
        User user = userService.findByUserId(userId);
        return postRepo.findByUser(user);
    }

    @Override
    public Post updatePost(PostModel postModel)
            throws PostNotFoundException, EntityOwnerMismatchException {
        Post post = findByPostId(postModel.getPostId());

        // validate whether the entity owner is same as the current authenticated user
        if (!post.matchOwnerWithAutenticatedUser()) {
            throw new EntityOwnerMismatchException();
        }

        post.setCaption(postModel.getCaption());
        post.setEdited(true);

        return postRepo.save(post);

    }

    @Override
    public void deletePostById(Long postId) throws PostNotFoundException, EntityOwnerMismatchException {
        Post post = findByPostId(postId);

        // validate whether the entity owner is same as the current authenticated user
        if (!post.matchOwnerWithAutenticatedUser()) {
            throw new EntityOwnerMismatchException();
        }

        // delete child entities first to avoid foreign key constraint
        // delete child entity : Reply
        List<Reply> replies = replyRepo.findByPost(post);
        System.out.println(replies);
        for (Reply reply : replies)
            replyRepo.deleteById(reply.getReplyId());

        // delete child entity : Reaction
        List<Reaction> reactions = reactionRepo.findByPost(post);
        System.out.println(reactions);
        for (Reaction reaction : reactions)
            reactionRepo.deleteById(reaction.getReactionId());

        postRepo.deleteById(postId);

    }

}
