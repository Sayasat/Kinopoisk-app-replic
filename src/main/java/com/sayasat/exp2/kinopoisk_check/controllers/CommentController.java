package com.sayasat.exp2.kinopoisk_check.controllers;

import com.sayasat.exp2.kinopoisk_check.dto.comments.CommentDTO;
import com.sayasat.exp2.kinopoisk_check.dto.movies.MovieResponse;
import com.sayasat.exp2.kinopoisk_check.services.CommentService;
import com.sayasat.exp2.kinopoisk_check.services.UserContextService;
import com.sayasat.exp2.kinopoisk_check.util.MovieErrorResponse;
import com.sayasat.exp2.kinopoisk_check.util.MovieException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserContextService userContextService;

    public CommentController(CommentService commentService, UserContextService userContextService) {
        this.commentService = commentService;
        this.userContextService = userContextService;
    }

    @GetMapping("{movie_id}/list")
    public MovieResponse getComments(@PathVariable Integer movie_id) {
        return new MovieResponse(commentService.getAllCommentsByMovieId(movie_id));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("{movie_id}/add")
    public ResponseEntity<HttpStatus> makeReview(@PathVariable int movie_id,
                                                 @RequestBody CommentDTO commentDTO) {
        String username = userContextService.getCurrentUsername();
        commentService.makeReview(movie_id, username, commentDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PatchMapping("{comment_id}/edit")
    public ResponseEntity<HttpStatus> editReview(@PathVariable int comment_id,
                                                 @RequestBody CommentDTO commentDTO) {
        String username = userContextService.getCurrentUsername();
        commentService.editReview(comment_id, username, commentDTO.getComment());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("{comment_id}/delete")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable int comment_id) {
        String username = userContextService.getCurrentUsername();
        commentService.deleteReview(comment_id, username);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("{comment_id}/like")
    public ResponseEntity<?> likeComment(@PathVariable int comment_id) {
        String username = userContextService.getCurrentUsername();
        try {
            commentService.likeComment(comment_id, username);
            return ResponseEntity.ok("Comment liked successfully");
        } catch (MovieException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("{comment_id}/dislike")
    public ResponseEntity<?> dislikeComment(@PathVariable int comment_id) {
        String username = userContextService.getCurrentUsername();
        try {
             commentService.dislikeComment(comment_id, username);
            return ResponseEntity.ok("Comment disliked successfully");
        } catch (MovieException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @ExceptionHandler
    private ResponseEntity<MovieErrorResponse> handleException(MovieException e) {
        MovieErrorResponse response = new MovieErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
