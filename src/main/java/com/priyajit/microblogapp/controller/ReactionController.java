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

import com.priyajit.microblogapp.dto.ReactionModel;
import com.priyajit.microblogapp.entity.Reaction;
import com.priyajit.microblogapp.exception.EntityOwnerMismatchException;
import com.priyajit.microblogapp.exception.PostNotFoundException;
import com.priyajit.microblogapp.exception.ReactionNotFoundException;
import com.priyajit.microblogapp.exception.ReplyNotFoundException;
import com.priyajit.microblogapp.exception.UserNotFoundException;
import com.priyajit.microblogapp.service.reaction.ReactionService;

@RestController
@RequestMapping("/rest/reaction/")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @PostMapping("/save")
    public ResponseEntity<Object> saveReaction(@RequestBody ReactionModel reactionModel)
            throws UserNotFoundException, PostNotFoundException, ReplyNotFoundException {

        Reaction reaction = reactionService.save(reactionModel);
        return ResponseBuilder.buildResponse(reaction, HttpStatus.CREATED, "Reaction saved successfully!");

    }

    @GetMapping("/byId/{reactionId}")
    public ResponseEntity<Object> getReactionById(@PathVariable(name = "reactionId") Long reactionId)
            throws ReactionNotFoundException {

        Reaction reaction = reactionService.findById(reactionId);
        return ResponseBuilder.buildResponse(reaction, HttpStatus.OK, null);
    }

    @GetMapping("/byPostId/{postId}")
    public ResponseEntity<Object> getReactionByPostId(@PathVariable(name = "postId") Long postId)
            throws PostNotFoundException {

        List<Reaction> reactions = reactionService.findByPostId(postId);
        return ResponseBuilder.buildResponse(reactions, HttpStatus.OK, null);
    }

    @PatchMapping("/update")
    public ResponseEntity<Object> updateReaction(@RequestBody ReactionModel reactionModel)
            throws ReactionNotFoundException, EntityOwnerMismatchException {

        Reaction reaction = reactionService.updateReaction(reactionModel);
        return ResponseBuilder.buildResponse(reaction, HttpStatus.OK, "Reaction updated successfully!");
    }

    @DeleteMapping("/delete/{reactionId}")
    public ResponseEntity<Object> deleteReactionById(@PathVariable Long reactionId)
            throws ReactionNotFoundException, EntityOwnerMismatchException {

        reactionService.deleteById(reactionId);
        return ResponseBuilder.buildResponse(null, HttpStatus.OK, "Reaction deleted successfully!");

    }
}
