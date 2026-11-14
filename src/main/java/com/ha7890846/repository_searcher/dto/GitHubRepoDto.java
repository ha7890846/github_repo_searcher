package com.ha7890846.repository_searcher.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRepoDto {

    private Long id;
    private String name;
    private String description;
    private OwnerDto owner;
    private String language;

    private int stargazers_count;
    private int forks_count;
    private OffsetDateTime updated_at;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OwnerDto {
        private String login;
    }
}
