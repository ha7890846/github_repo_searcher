package com.ha7890846.repository_searcher.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Table(name = "github_repositories")
@Getter
@Setter
public class GitHubRepository {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String owner;

    private String language;

    private Integer stars;

    private Integer forks;

    private OffsetDateTime lastUpdated;
}
