package com.sayasat.exp2.kinopoisk_check.services;

import com.sayasat.exp2.kinopoisk_check.dto.comments.CommentDTO;
import com.sayasat.exp2.kinopoisk_check.dto.comments.CommentInfoDTO;
import com.sayasat.exp2.kinopoisk_check.models.Comment;
import com.sayasat.exp2.kinopoisk_check.models.CommentReaction;
import com.sayasat.exp2.kinopoisk_check.models.Movie;
import com.sayasat.exp2.kinopoisk_check.models.User;
import com.sayasat.exp2.kinopoisk_check.repositories.CommentReactionRepository;
import com.sayasat.exp2.kinopoisk_check.repositories.CommentRepository;
import com.sayasat.exp2.kinopoisk_check.repositories.MovieRepository;
import com.sayasat.exp2.kinopoisk_check.repositories.UserRepository;
import com.sayasat.exp2.kinopoisk_check.util.MovieException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CommentReactionRepository commentReactionRepository;

    public CommentService(CommentRepository commentRepository, MovieRepository movieRepository, UserRepository userRepository, ModelMapper modelMapper, CommentReactionRepository commentReactionRepository) {
        this.commentRepository = commentRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.commentReactionRepository = commentReactionRepository;
    }

    public List<CommentInfoDTO> getAllCommentsByMovieId(int movieId) {
        return commentRepository.findAllByMovieIdOrderByIdDesc(movieId).stream()
                .map(this::convertToCommentInfoDTO).collect(Collectors.toList());
    }

    @Transactional
    public void makeReview(int movieId, String username, CommentDTO commentDTO) {
        Comment comment = convertToComment(commentDTO);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        comment.setUser(user);
        comment.setMovie(movie);
        comment.setCommentDate(LocalDateTime.now());
        comment.setLikes(0);    // начальное значение для лайков
        comment.setDislikes(0); // начальное значение для дизлайков

        commentRepository.save(comment);
    }

    @Transactional
    public void editReview(int commentId, String username, String newCommentText) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new MovieException("Comment not found with id " + commentId));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new MovieException("You are not authorized to edit this comment.");
        }

        comment.setComment(newCommentText);
        comment.setCommentDate(LocalDateTime.now());

        commentRepository.save(comment);
    }

    @Transactional
    public void deleteReview(int commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new MovieException("Comment not found with id " + commentId));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new MovieException("You are not authorized to delete this comment.");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public void likeComment(int commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new MovieException("Comment not found with id " + commentId));

        User user = getUserByUsername(username);
        Optional<CommentReaction> existingReaction = commentReactionRepository.findByCommentIdAndUserId(commentId, user.getId());

        if (existingReaction.isPresent()) {
            CommentReaction reaction = existingReaction.get();
            if (reaction.getReactionType() == CommentReaction.ReactionType.LIKE) {
                throw new MovieException("You have already liked this comment.");
            } else if (reaction.getReactionType() == CommentReaction.ReactionType.DISLIKE) {
                comment.setDislikes(comment.getDislikes() - 1);
                reaction.setReactionType(CommentReaction.ReactionType.LIKE);
                commentReactionRepository.save(reaction);
                comment.setLikes(comment.getLikes() + 1);
                commentRepository.save(comment);
//                commentReactionRepository.save(reaction);
//                commentReactionRepository.delete(reaction);
            }
        } else {
            CommentReaction commentReaction = new CommentReaction();
            commentReaction.setComment(comment);
            commentReaction.setUser(user);
            commentReaction.setReactionType(CommentReaction.ReactionType.LIKE);
            commentReactionRepository.save(commentReaction);
            comment.setLikes(comment.getLikes() + 1);
            commentRepository.save(comment);
        }
    }

    @Transactional
    public void dislikeComment(int commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new MovieException("Comment not found with id " + commentId));

        User user = getUserByUsername(username);
        Optional<CommentReaction> existingReaction = commentReactionRepository.findByCommentIdAndUserId(commentId, user.getId());

        if (existingReaction.isPresent()) {
            CommentReaction reaction = existingReaction.get();
            if (reaction.getReactionType() == CommentReaction.ReactionType.DISLIKE) {
                throw new MovieException("You have already disliked this comment.");
            } else if (reaction.getReactionType() == CommentReaction.ReactionType.LIKE) {
                // Изменение реакции с лайка на дислайк
                comment.setLikes(comment.getLikes() - 1);
                comment.setDislikes(comment.getDislikes() + 1);
                reaction.setReactionType(CommentReaction.ReactionType.DISLIKE);
                commentReactionRepository.save(reaction);
                commentRepository.save(comment);
            }
        } else {
            // Добавление нового дислайка
            CommentReaction commentReaction = new CommentReaction();
            commentReaction.setComment(comment);
            commentReaction.setUser(user);
            commentReaction.setReactionType(CommentReaction.ReactionType.DISLIKE);
            commentReactionRepository.save(commentReaction);
            comment.setDislikes(comment.getDislikes() + 1);
            commentRepository.save(comment);
        }
    }




    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new MovieException("User not found with username " + username));
    }


    public Comment convertToComment(CommentDTO commentDTO) {
        return modelMapper.map(commentDTO, Comment.class);
    }

    public CommentInfoDTO convertToCommentInfoDTO(Comment comment) {
        return modelMapper.map(comment, CommentInfoDTO.class);
    }

}
