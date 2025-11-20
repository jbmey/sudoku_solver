console.log("sudoku.js loaded successfully");


const gridToSolve = document.querySelector("#gridToSolve");
const noSolutionText = document.querySelector("#noSolutionText");
const validate = document.querySelector("#validate");
validate.addEventListener(`click`, () => solveGrid());
const clear = document.querySelector("#clear");
clear.addEventListener(`click`, () => clearGrid());
const btnSolution1 = document.querySelector("#btnSolution1");
const btnSolution2 = document.querySelector("#btnSolution2");

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

        if (value != '') this.classList.toggle("cellOrigin");
    }
    )
);

function initDisplay() {
    gridToSolve.classList.remove("redBg");
    gridToSolve.classList.remove("greenBg");
    noSolutionText.style.display = "none";
    toggleSolutionButton("off");
}

function solveGrid() {
    initDisplay();
    let row = []
    let grid = [];
    for (let r = 0; r < 9; r++) {
        row = []
        for (let c = 0; c < 9; c++) {
            let value = parseInt(document.getElementById(`r${r} c${c}`).value);
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
    initDisplay();
    for (let r = 0; r < 9; r++) {
        for (let c = 0; c < 9; c++) {
            let cell = document.createElement("input");
            cell.setAttribute("type", "number");
            cell.setAttribute("min", "0");
            cell.setAttribute("max", "9");
            cell.setAttribute("id", `r${r} c${c}`);
            cell.setAttribute("class", "cell");
            if ((Math.floor(r / 3) + Math.floor(c / 3)) % 2 === 0) {
                cell.classList.add("subgrid");
            }
            gridToSolve.appendChild(cell);

        }

    }


}

function clearGrid() {
    for (const child of gridToSolve.children) {
        child.value = "";
    }

    cellInputs.forEach((cell) =>
        cell.classList.remove("cellOrigin"));
    initDisplay();
    toggleSolutionButton("off");
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
    if (noSolution) {
        displayNoSolution();
    } else {
        displayResult(result1);
        if (!multipleSolution) {
            toggleSolutionButton("off");
        } else {
            toggleSolutionButton("on");
            btnSolution1.addEventListener(`click`, () => displayResult(result1));
            btnSolution2.addEventListener(`click`, () => displayResult(result2));
        }
    }

}

function displayResult(solution) {
    gridToSolve.classList.add("greenBg");
    let r;
    let c;
    cellInputs.forEach((cell) => {
        if (!cell.classList.contains("cellOrigin")) {
            r = cell.id.charAt(1);
            c = cell.id.charAt(4);
            cell.value = solution[r][c];
        }
    }

    )
}


function displayNoSolution() {
    gridToSolve.classList.add("redBg");
    toggleSolutionButton("off");
    noSolutionText.style.display = "block";
}

function toggleSolutionButton(state) {
    switch (state) {
        case "on":
            btnSolution1.disabled = false;
            btnSolution2.disabled = false;
            btnSolution1.style.display = "block";
            btnSolution2.style.display = "block";
            break;

        case "off":
            btnSolution1.disabled = true;
            btnSolution2.disabled = true;
            btnSolution1.style.display = "none";
            btnSolution2.style.display = "none";
            break;
    }
}





