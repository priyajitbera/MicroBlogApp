package com.priyajit.microblogapp.service.post;

import java.util.List;

import com.priyajit.microblogapp.dto.PostModel;
import com.priyajit.microblogapp.entity.Post;
import com.priyajit.microblogapp.exception.DBException;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.PostNotFoundException;
import com.priyajit.microblogapp.exception.UserNotFoundException;

public interface PostService {

    public Post save(PostModel postModel) throws UserNotFoundException, DBException;

    public Post findByPostId(Long postId) throws PostNotFoundException;

    public List<Post> findByUserId(Long userId) throws UserNotFoundException;

    public Post updatePost(PostModel postModel)
            throws UserNotFoundException, PostNotFoundException, EntityOwnerMismatchException, DBException;

    public void deletePostById(Long postId) throws PostNotFoundException, EntityOwnerMismatchException, DBException;
}