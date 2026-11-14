
package com.ha7890846.repository_searcher.repository;

import com.ha7890846.repository_searcher.entity.GitHubRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GitHubRepositoryRepository
        extends JpaRepository<GitHubRepository, Long> {

    List<GitHubRepository> findByLanguageIgnoreCase(String language);

    List<GitHubRepository> findByStarsGreaterThanEqual(Integer minStars);

    List<GitHubRepository> findByLanguageIgnoreCaseAndStarsGreaterThanEqual(
            String language, Integer minStars);
}
