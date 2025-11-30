package com.blognovel.blognovel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "novel_saves", uniqueConstraints = @UniqueConstraint(columnNames = { "novel_id", "user_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NovelSave extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer readChapters;

    private String savedAt;

    private String lastRead;
}
