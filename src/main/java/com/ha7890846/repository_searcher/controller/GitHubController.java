package com.ha7890846.repository_searcher.controller;

import com.ha7890846.repository_searcher.dto.GitHubSearchRequest;
import com.ha7890846.repository_searcher.dto.GitHubRepositoryResponseDto;
import com.ha7890846.repository_searcher.dto.GitHubSearchResultDto;
import com.ha7890846.repository_searcher.service.GitHubService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubService githubService;

    public GitHubController(GitHubService githubService) {
        this.githubService = githubService;
    }



    @GetMapping("/repositories")
    public ResponseEntity<List<GitHubRepositoryResponseDto>> getRepositories(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(required = false) String sort)
    {
        List<GitHubRepositoryResponseDto> repositories = githubService.getRepositories(language, minStars, sort);
        return ResponseEntity.ok(repositories);
    }


    @PostMapping("/search")
    public ResponseEntity<GitHubSearchResultDto> searchRepositories(
            @Valid @RequestBody GitHubSearchRequest request)
    {
        List<GitHubRepositoryResponseDto> repositories = githubService.searchRepositories(request);
        GitHubSearchResultDto response = new GitHubSearchResultDto("Repositories fetched and saved successfully", repositories );
        return ResponseEntity.ok(response);
    }

}
