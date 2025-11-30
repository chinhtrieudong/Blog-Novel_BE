package com.blognovel.blognovel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "novel_related", uniqueConstraints = @UniqueConstraint(columnNames = { "novel_id", "related_novel_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatedNovel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    @ManyToOne
    @JoinColumn(name = "related_novel_id", nullable = false)
    private Novel relatedNovel;
}
