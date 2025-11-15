package com.jbme.sudoku_solver;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 This class allows the solving of a Sudoku board. Two different solving algorithms are available (a search by incrementing the value in the cells to be filled, or a search by decrementing the value in the cells to be filled) in order to check whether the grid has multiple possible solutions.
 * 
 * @author Jean-Baptiste MEYRIEUX
 *
 */


public class SudokuSolver {

    /**
	 * Logger creation
	 */

	 private static final Logger log = LoggerFactory.getLogger(SudokuSolver.class);

    /**
	 * The array with the sudoku grid to complete.
	 *
	 * <ul>
	 * <li>1er dimension = row
	 * <li>2e dimension = column
	 * </ul>
	 */
	private int[][] sudokuGrid;

    /**
	 * An array containin the coordinate of the cells to fill, order by priority.
	 */
	private ArrayList<int[]> cellsToSolve = new ArrayList<int[]>();

	/**
	 * "getters" for the sudoku grid.
	 * 
	 * @return sudokuGrid The Sudoku Grid
	 */
	public int[][] getGrid() {
		return sudokuGrid;
	}

	/**
	 * "setters" to initialize the sudoku grid. 
	 * 
	 * @param tableau Sudoku Grid to fill.
	 */
	public void setGrid(int[][] sudokuGrid) {
		this.sudokuGrid = sudokuGrid;

	}

	/**
	 * Main methods of the class, which fill the sudoku grid.
	 * 
	 * @param direction Parameter used to define the search direction for allowed values in the cells to be filled.
	 * 
	 * <ul>
	 * <li>1: ascending (incrementing the value in the target cell)
	 * <li>2: descending (decrementing the value in the target cell)
	 * </ul>
	 * 
	 * @return The filled sudoku grid or "null".
	 */
	public int[][] fillSudokuGrid(int direction) {


		getCellsToSolve();

		showOrder();// optionnel - - only actif when log4j is on DEBUG

		// scan the list and search for the value for each cell in this
        // list, according to the chosen fill direction
		if (scanList(direction) == true) {

			return sudokuGrid;

		} else {
			return null;
		}

	}

	/**
     * This method adds all the cells to be filled in to a list
     * sorted by priority. The “cells” are in the form of an array (of
     * size 3). Index [0] corresponds to the priority, index [1] corresponds to the
     * x-coordinate, and index [2] corresponds to the y-coordinate.
     */
	public void getCellsToSolve() {
		/**
		 * x = row
		 */
		int x = 0;
		/**
		 * y = column
		 */
		int y = 0;
		for (x = 0; x < 9; x++) {//horizontal and vertical scan
			for (y = 0; y < 9; y++) {

				if (sudokuGrid[x][y] == 0) {// if the value of the cell is 0, it is a cell to fill
											

					int priorityValue = getPriorityValue(x, y); 

					int[] cellToAddToPriorityList = { priorityValue, x, y };// combine priority value and coordinate
																					
					addToSortedList(cellToAddToPriorityList);   // Call the method to add this cell to the right
                                                                // place in our list of cells to be filled in
                                                                // according to priority.

				}
			}
		}

	}

	/**
	 * Method that returns the priority value of the cells to be filled in at
     * coordinates x and y. The index is the sum of all cells already
     * predefined in the same row and column as the target cell. The higher this
     * number, the higher the priority of the cell..
	 * 
	 * @param x x coordinate of the sudoku.
	 * @param y y coordinate of the sudoku.
	 * @return countNb Number of cells predefined in row and column of target = priority value.
	 */
	public int getPriorityValue(int x, int y) {
		int countNb = 0;

		// Count of the already filled cells in row and column of our Target
		for (int i = 0; i < 9; i++) {
			if (sudokuGrid[x][i] != 0 && i != y) {
				countNb++;
			}
		}
		for (int i = 0; i < 9; i++) {
			if (sudokuGrid[i][y] != 0 && i != x) {
				countNb++;
			}
		}
		return countNb;
	}

