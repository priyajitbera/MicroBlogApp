package com.priyajit.microblogapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Object> savePost(@RequestBody PostModel postModel) {
        try {
            Post post = postService.save(postModel);
            return ResponseBuilder.buildResponse(post, HttpStatus.CREATED, "Post saved successfully!");
        } catch (UserNotFoundException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DBException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/byId/{postId}")
    public ResponseEntity<Object> getPostById(@PathVariable(name = "postId") Long postId) {
        try {
            Post post = postService.findByPostId(postId);
            return ResponseBuilder.buildResponse(post, HttpStatus.OK, null);
        } catch (PostNotFoundException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/byUserId/{userId}")
    public ResponseEntity<Object> getPostsByUserId(@PathVariable(name = "userId") Long userId) {
        try {
            List<Post> posts = postService.findByUserId(userId);
            return ResponseBuilder.buildResponse(posts, HttpStatus.OK, null);
        } catch (UserNotFoundException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<Object> updatePost(@RequestBody PostModel postModel) {
        try {
            Post post = postService.updatePost(postModel);
            return ResponseBuilder.buildResponse(post, HttpStatus.OK, "Post updated successfully!");
        } catch (UserNotFoundException | PostNotFoundException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityOwnerMismatchException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (DBException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Object> deletePostById(@PathVariable(name = "postId") Long postId) {
        try {
            postService.deletePostById(postId);
            return ResponseBuilder.buildResponse(null, HttpStatus.OK, "Post deleted successfully!");
        } catch (PostNotFoundException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityOwnerMismatchException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (DBException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
