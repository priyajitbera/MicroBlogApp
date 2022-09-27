package com.priyajit.microblogapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.priyajit.microblogapp.dto.PostModel;
import com.priyajit.microblogapp.entity.Post;
import com.priyajit.microblogapp.exception.DBException;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.PostNotFoundException;
import com.priyajit.microblogapp.exception.UserNotFoundException;
import com.priyajit.microblogapp.service.post.PostService;

@RestController
@RequestMapping("/rest/post")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/save")
    public Post savePost(@RequestBody PostModel postModel) {
        try {
            return postService.save(postModel);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DBException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/byId/{postId}")
    public Post getPostById(@PathVariable(name = "postId") Long postId) {
        try {
            return postService.findByPostId(postId);
        } catch (PostNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/byUserId/{userId}")
    public List<Post> getPostsByUserId(@PathVariable(name = "userId") Long userId) {
        try {
            return postService.findByUserId(userId);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/update")
    public Post updatePost(@RequestBody PostModel postModel) {
        try {
            return postService.updatePost(postModel);
        } catch (UserNotFoundException | PostNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityOwnerMismatchException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (DBException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{postId}")
    public void deletePostById(@PathVariable(name = "postId") Long postId) {
        try {
            postService.deletePostById(postId);
        } catch (PostNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityOwnerMismatchException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (DBException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
