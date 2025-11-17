console.log("sudoku.js loaded successfully");

const container = document.querySelector("#container");
const solutionContainer = document.querySelector(".solution");
solutionContainer.style.display = "none";
const gridToSolve = document.querySelector("#gridToSolve");
const gridSolution = document.querySelector("#gridSolution");
const noSolutionText = document.querySelector("#noSolutionText");
const validate = document.querySelector("#validate");
validate.addEventListener(`click`, () => solveGrid());
const otherSolution = document.querySelector("#moreSolutions");
const clear = document.querySelector("#clear");
clear.addEventListener(`click`, () => clearGrid());




setBlankGrid();

const cellInputs = document.querySelectorAll('.cell');
cellInputs.forEach((cellInput) =>
    cellInput.addEventListener('input', function () {
        const min = parseInt(this.min);
        const max = parseInt(this.max);
        let value = parseInt(this.value);

        if (value < min) this.value = "";
        if (value > max) this.value = max;
        if (isNaN(value)) this.value = "";
    }
    )
);

function initDisplay() {
    solutionContainer.style.display = "none";
    noSolutionText.style.display = "none";
}

function solveGrid() {
    initDisplay();
    let row = []
    let grid = [];
    for (let i = 0; i < 9; i++) {
        row = []
        for (let j = 0; j < 9; j++) {
            let value = parseInt(document.getElementById(`x${j} y${i}`).value);
            if (isNaN(value)) {
                value = 0;
            }
            row.push(value);
        }
        grid.push(row);

    }
    sendSolveRequest(grid);
}


function setBlankGrid() {
    for (let i = 0; i < 9; i++) {
        for (let j = 0; j < 9; j++) {
            let cell = document.createElement("input");
            cell.setAttribute("type", "number");
            cell.setAttribute("min", "0");
            cell.setAttribute("max", "9");
            cell.setAttribute("id", `x${j} y${i}`);
            cell.setAttribute("class", "cell");
            gridToSolve.appendChild(cell);

        }

    }

}

function clearGrid() {
    for (const child of gridToSolve.children) {
        child.value = "";
    }
    for (const child of gridSolution.children) {
        child.value = "";
    }
    initDisplay();
}





async function sendSolveRequest(grid) {
    try {
        const res = await fetch("/api/sudoku/solve", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(grid)
        });

        if (!res.ok) {
            throw new Error(`Server error: ${res.status} ${res.statusText}`);
        }

        const json = await res.json();
        console.log("Solve response:", json);
        processResponse(json);
        return json;
    } catch (err) {
        console.error("Failed to send solve request:", err);
        // show error to user or rethrow
        throw err;
    }

}

function processResponse(json) {
    result1 = json.solution1;
    result2 = json.solution2;
    multipleSolution = json.multiplesSolutions;
    noSolution = json.noSolution;
    solutionContainer.style.display = "flex";
    if (noSolution) {
        displayNoSolution();

    } else {
        displayResult(result1);
        if (!multipleSolution) {
            otherSolution.disabled = true;
        } else {
            otherSolution.addEventListener(`click`, () => displayResult(result2));
        }
    }

}

function displayResult(solution) {
    empty(gridSolution);
    for (const child of gridSolution.children) {
        child.value = "";
    }
    solutionContainer.style.display = "flex";
    empty(gridSolution);
    for (let i = 0; i < 9; i++) {
        for (let j = 0; j < 9; j++) {
            let cell = document.createElement("span");
            let cellContent = document.createTextNode(`${solution[i][j]}`);
            cell.appendChild(cellContent);
            cell.setAttribute("class", "solutionCell");
            gridSolution.append(cell);
        }

    }
}

function displayNoSolution() {
    solutionContainer.style.display = "flex";
    noSolutionText.style.display = "block";
    otherSolution.style.display = "none";
}

function empty(element) {
    for (const child of element.children) {
        child.value = "";
    }
    while (element.firstElementChild) {
        element.firstElementChild.remove();
    }


}




