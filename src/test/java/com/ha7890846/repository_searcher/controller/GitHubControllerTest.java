
package com.ha7890846.repository_searcher.controller;

import com.ha7890846.repository_searcher.dto.GitHubRepositoryResponseDto;
import com.ha7890846.repository_searcher.dto.GitHubSearchRequest;
import com.ha7890846.repository_searcher.dto.GitHubSearchResultDto;
import com.ha7890846.repository_searcher.service.GitHubService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubControllerTest {

    @Mock
    private GitHubService githubService;

    @InjectMocks
    private GitHubController githubController;

    @Test
    void shouldReturnSuccessfulSearchResponse() {

        // Arrange
        GitHubSearchRequest request = new GitHubSearchRequest();
        request.setQuery("spring boot");
        request.setLanguage("Java");
        request.setSort("stars");

        GitHubRepositoryResponseDto repo =
                new GitHubRepositoryResponseDto();

        repo.setId(123L);
        repo.setName("spring-boot-example");
        repo.setLanguage("Java");
        repo.setStars(500);

        when(githubService.searchRepositories(request))
                .thenReturn(List.of(repo));

        // Act
        ResponseEntity<GitHubSearchResultDto> response =
                githubController.searchRepositories(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        GitHubSearchResultDto body = response.getBody();
        assertNotNull(body);

        assertEquals(
                "Repositories fetched and saved successfully",
                body.message()
        );

        assertEquals(1, body.repositories().size());
        assertEquals(
                "spring-boot-example",
                body.repositories().get(0).getName()
        );

        verify(githubService).searchRepositories(request);
    }
}
