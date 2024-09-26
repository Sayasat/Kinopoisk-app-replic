package com.sayasat.exp2.kinopoisk_check.repositories;

import com.sayasat.exp2.kinopoisk_check.models.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, Integer> {
    Optional<CommentReaction> findByCommentIdAndUserId(int commentId, int userId);
}
