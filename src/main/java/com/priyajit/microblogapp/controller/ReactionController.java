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

import com.priyajit.microblogapp.dto.ReactionModel;
import com.priyajit.microblogapp.entity.Reaction;
import com.priyajit.microblogapp.exception.DBException;
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
    public Reaction saveReaction(@RequestBody ReactionModel reactionModel) {
        try {
            return reactionService.save(reactionModel);
        } catch (UserNotFoundException | PostNotFoundException | ReplyNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DBException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/byId/{reactionId}")
    public Reaction getReactionById(@PathVariable(name = "reactionId") Long reactionId) {
        try {
            return reactionService.findById(reactionId);
        } catch (ReactionNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/byPostId/{postId}")
    public List<Reaction> getReactionByPostId(@PathVariable(name = "postId") Long postId) {
        try {
            return reactionService.findByPostId(postId);
        } catch (PostNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/update")
    public Reaction updateReaction(@RequestBody ReactionModel reactionModel) {
        try {
            return reactionService.updateReaction(reactionModel);
        } catch (ReactionNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DBException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityOwnerMismatchException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{reactionId}")
    public void deleteReactionById(@PathVariable Long reactionId) {
        try {
            reactionService.deleteById(reactionId);
        } catch (ReactionNotFoundException e) {
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
