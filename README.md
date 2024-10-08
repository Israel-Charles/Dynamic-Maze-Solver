# GUI Dynamic Maze Solver
Dynamic Maze Solver is a Java-based graphical application that demonstrates maze-solving techniques using a backtracking algorithm. This project visualizes the process of solving a maze in real-time, showcasing pathfinding, backtracking, and teleportation mechanics through portals.

### Demo > https://codepad.site/pad/a49untl5

## Features

- **Real-time Visualization:** Watch the algorithm solve the maze step by step.
- **Backtracking:** See the algorithm backtrack from dead ends and visualize the path taken.
- **Portals:** Experience teleportation through numbered portals.
- **Start and End Points:** Clearly labeled start (`S`) and exit (`E`) points.
- **Interactive Graphics:** Uses Java Swing to provide a graphical representation of the maze.

## How It Works

1. **Maze Representation:** The maze is represented as a 2D grid, read from a text file where each character denotes a wall, path, portal, start, or exit.
2. **Pathfinding Algorithm:** A backtracking algorithm explores the maze, visiting valid paths, utilizing portals, and marking visited and backtracked paths.
3. **Visualization:** The graphical interface updates in real-time to show the current position, visited cells, and backtracked paths.

## Prerequisites

- Java Development Kit (JDK) 8 or later.
- An IDE or text editor to view and run the Java program.

## Setup

1. **Clone the Repository:**

```bash
git clone https://github.com/Israel-Charles/Dynamic-Maze-Solver.git
```

2. **Navigate to the Project Directory:**

```bash
cd DynamicMazeSolver
```

3. **Compile the Java Program:**

```bash
javac MagicMaze.java
```

4. **Run the Program:**



### Maze File Format

1. Create a text file in the project directory to pass it as argument when running the program.

2. Use characters to design your maze:

- '@' for walls
- '*' for paths
- '0-9' for portal pairs
- 'X' for the exit point<br/>

#### ***Note: Maze will always start at the bottom left corner***

#### ***Some example maze files are in the repo***

## Run the Program

```bash
java DynamicMazeSolver <mazefile.txt> <# of rows> <# of cols>
```
Example with a maze file that has 15 rows and 20 columns:
```bash
java DynamicMazeSolver maze4.txt 15 20
```
