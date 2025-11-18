package com.jbme.sudoku_solver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SudokuController.class)
class SudokuControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SudokuSolver solver;  // Single mock bean matching your controller

    // Test data
    private final int[][] VALID_PUZZLE = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    private final int[][] SOLUTION_1 = {
        {5, 3, 4, 6, 7, 8, 9, 1, 2},
        {6, 7, 2, 1, 9, 5, 3, 4, 8},
        {1, 9, 8, 3, 4, 2, 5, 6, 7},
        {8, 5, 9, 7, 6, 1, 4, 2, 3},
        {4, 2, 6, 8, 5, 3, 7, 9, 1},
        {7, 1, 3, 9, 2, 4, 8, 5, 6},
        {9, 6, 1, 5, 3, 7, 2, 8, 4},
        {2, 8, 7, 4, 1, 9, 6, 3, 5},
        {3, 4, 5, 2, 8, 6, 1, 7, 9}
    };

    private final int[][] SOLUTION_2 = {
        {5, 3, 4, 6, 7, 8, 9, 1, 2},
        {6, 7, 2, 1, 9, 5, 3, 4, 8},
        {1, 9, 8, 3, 4, 2, 5, 6, 7},
        {8, 5, 9, 7, 6, 1, 4, 2, 3},
        {4, 2, 6, 8, 5, 3, 7, 9, 1},
        {7, 1, 3, 9, 2, 4, 8, 5, 6},
        {9, 6, 1, 5, 3, 7, 2, 8, 4},
        {2, 8, 7, 4, 1, 9, 6, 3, 5},
        {3, 4, 5, 2, 8, 6, 1, 7, 9}
    };

    private final int[][] DIFFERENT_SOLUTION_2 = {
        {5, 3, 4, 6, 7, 8, 9, 2, 1}, // Different from SOLUTION_1
        {6, 7, 2, 1, 9, 5, 3, 4, 8},
        {1, 9, 8, 3, 4, 2, 5, 6, 7},
        {8, 5, 9, 7, 6, 1, 4, 2, 3},
        {4, 2, 6, 8, 5, 3, 7, 9, 1},
        {7, 1, 3, 9, 2, 4, 8, 5, 6},
        {9, 6, 1, 5, 3, 7, 2, 8, 4},
        {2, 8, 7, 4, 1, 9, 6, 3, 5},
        {3, 4, 5, 2, 8, 6, 1, 7, 9}
    };

    @Test
    @DisplayName("Should solve puzzle and return identical solutions")
    void shouldSolvePuzzleAndReturnIdenticalSolutions() throws Exception {
        // Arrange
        when(solver.fillSudokuGrid(1)).thenReturn(SOLUTION_1);
        when(solver.fillSudokuGrid(2)).thenReturn(SOLUTION_2);
        String jsonContent = objectMapper.writeValueAsString(VALID_PUZZLE);

        // Act & Assert
        mockMvc.perform(post("/api/sudoku/solve")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.solution1").isArray())
                .andExpect(jsonPath("$.solution2").isArray())
                .andExpect(jsonPath("$.solution1.length()").value(9))
                .andExpect(jsonPath("$.solution2.length()").value(9))
                .andExpect(jsonPath("$.solution1[0][0]").value(5))
                .andExpect(jsonPath("$.solution2[0][0]").value(5))
                .andExpect(jsonPath("$.multiplesSolutions").value(false))
                .andExpect(jsonPath("$.noSolution").value(false));
    }

    @Test
    @DisplayName("Should detect multiple solutions when solutions differ")
    void shouldDetectMultipleSolutionsWhenSolutionsDiffer() throws Exception {
        // Arrange
        when(solver.fillSudokuGrid(1)).thenReturn(SOLUTION_1);
        when(solver.fillSudokuGrid(2)).thenReturn(DIFFERENT_SOLUTION_2);
        String jsonContent = objectMapper.writeValueAsString(VALID_PUZZLE);

        // Act & Assert
        mockMvc.perform(post("/api/sudoku/solve")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.solution1").isArray())
                .andExpect(jsonPath("$.solution2").isArray())
                .andExpect(jsonPath("$.multiplesSolutions").value(true))
                .andExpect(jsonPath("$.noSolution").value(false));
    }

    @Test
    @DisplayName("Should handle unsolvable puzzle")
    void shouldHandleUnsolvablePuzzle() throws Exception {
        // Arrange
        when(solver.fillSudokuGrid(1)).thenReturn(null);
        when(solver.fillSudokuGrid(2)).thenReturn(null);
        String jsonContent = objectMapper.writeValueAsString(VALID_PUZZLE);

        // Act & Assert
        mockMvc.perform(post("/api/sudoku/solve")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.solution1").value((Object) null))
                .andExpect(jsonPath("$.solution2").value((Object) null))
                .andExpect(jsonPath("$.multiplesSolutions").value(false))
                .andExpect(jsonPath("$.noSolution").value(true));
    }

    @Test
    @DisplayName("Should handle partially solvable puzzle")
    void shouldHandlePartiallySolvablePuzzle() throws Exception {
        // Arrange - only first solution found
        when(solver.fillSudokuGrid(1)).thenReturn(SOLUTION_1);
        when(solver.fillSudokuGrid(2)).thenReturn(null);
        String jsonContent = objectMapper.writeValueAsString(VALID_PUZZLE);

        // Act & Assert
        mockMvc.perform(post("/api/sudoku/solve")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.solution1").isArray())
                .andExpect(jsonPath("$.solution1.length()").value(9))
                .andExpect(jsonPath("$.solution2").value((Object) null))
                .andExpect(jsonPath("$.multiplesSolutions").value(true))  // Different because one is null
                .andExpect(jsonPath("$.noSolution").value(false));
    }

    @Test
    @DisplayName("Should return health check status")
    void shouldReturnHealthCheckStatus() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/sudoku/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Sudoku Solver API is running"));
    }

    @Test
    @DisplayName("Should handle malformed JSON")
    void shouldHandleMalformedJson() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/sudoku/solve")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle empty request body")
    void shouldHandleEmptyRequestBody() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/sudoku/solve")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle null grid")
    void shouldHandleNullGrid() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/sudoku/solve")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return proper JSON structure")
    void shouldReturnProperJsonStructure() throws Exception {
        // Arrange
        when(solver.fillSudokuGrid(1)).thenReturn(SOLUTION_1);
        when(solver.fillSudokuGrid(2)).thenReturn(SOLUTION_2);
        String jsonContent = objectMapper.writeValueAsString(VALID_PUZZLE);

        // Act & Assert
        mockMvc.perform(post("/api/sudoku/solve")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.solution1").exists())
                .andExpect(jsonPath("$.solution2").exists())
                .andExpect(jsonPath("$.multiplesSolutions").exists())
                .andExpect(jsonPath("$.noSolution").exists());
    }
}