import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DynamicMazeSolver extends JPanel {
    private static final char NOT_VISITED = '*';
    private static final char VISITED = '#';
    private static final char WALL = '@';
    private static final char CURRENT_POS = '+';
    private static final char EXIT = 'X';
    private static final char BACKTRACK = 'B'; // New character for backtracking

    private static final int PAIR_A_ROW = 0;
    private static final int PAIR_A_COL = 1;
    private static final int PAIR_B_ROW = 2;
    private static final int PAIR_B_COL = 3;

    private char[][] mazeMap;
    private char[][] trajectoryMap;
    private char[][] mazeMapOrigin;
    private int[][] portalMap;
    private int numRow;
    private int numColumn;
    private String mazeID;

    private int startRow;
    private int startColumn;
    private int EXIT_ROW;
    private int EXIT_COL;

    private JFrame frame;

    public DynamicMazeSolver(String mazeID, int numRow, int numColumn) {
        this.numRow = numRow;
        this.numColumn = numColumn;
        this.mazeID = mazeID;

        this.mazeMap = new char[numRow][numColumn];
        this.trajectoryMap = new char[numRow][numColumn];
        this.mazeMapOrigin = new char[numRow][numColumn];
        this.portalMap = new int[10][4];

        startRow = numRow - 1;
        startColumn = 0;

        frame = new JFrame("Dynamic Maze Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(this);
        frame.setVisible(true);
    }

    public boolean solveMaze() {
        getMazeMap();
        boolean result = mazeBacktrack(startRow, startColumn);
        repaint();
        return result;
    }

    public boolean mazeBacktrack(int BTrow, int BTcol) {
        trajectoryMap[BTrow][BTcol] = CURRENT_POS;
        repaint();
        sleep(100); // Delay to slow down the solution

        if (BTrow == EXIT_ROW && BTcol == EXIT_COL) {
            return true;
        }

        if (Character.isDigit(mazeMapOrigin[BTrow][BTcol])) {
            int digit = Character.getNumericValue(mazeMap[BTrow][BTcol]);
            int[] teleportLocation = portalMove(digit, BTrow, BTcol);

            if (mazeMap[teleportLocation[0]][teleportLocation[1]] != VISITED) {
                mazeMap[BTrow][BTcol] = VISITED;
                updateTrajectory(BTrow, BTcol);
                repaint();

                if (mazeBacktrack(teleportLocation[0], teleportLocation[1])) {
                    return true;
                }
                trajectoryMap[BTrow][BTcol] = BACKTRACK; // Mark as backtrack
                mazeMap[BTrow][BTcol] = NOT_VISITED;
                repaint();
                sleep(100); // Slow down backtracking visualization
            }
        }

        if (isMoveUpValid(BTrow, BTcol) && (mazeMap[BTrow - 1][BTcol] != VISITED)) {
            mazeMap[BTrow][BTcol] = VISITED;
            updateTrajectory(BTrow, BTcol);
            repaint();

            if (mazeBacktrack(BTrow - 1, BTcol)) {
                return true;
            }
            trajectoryMap[BTrow][BTcol] = BACKTRACK;
            mazeMap[BTrow][BTcol] = NOT_VISITED;
            repaint();
            sleep(100);
        }

        if (isMoveRightValid(BTrow, BTcol) && (mazeMap[BTrow][BTcol + 1] != VISITED)) {
            mazeMap[BTrow][BTcol] = VISITED;
            updateTrajectory(BTrow, BTcol);
            repaint();

            if (mazeBacktrack(BTrow, BTcol + 1)) {
                return true;
            }
            trajectoryMap[BTrow][BTcol] = BACKTRACK;
            mazeMap[BTrow][BTcol] = NOT_VISITED;
            repaint();
            sleep(100);
        }

        if (isMoveLeftValid(BTrow, BTcol) && (mazeMap[BTrow][BTcol - 1] != VISITED)) {
            mazeMap[BTrow][BTcol] = VISITED;
            updateTrajectory(BTrow, BTcol);
            repaint();

            if (mazeBacktrack(BTrow, BTcol - 1)) {
                return true;
            }
            trajectoryMap[BTrow][BTcol] = BACKTRACK;
            mazeMap[BTrow][BTcol] = NOT_VISITED;
            repaint();
            sleep(100);
        }

        if (isMoveDownValid(BTrow, BTcol) && (mazeMap[BTrow + 1][BTcol] != VISITED)) {
            mazeMap[BTrow][BTcol] = VISITED;
            updateTrajectory(BTrow, BTcol);
            repaint();

            if (mazeBacktrack(BTrow + 1, BTcol)) {
                return true;
            }
            trajectoryMap[BTrow][BTcol] = BACKTRACK;
            mazeMap[BTrow][BTcol] = NOT_VISITED;
            repaint();
            sleep(100);
        }

        trajectoryMap[BTrow][BTcol] = BACKTRACK; // Mark as backtrack on final retreat
        mazeMap[BTrow][BTcol] = VISITED;
        repaint();
        sleep(100);

        return false;
    }

    private int[] portalMove(int portalID, int Mrow, int Mcol) {
        int[] portalPoints = new int[2];
        if ((portalMap[portalID][PAIR_A_ROW] == Mrow) && (portalMap[portalID][PAIR_A_COL] == Mcol)) {
            portalPoints[0] = portalMap[portalID][PAIR_B_ROW];
            portalPoints[1] = portalMap[portalID][PAIR_B_COL];
        } else {
            portalPoints[0] = portalMap[portalID][PAIR_A_ROW];
            portalPoints[1] = portalMap[portalID][PAIR_A_COL];
        }

        return portalPoints;
    }

    private boolean isMoveUpValid(int Mrow, int Mcol) {
        return (((Mrow - 1) >= 0) && (mazeMap[Mrow - 1][Mcol] != WALL));
    }

    private boolean isMoveRightValid(int Mrow, int Mcol) {
        return (((Mcol + 1) < numColumn) && (mazeMap[Mrow][Mcol + 1] != WALL));
    }

    private boolean isMoveLeftValid(int Mrow, int Mcol) {
        return (((Mcol - 1) >= 0) && (mazeMap[Mrow][Mcol - 1] != WALL));
    }

    private boolean isMoveDownValid(int Mrow, int Mcol) {
        return (((Mrow + 1) < numRow) && (mazeMap[Mrow + 1][Mcol] != WALL));
    }

    private void getMazeMap() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(mazeID));
            int row = 0;
            String line;
            boolean[] pairPresent = new boolean[10];

            while ((line = reader.readLine()) != null) {
                for (int column = 0; column < numColumn; column++) {
                    mazeMap[row][column] = line.charAt(column);
                    trajectoryMap[row][column] = line.charAt(column);
                    mazeMapOrigin[row][column] = line.charAt(column);

                    if (line.charAt(column) == EXIT) {
                        EXIT_ROW = row;
                        EXIT_COL = column;
                    } else if (Character.isDigit(line.charAt(column))) {
                        int digit = Character.getNumericValue(line.charAt(column));
                        if (!pairPresent[digit]) {
                            portalMap[digit][PAIR_A_ROW] = row;
                            portalMap[digit][PAIR_A_COL] = column;
                            pairPresent[digit] = true;
                        } else {
                            portalMap[digit][PAIR_B_ROW] = row;
                            portalMap[digit][PAIR_B_COL] = column;
                        }
                    }
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    private void updateTrajectory(int row, int col) {
        if (!Character.isDigit(mazeMapOrigin[row][col]) && trajectoryMap[row][col] != BACKTRACK) {
            trajectoryMap[row][col] = VISITED;
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = Math.min(getWidth() / numColumn, getHeight() / numRow);
        for (int row = 0; row < numRow; row++) {
            for (int col = 0; col < numColumn; col++) {
                if (mazeMapOrigin[row][col] == WALL) {
                    g.setColor(Color.BLACK);
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                } else if (trajectoryMap[row][col] == VISITED) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                } else if (trajectoryMap[row][col] == BACKTRACK) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                } else if (trajectoryMap[row][col] == CURRENT_POS) {
                    g.setColor(Color.ORANGE);
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                }

                g.setColor(Color.BLACK);
                g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);

                if (Character.isDigit(mazeMapOrigin[row][col])) {
                    g.setColor(Color.BLUE);
                    g.drawString(Character.toString(mazeMapOrigin[row][col]), col * cellSize + cellSize / 4, row * cellSize + cellSize / 2);
                }

                if (row == startRow && col == startColumn) {
                    g.setColor(Color.ORANGE);
                    g.drawString("S", col * cellSize + cellSize / 4, row * cellSize + cellSize / 2);
                }

                if (row == EXIT_ROW && col == EXIT_COL) {
                    g.setColor(Color.GREEN);
                    g.drawString("E", col * cellSize + cellSize / 4, row * cellSize + cellSize / 2);
                }
            }
        }
    }
    public static void main(String args[]) throws Exception
    {
        DynamicMazeSolver maze = new DynamicMazeSolver(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
           
        if(maze.solveMaze())
			System.out.println(args[0] + " Solved!");
		else
			System.out.println(args[0] + " could not get solved!");
    }
}
