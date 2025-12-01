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

    @Column(name = "read_chapters")
    private Integer readChapters;

    @Column(name = "saved_at")
    private String savedAt;

    @Column(name = "last_read")
    private String lastRead;
}
