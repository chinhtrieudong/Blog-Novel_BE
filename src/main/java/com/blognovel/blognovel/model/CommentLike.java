package com.blognovel.blognovel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment_likes", uniqueConstraints = @UniqueConstraint(columnNames = { "comment_id", "user_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
