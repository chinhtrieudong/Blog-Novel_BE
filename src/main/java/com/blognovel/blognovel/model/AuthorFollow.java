package com.blognovel.blognovel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "author_follows", uniqueConstraints = @UniqueConstraint(columnNames = { "author_id", "user_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorFollow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
