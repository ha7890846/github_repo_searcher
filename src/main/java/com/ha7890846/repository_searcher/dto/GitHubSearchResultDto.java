
package com.ha7890846.repository_searcher.dto;

import java.util.List;

public record GitHubSearchResultDto(
        String message,
        List<GitHubRepositoryResponseDto> repositories
) {
}
