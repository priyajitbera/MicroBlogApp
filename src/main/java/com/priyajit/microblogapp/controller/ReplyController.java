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
    public Reply saveReply(@RequestBody ReplyModel replyModel) {
        try {
            return replyService.save(replyModel);
        } catch (UserNotFoundException | PostNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DBException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/byId/{replyId}")
    public Reply getReplyById(@PathVariable(name = "replyId") Long replyId) {
        try {
            return replyService.findById(replyId);
        } catch (ReplyNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/byPostId/{postId}")
    public List<Reply> getRepliesByPostId(@PathVariable(name = "postId") Long postId) {
        try {
            return replyService.findByPostId(postId);
        } catch (PostNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/update")
    public Reply updateReply(@RequestBody ReplyModel replyModel) {
        try {
            return replyService.updateReply(replyModel);
        } catch (ReplyNotFoundException e) {
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

    @DeleteMapping("/delete/{replyId}")
    public void deleteReplyById(@PathVariable(name = "replyId") Long replyId) {
        try {
            replyService.deleteById(replyId);
        } catch (ReplyNotFoundException | EntityOwnerMismatchException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (DBException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
