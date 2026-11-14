package com.ha7890846.repository_searcher.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class GitHubRepositoryResponseDto {

    private Long id;
    private String name;
    private String description;
    private String owner;
    private String language;
    private Integer stars;
    private Integer forks;
    private OffsetDateTime lastUpdated;
}
