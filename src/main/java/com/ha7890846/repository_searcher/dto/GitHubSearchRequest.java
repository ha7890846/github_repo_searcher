
package com.ha7890846.repository_searcher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class GitHubSearchRequest {
    @NotBlank(message = "Search query is required")
    private String query;
    private String language;

    @Pattern(
            regexp = "^(?i:stars|forks|updated)$",
            message = "Sort must be stars, forks, or updated"
    )
    private String sort;
}
