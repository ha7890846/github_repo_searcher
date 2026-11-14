
package com.ha7890846.repository_searcher.mapper;

import com.ha7890846.repository_searcher.dto.GitHubRepoDto;
import com.ha7890846.repository_searcher.dto.GitHubRepositoryResponseDto;
import com.ha7890846.repository_searcher.entity.GitHubRepository;
import org.springframework.stereotype.Component;

@Component
public class GitHubRepositoryMapper {

    public GitHubRepository toEntity(GitHubRepoDto dto) {
        GitHubRepository entity = new GitHubRepository();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        if (dto.getOwner() != null) {
            entity.setOwner(dto.getOwner().getLogin());
        }
        entity.setLanguage(dto.getLanguage());
        entity.setStars(dto.getStargazers_count());
        entity.setForks(dto.getForks_count());
        entity.setLastUpdated(dto.getUpdated_at());
        return entity;
    }

    public GitHubRepositoryResponseDto toResponseDto(GitHubRepository entity) {
        GitHubRepositoryResponseDto dto = new GitHubRepositoryResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setOwner(entity.getOwner());
        dto.setLanguage(entity.getLanguage());
        dto.setStars(entity.getStars());
        dto.setForks(entity.getForks());
        dto.setLastUpdated(entity.getLastUpdated());
        return dto;
    }
}
