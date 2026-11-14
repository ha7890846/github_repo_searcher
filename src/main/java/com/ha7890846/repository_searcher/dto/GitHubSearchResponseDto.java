package com.ha7890846.repository_searcher.dto;

import lombok.Data;
import java.util.List;

@Data
public class GitHubSearchResponseDto {

    private List<GitHubRepoDto> items;
}
