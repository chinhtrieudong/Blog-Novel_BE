package com.blognovel.blognovel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chapters")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chapter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    private Integer chapterNumber;

    @Builder.Default
    private Long viewCount = 0L;

    @ManyToOne
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;
}
