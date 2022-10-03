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

import com.priyajit.microblogapp.dto.ReplyModel;
import com.priyajit.microblogapp.entity.Reply;
import com.priyajit.microblogapp.exception.DBException;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.PostNotFoundException;
import com.priyajit.microblogapp.exception.ReplyNotFoundException;
import com.priyajit.microblogapp.exception.UserNotFoundException;
import com.priyajit.microblogapp.service.reply.ReplyService;

@RestController
@RequestMapping("/rest/reply")
public class ReplyController {

    @Autowired
    ReplyService replyService;

    @PostMapping("/save")
    public ResponseEntity<Object> saveReply(@RequestBody ReplyModel replyModel) {
        try {
            Reply reply = replyService.save(replyModel);
            return ResponseBuilder.buildResponse(reply, HttpStatus.CREATED, "Reply saved successfully!");
        } catch (UserNotFoundException | PostNotFoundException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DBException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/byId/{replyId}")
    public ResponseEntity<Object> getReplyById(@PathVariable(name = "replyId") Long replyId) {
        try {
            Reply reply = replyService.findById(replyId);
            return ResponseBuilder.buildResponse(reply, HttpStatus.OK, null);
        } catch (ReplyNotFoundException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/byPostId/{postId}")
    public ResponseEntity<Object> getRepliesByPostId(@PathVariable(name = "postId") Long postId) {
        try {
            List<Reply> replies = replyService.findByPostId(postId);
            return ResponseBuilder.buildResponse(replies, HttpStatus.OK, null);
        } catch (PostNotFoundException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<Object> updateReply(@RequestBody ReplyModel replyModel) {
        try {
            Reply reply = replyService.updateReply(replyModel);
            return ResponseBuilder.buildResponse(reply, HttpStatus.OK, "Reply updated successfully!");
        } catch (ReplyNotFoundException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityOwnerMismatchException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (DBException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{replyId}")
    public ResponseEntity<Object> deleteReplyById(@PathVariable(name = "replyId") Long replyId) {
        try {
            replyService.deleteById(replyId);
            return ResponseBuilder.buildResponse(null, HttpStatus.OK, "Reply deleted successfully!");
        } catch (ReplyNotFoundException | EntityOwnerMismatchException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DBException e) {
            return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
