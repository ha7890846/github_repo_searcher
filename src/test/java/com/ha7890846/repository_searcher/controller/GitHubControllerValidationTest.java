
package com.ha7890846.repository_searcher.controller;

import com.ha7890846.repository_searcher.exception.GlobalExceptionHandler;
import com.ha7890846.repository_searcher.service.GitHubService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GitHubControllerValidationTest {

    private MockMvc mockMvc;
    private GitHubService githubService;

    @BeforeEach
    void setUp() {
        githubService = mock(GitHubService.class);

        GitHubController controller =
                new GitHubController(githubService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldRejectEmptySearchQuery() throws Exception {

        String requestBody = """
                {
                    "query": "",
                    "language": "Java",
                    "sort": "stars"
                }
                """;

        mockMvc.perform(post("/api/github/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Search query is required"));

        verifyNoInteractions(githubService);
    }
}