	/**
	 * This method adds the cells to be filled in to a list, in order
     * according to their priority value (from the highest index to the
     * lowest).
	 * 
	 * @param cellToAddToPriorityList array representing the cell. It contains:
	 * 
	 *                                       <ul>
	 *                                       <li>[0] priority value
	 *                                       <li>[1] x coordinate
	 *                                       <li>[2] y coordinate
	 *                                       </ul>
	 * 
	 */
	public void addToSortedList(int[] cellToAddToPriorityList) {

		boolean cellWasAdded = false;

		//sweep the list and compare priority value to insert the cell in the right place
		for (int i = 0; i < cellsToSolve.size(); i++) {
			if (cellToAddToPriorityList[0] > cellsToSolve.get(i)[0]) {
				cellsToSolve.add(i, cellToAddToPriorityList);
				cellWasAdded = true;
				break;

			}
		}
		
        //if priority smaller than all other cells, it is added to the end
		if (cellWasAdded == false) {
			cellsToSolve.add(cellToAddToPriorityList);

		}
	}

	/**
	 * Method used only to display our list of cells to solve in order of priority.
	 * Used only for debugging.
	 */
	public void showOrder() {
		log.debug("Display list of cells to solve sorted by priority value" + "\n");
		for (int i = 0; i < cellsToSolve.size(); i++) {

			for (int j = 0; j < 3; j++) {
				switch (j) {
				case 0:
					log.debug("priority: " + cellsToSolve.get(i)[j]);
					break;

				case 1:
					log.debug("x: " + cellsToSolve.get(i)[j]);
					break;

				case 2:
					log.debug("y: " + cellsToSolve.get(i)[j] + "\n");
				}
			}
		}
	}

	/**
	 * Method that scans the list of cells which need to be solved in order of
     * priority and calls a method to search for an authorized value for
     * each cell. The method called depends on the “direction” parameter.
	 * 
	 * @param direction Depending on the scenario chosen, the method will launch a different algorithm
     *            to search for the value of the cell.
	 * 
	 *            <ul>
     *            <li>1: “ascending” search by incrementing the value in
     *            the target cell
     *            <li>2: “descending” search by decrementing the value
     *            in the target cell
     *            </ul>
	 * 
	 * @return solutionFound Boolean to indicate whether a solution has been found
     *         and all cells are filled.
	 */
	public boolean scanList(int direction) {

		boolean solutionFound = true;

		switch (direction) {

		case 1:
			int i = 0;
			while (i < cellsToSolve.size() && i >= 0) {

				int x = cellsToSolve.get(i)[1];// get row coordinate
				int y = cellsToSolve.get(i)[2];// get column coordinate

				if (searchCellValueAscending(x, y) == true) {//call method to search cell value
					i++;// if a value is found we continue
				} else {
					i--;// else we go back to previous cell
				}

			}
			if (i != cellsToSolve.size()) {// check if grid is complete

				solutionFound = false;
			}
			break;

		case 2:
			int j = 0;
			while (j < cellsToSolve.size() && j >= 0) {

				int x = cellsToSolve.get(j)[1];// get row coordinate
				int y = cellsToSolve.get(j)[2];// get column coordinate

				if (searchCellValueDescending(x, y) == true) {//call method to search cell value
					j++;// if a value is found we continue
				} else {
					j--;// else we go back to previous cell
				}

			}
			if (j != cellsToSolve.size()) {// check if grid is complete

				solutionFound = false;
			}
			break;

		}

		return solutionFound;
	}

