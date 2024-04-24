function make2DArray(rows, cols) {
    let arr = new Array(rows);
    for (let row = 0; row < arr.length; row++) {
        arr[row] = new Array(cols);

        for (let col = 0; col < arr[row].length; col++) {
            arr[row][col] = 0;
        }
    }

    return arr;
}

let grid;
let w = 5;
let rows, cols;
let hueValue = 60;

function withinCols(col) {
    return col >= 0 && col <= cols - 1;
}

function withinRows(row) {
    return row >= 0 && row < rows - 1
}

function setup() {
    createCanvas(600, 800);
    colorMode(HSB, 360, 255, 255);
    rows = height / w;
    cols = width / w;
    grid = make2DArray(rows, cols);
}

function mouseDragged() {
    let mouseRow = floor(mouseY / w);
    let mouseCol = floor(mouseX / w);

    let matrix = 2;
    let extent = floor(matrix / 2);
    for (let r = -extent; r <= extent; r++){
        for (let c = -extent; c <= extent; c++){
            let row = mouseRow + r;
            let col = mouseCol + c; 
            if (withinRows(row) && withinCols(col)) {
            grid[row][col] = hueValue;
            }
        }
    }
}


function draw() {
    background(0);

    // Display the current grid
    for (let row = 0; row < rows; row++) {
        for (let col = 0; col < cols; col++) {
            noStroke();
            if (grid[row][col] > 0){
                fill(grid[row][col], 255, 255);

                let y = row * w;
                let x = col * w;

                square(x, y, w);
            }
        }
    }

    let nextGrid = make2DArray(rows, cols);
    for (let row = 0; row < rows; row++) {
        for (let col = 0; col < cols; col++) {
            let currentCell = grid[row][col];

            if (currentCell > 1) {
                // Check if we're not at the bottom row
                if (row < rows - 1) {
                    let belowCell = grid[row + 1][col];
;
                    let dir = random([-1, 1]);

                    let belowA = grid[row + 1][col + dir];
                    let belowB = grid[row + 1][col - dir];
                    // Move the cell down if the cell below is empty and within bounds
                    if (belowCell === 0) {
                        nextGrid[row + 1][col] = currentCell;
                    } else if (belowA === 0) {
                        grid[row + 1][col + dir] = currentCell;
                      } else if (belowB === 0) {
                        grid[row + 1][col - dir] = currentCell;
                      } else {
                        nextGrid[row][col] = currentCell;
                      }
                } else {
                    // If we're at the bottom row, keep the cell in the same position
                    nextGrid[row][col] = currentCell;
                }
            }
        }
    }

    grid = nextGrid;
}
