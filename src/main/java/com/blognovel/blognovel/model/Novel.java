package com.blognovel.blognovel.model;

import com.blognovel.blognovel.enums.NovelStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "novels")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Novel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(unique = true)
    private String slug;

    private String description;

    private String coverImage;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Enumerated(EnumType.STRING)
    private NovelStatus status = NovelStatus.ONGOING;

    private Long viewCount = 0L;

    private Float avgRating = 0F;

    @ManyToMany
    @JoinTable(name = "novel_genres",
            joinColumns = @JoinColumn(name = "novel_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<Chapter> chapters;

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