	/**
	 * Method that searches for an authorized value for a cell to be filled in. This
     * “ascending” algorithm works by incrementing the value in the cell and
     * comparing it with all the other cells in the
     * same row and column.
     * 
	 * @param x x coordinate of sudoku grid
	 * @param y y coordinate of sudoku grid
	 * @return cellSolved Boolean showing if a value was found for the cell
	 */
	public boolean searchCellValueAscending(int x, int y) {
		boolean cellSolved;

		do {
			sudokuGrid[x][y]++;// incrementing current cell value

			log.trace("coordinate x:" + x + " y:" + y + " " + "value=" + sudokuGrid[x][y]);

		} while ((compareValueWithRow(sudokuGrid[x][y], x, y) || scanColonne(sudokuGrid[x][y], x, y)) && sudokuGrid[x][y] <= 9);
		// as long as there is at least one cell with the same value in the row or column,
        // restart the loop to increment the value in our cell
        // if the value of our cell is not in the row, we can move on to the next one
        // if the value of our current cell is at its maximum (9), we exit the loop

		if (sudokuGrid[x][y] > 9) {
			// If the value of this cell is greater than 9 (not possible), and no combination works,
            // we will try to increment the previous cell in the priority list
            // that allows it.
            // To do this, we return a Boolean: false
            // and reset the value of the cell to zero.
			sudokuGrid[x][y] = 0;

			log.trace("No solution found. return to the previous cell in the priority order." + "\n");
			cellSolved = false;

		} else {
			log.trace("A value is found. Go to the next cell in the priority order." + "\n");
			cellSolved = true;
		}

		return cellSolved;

	}

	/**
	 * Method that searches for an authorized value for a cell to be filled in. This
     * "descending" algorithm works by decrementing the value in the cell and
     * comparing it with all the other cells in the
     * same row and column.
	 * 
	* @param x x coordinate of sudoku grid
	 * @param y y coordinate of sudoku grid
	 * @return cellSolved Boolean showing if a value was found for the cell
	 */

	public boolean searchCellValueDescending(int x, int y) {
		boolean cellSolved;

		if (sudokuGrid[x][y] == 0) {
			sudokuGrid[x][y] = 10;
			// if the value is at zero, reset the value to 10, so that the first value to be
            // compared is 9
		}

		do {
			log.trace("coordinate x:" + x + " y:" + y + " " + "value=" + sudokuGrid[x][y]);

			sudokuGrid[x][y]--;
		} while ((compareValueWithRow(sudokuGrid[x][y], x, y) || scanColonne(sudokuGrid[x][y], x, y)) && sudokuGrid[x][y] >= 1);
		// as long as there is at least one cell with the same value in the row or column,
        // restart the loop to decrement the value in our cell
        // if the value of the cell is not in the row, we can move on to the next one
        // if the value of our current cell is at its maximum (9), we exit the loop

		if (sudokuGrid[x][y] == 0) {
			// If the value of this cell is smaller than 1 (not possible), and no combination works,
            // we will try to decrement the previous cell in the  priority list
            // that allows it.
            // To do this, we return a Boolean: false
 

			log.trace("No solution found. return to the previous cell in the priority order." + "\n");
			cellSolved = false;

		} else {
			log.trace("A value is found. Go to the next cell in the priority order." + "\n");
			cellSolved = true;
		}

		return cellSolved;

	}

	/**
	 * Method that compares a value of a cell with those contained in the rest of the row
     * in order to determine whether this value is alread in use.
	 * 
	 * @param valueToCompare The Value to compare with the rest of the row
	 * @param x               x coordinate of the sudoku grid
	 * @param y               y coordinate of the sudoku grid
	 * @return valueInRow Boolean true if the value is already in the row
	 */
	public boolean compareValueWithRow(int valueToCompare, int x, int y) {

		boolean valueInRow = false;
		for (int i = 0; i < 9; i++) {
			if (i == y) {
				continue;// No comparing with the cell itself
			} else if (sudokuGrid[x][i] == valueToCompare) {
				valueInRow = true;

			} else {
				continue;
			}
		}
		return valueInRow;
	}

	/**
	 * Method that compares a value of a cell with those contained in the rest of the row
     * in order to determine whether this value is alread in use.
	 * 
	 * @param valueToCompare The Value to compare with the rest of the column
	 *                        
	 * @param x               x coordinate of the sudoku grid.
	 * @param y               y coordinate of the sudoku grid.
	 * @return valueInColumn BBoolean true if the value is already in the row
	 */
	public boolean scanColonne(int valueToCompare, int x, int y) {

		boolean valueInColumn = false;
		for (int i = 0; i < 9; i++) {
			if (i == x) {
				continue;
			} else if (sudokuGrid[i][y] == valueToCompare) {
				valueInColumn = true;

			} else {
				continue;
			}
		}
		return valueInColumn;
	}

	

}


