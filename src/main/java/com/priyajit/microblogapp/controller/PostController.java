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
    public ResponseEntity<Object> savePost(@RequestBody PostModel postModel) throws UserNotFoundException {
        Post post = postService.save(postModel);
        return ResponseBuilder.buildResponse(post, HttpStatus.CREATED, "Post saved successfully!");
    }

    @GetMapping("/byId/{postId}")
    public ResponseEntity<Object> getPostById(@PathVariable(name = "postId") Long postId) throws PostNotFoundException {

        Post post = postService.findByPostId(postId);
        return ResponseBuilder.buildResponse(post, HttpStatus.OK, null);
    }

    @GetMapping("/byUserId/{userId}")
    public ResponseEntity<Object> getPostsByUserId(@PathVariable(name = "userId") Long userId)
            throws UserNotFoundException {

        List<Post> posts = postService.findByUserId(userId);
        return ResponseBuilder.buildResponse(posts, HttpStatus.OK, null);
    }

    @PatchMapping("/update")
    public ResponseEntity<Object> updatePost(@RequestBody PostModel postModel)
            throws UserNotFoundException, PostNotFoundException, EntityOwnerMismatchException {

        Post post = postService.updatePost(postModel);
        return ResponseBuilder.buildResponse(post, HttpStatus.OK, "Post updated successfully!");
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Object> deletePostById(@PathVariable(name = "postId") Long postId)
            throws PostNotFoundException, EntityOwnerMismatchException {

        postService.deletePostById(postId);
        return ResponseBuilder.buildResponse(null, HttpStatus.OK, "Post deleted successfully!");
    }
}
