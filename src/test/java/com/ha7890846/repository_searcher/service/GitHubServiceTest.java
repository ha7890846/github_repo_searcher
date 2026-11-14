
package com.ha7890846.repository_searcher.service;

import com.ha7890846.repository_searcher.dto.GitHubRepositoryResponseDto;
import com.ha7890846.repository_searcher.entity.GitHubRepository;
import com.ha7890846.repository_searcher.mapper.GitHubRepositoryMapper;
import com.ha7890846.repository_searcher.repository.GitHubRepositoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GitHubServiceTest {

    @Mock
    private GitHubRepositoryRepository repository;

    private GitHubRepositoryMapper mapper;
    private GitHubService githubService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mapper = new GitHubRepositoryMapper();

        githubService = new GitHubService(repository, mapper);
    }

    @Test
    void shouldReturnRepositoriesSortedByStarsDescending() {

        // Arrange
        GitHubRepository repo1 = new GitHubRepository();
        repo1.setId(1L);
        repo1.setName("Repo A");
        repo1.setStars(100);

        GitHubRepository repo2 = new GitHubRepository();
        repo2.setId(2L);
        repo2.setName("Repo B");
        repo2.setStars(500);

        when(repository.findAll())
                .thenReturn(List.of(repo1, repo2));

        // Act
        List<GitHubRepositoryResponseDto> result =
                githubService.getRepositories(null, null, "stars");

        // Assert
        assertEquals(2, result.size());
        assertEquals("Repo B", result.get(0).getName());
        assertEquals(500, result.get(0).getStars());
        assertEquals("Repo A", result.get(1).getName());

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldFilterRepositoriesByLanguage() {

        GitHubRepository repo = new GitHubRepository();
        repo.setId(1L);
        repo.setName("Spring Boot");
        repo.setLanguage("Java");
        repo.setStars(500);

        when(repository.findByLanguageIgnoreCase("Java"))
                .thenReturn(List.of(repo));

        List<GitHubRepositoryResponseDto> result =
                githubService.getRepositories("Java", null, "stars");

        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getLanguage());

        verify(repository).findByLanguageIgnoreCase("Java");
    }

    @Test
    void shouldFilterRepositoriesByMinimumStars() {

        GitHubRepository repo = new GitHubRepository();
        repo.setId(2L);
        repo.setName("Popular Repo");
        repo.setStars(1000);

        when(repository.findByStarsGreaterThanEqual(500))
                .thenReturn(List.of(repo));

        List<GitHubRepositoryResponseDto> result =
                githubService.getRepositories(null, 500, "stars");

        assertEquals(1, result.size());
        assertTrue(result.get(0).getStars() >= 500);

        verify(repository).findByStarsGreaterThanEqual(500);
    }

    @Test
    void shouldThrowExceptionForInvalidSort() {

        when(repository.findAll())
                .thenReturn(List.of());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> githubService.getRepositories(
                        null, null, "invalid")
        );

        assertEquals(
                "Invalid sort value. Use stars, forks, or updated.",
                exception.getMessage()
        );
    }

}
