
package com.ha7890846.repository_searcher.service;

import com.ha7890846.repository_searcher.dto.GitHubSearchRequest;
import com.ha7890846.repository_searcher.dto.GitHubSearchResponseDto;
import com.ha7890846.repository_searcher.dto.GitHubRepositoryResponseDto;
import com.ha7890846.repository_searcher.entity.GitHubRepository;
import com.ha7890846.repository_searcher.mapper.GitHubRepositoryMapper;
import com.ha7890846.repository_searcher.repository.GitHubRepositoryRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
public class GitHubService {

    private final GitHubRepositoryRepository repository;
    private final GitHubRepositoryMapper mapper;
    private final RestClient restClient;


    public GitHubService(
            GitHubRepositoryRepository repository,
            GitHubRepositoryMapper mapper) {

        this.repository = repository;
        this.mapper = mapper;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.github.com")
                .build();
    }

    public List<GitHubRepositoryResponseDto> searchRepositories(GitHubSearchRequest request) {
        String query = request.getQuery();
        if (request.getLanguage() != null
                && !request.getLanguage().isBlank()) {
            query += " language:" + request.getLanguage();
        }
        String sort = request.getSort();
        if (sort == null || sort.isBlank()) {
            sort = "stars";
        }
        final String searchQuery = query;
        final String sortBy = sort;
        GitHubSearchResponseDto response;
        try {
            response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/repositories")
                            .queryParam("q", searchQuery)
                            .queryParam("sort", sortBy)
                            .build())
                    .retrieve()
                    .body(GitHubSearchResponseDto.class);

        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().value() == 403 ||
                    ex.getStatusCode().value() == 429) {
                throw new ResponseStatusException(
                        HttpStatus.TOO_MANY_REQUESTS,
                        "GitHub API request was forbidden or rate limited",
                        ex
                );
            }
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "GitHub API rejected the request",
                    ex
            );
        } catch (HttpServerErrorException | ResourceAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "GitHub API is temporarily unavailable",
                    ex
            );
        }
        List<GitHubRepository> entities = response.getItems()
                .stream()
                .map(mapper::toEntity)
                .toList();

        List<GitHubRepository> savedRepositories =
                repository.saveAll(entities);

        return savedRepositories.stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    public List<GitHubRepositoryResponseDto> getRepositories(
            String language, Integer minStars, String sort) {
        List<GitHubRepository> repositories;
        if (language != null && !language.isBlank() && minStars != null) {
            repositories =
                    repository.findByLanguageIgnoreCaseAndStarsGreaterThanEqual(
                            language, minStars);
        } else if (language != null && !language.isBlank()) {
            repositories = repository.findByLanguageIgnoreCase(language);
        } else if (minStars != null) {
            repositories = repository.findByStarsGreaterThanEqual(minStars);
        } else {
            repositories = repository.findAll();
        }
        if (sort == null || sort.isBlank()) {
            sort = "stars";
        }
        Comparator<GitHubRepository> comparator;
        switch (sort.toLowerCase()) {
            case "forks":
                comparator = Comparator.comparing(
                        GitHubRepository::getForks,
                        Comparator.nullsLast(Comparator.reverseOrder()));
                break;
            case "updated":
                comparator = Comparator.comparing(
                        GitHubRepository::getLastUpdated,
                        Comparator.nullsLast(Comparator.reverseOrder()));
                break;
            case "stars":
                comparator = Comparator.comparing(
                        GitHubRepository::getStars,
                        Comparator.nullsLast(Comparator.reverseOrder()));
                break;
            default:
                throw new IllegalArgumentException(
                        "Invalid sort value. Use stars, forks, or updated.");
        }
        return repositories.stream()
                .sorted(comparator)
                .map(mapper::toResponseDto)
                .toList();
    }

}
