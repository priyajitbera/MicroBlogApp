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
    public ResponseEntity<Object> saveReply(@RequestBody ReplyModel replyModel)
            throws UserNotFoundException, PostNotFoundException {

        Reply reply = replyService.save(replyModel);
        return ResponseBuilder.buildResponse(reply, HttpStatus.CREATED, "Reply saved successfully!");
    }

    @GetMapping("/byId/{replyId}")
    public ResponseEntity<Object> getReplyById(@PathVariable(name = "replyId") Long replyId)
            throws ReplyNotFoundException {

        Reply reply = replyService.findById(replyId);
        return ResponseBuilder.buildResponse(reply, HttpStatus.OK, null);

    }

    @GetMapping("/byPostId/{postId}")
    public ResponseEntity<Object> getRepliesByPostId(@PathVariable(name = "postId") Long postId)
            throws PostNotFoundException {

        List<Reply> replies = replyService.findByPostId(postId);
        return ResponseBuilder.buildResponse(replies, HttpStatus.OK, null);
    }

    @PatchMapping("/update")
    public ResponseEntity<Object> updateReply(@RequestBody ReplyModel replyModel)
            throws ReplyNotFoundException, EntityOwnerMismatchException {

        Reply reply = replyService.updateReply(replyModel);
        return ResponseBuilder.buildResponse(reply, HttpStatus.OK, "Reply updated successfully!");
    }

    @DeleteMapping("/delete/{replyId}")
    public ResponseEntity<Object> deleteReplyById(@PathVariable(name = "replyId") Long replyId)
            throws ReplyNotFoundException, EntityOwnerMismatchException {

        replyService.deleteById(replyId);
        return ResponseBuilder.buildResponse(null, HttpStatus.OK, "Reply deleted successfully!");
    }
}
