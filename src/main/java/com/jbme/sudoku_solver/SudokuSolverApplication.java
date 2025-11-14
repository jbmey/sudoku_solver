package com.jbme.sudoku_solver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.FileNotFoundException;
import java.util.Arrays;


/**
* Launcher that invokes the other classes of the program: {@link Grille} and
 * {@link TableauSudoku}. This launcher will load a Sudoku grid in text format
 * located at the path provided as a command-line argument. That grid will be
 * transcribed into an array. Finally, the array will be filled.
 * The operation is performed twice using a variation of the algorithm. If
 * the two results differ, the program will display both possible solutions.
 * Otherwise, the single result is displayed with the message
 * "!!!No other solutions possible!!!".
 * 
 * @author Jean-Baptiste MEYRIEUX
 *
 */

@SpringBootApplication
public class SudokuSolverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SudokuSolverApplication.class, args);
	}

}
