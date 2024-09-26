package com.sayasat.exp2.kinopoisk_check.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "commentreaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false)
    private ReactionType reactionType;

    public enum ReactionType {
        LIKE, DISLIKE
    }

    public CommentReaction(Comment comment, User user, ReactionType reactionType) {
        this.comment = comment;
        this.user = user;
        this.reactionType = reactionType;
    }
}
