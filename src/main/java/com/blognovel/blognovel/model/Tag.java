package com.blognovel.blognovel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String slug;

    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts;
}
