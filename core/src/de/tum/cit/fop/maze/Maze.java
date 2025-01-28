package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.awt.Point;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.io.*;
import java.util.*;

/**
 * The Maze class used to generate, load and render the maze.
 */
public class Maze {
    private final Properties mazeProperties;
    private final Map<Point, Integer> mazeMap;
    private final Map<Point, Integer> entityMap;
    private Texture texture;
    private Array<TextureRegion> textures;
    private static final int WALL = 0;
    private static final int PATH = 1;
    private static final int[] DX = {-1, 1, 0, 0};
    private static final int[] DY = {0, 0, -1, 1};
    private final int size;

    /**
     * Constructor for Maze, with the name of the properties file from which to load the maze and its size.
     * @param filename The name of the java properties file from which to load the maze.
     * @param size The size of the maze.
     */
    public Maze(String filename, int size) {
        mazeProperties = new Properties();
        mazeMap = new HashMap<>();
        entityMap = new HashMap<>();
        this.size = size;
        try {
            mazeProperties.load(new FileInputStream(filename));
        } catch (IOException e) {
            System.out.println("Maze file " + filename + " not found");
            mazeProperties.put(0,0);
        }
        loadTextures();
        for (String key : mazeProperties.stringPropertyNames()) {
            String[] coords = key.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);

            if (Integer.parseInt(mazeProperties.getProperty(key)) < 10) {
                mazeMap.put(new Point(x, y), Integer.parseInt(mazeProperties.getProperty(key)));
            } else {
                if (Integer.parseInt(mazeProperties.getProperty(key)) != 12) {
                    mazeMap.put(new Point(x, y), PATH);
                } else {
                    mazeMap.put(new Point(x, y), 12);
                }
                entityMap.put(new Point(x, y), Integer.parseInt(mazeProperties.getProperty(key)));
            }
        }
    }

    /**
     * Loads the textures for the maze tiles.
     */
    private void loadTextures() {
        textures = new Array<>(TextureRegion.class);
        texture = new Texture(Gdx.files.internal("tiles.png"));

        int tileSize = 16;

        textures.add(new TextureRegion(texture, 0, 0, tileSize * 2, tileSize * 2));
        textures.add(new TextureRegion(texture, tileSize * 3, tileSize, 0, 0));
        textures.add(new TextureRegion(texture, tileSize * 3, tileSize, 0, 0));
        textures.add(new TextureRegion(texture, tileSize * 3, tileSize, 0, 0));
        textures.add(new TextureRegion(texture, tileSize * 2, tileSize, 0, 0));
    }

    /**
     * Renders the maze.
     * @param batch The SpriteBatch in which to render the maze.
     */
    public void draw(SpriteBatch batch) {
        for (Point point : mazeMap.keySet()) {
            if (mazeMap.get(point) <= 3) {
                batch.draw(textures.get(mazeMap.get(point)), point.x * GameScreen.tileSize, point.y * GameScreen.tileSize, GameScreen.tileSize, GameScreen.tileSize);
            } else {
                batch.draw(textures.get(textures.size - 1), point.x * GameScreen.tileSize, point.y * GameScreen.tileSize, GameScreen.tileSize, GameScreen.tileSize);
            }
        }
    }

    /**
     * Generates a new maze.
     * @param rows The number of rows of the maze.
     * @param cols The number of columns of the maze.
     * @param difficulty The difficulty modifier of the maze.
     * @return The maze in integer array representation.
     */
    public static int[][] generateMaze(int rows, int cols, float difficulty) {
        int[][] maze = new int[rows][cols];

        for (int[] row : maze) {
            Arrays.fill(row, WALL);
        }

        Random random = new Random();
        int startX = random.nextInt(rows / 2) * 2 + 1;
        int startY = random.nextInt(cols / 2) * 2 + 1;

        dfs(maze, startX, startY, random);

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if (maze[i][j] == WALL && random.nextInt(8) == 0) {
                    setArea(maze, j, i, 3, PATH);
                }
            }
        }

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if (maze[i][j] == WALL && countSurroundingTiles(maze, j, i, WALL) == 0) {
                    maze[i][j] = PATH;
                }
                if (maze[i][j] == WALL && countSurroundingTiles(maze, j, i, WALL) == 1
                        && countSurroundingTiles(maze, j - 1, i, WALL) == 1) {
                    maze[i][j] = PATH;
                    maze[i][j - 1] = PATH;
                }
                if (maze[i][j] == WALL && countSurroundingTiles(maze, j, i, WALL) == 1
                    && countSurroundingTiles(maze, j, i - 1, WALL) == 1) {
                    maze[i][j] = PATH;
                    maze[i - 1][j] = PATH;
                }
            }
        }

        Arrays.fill(maze[rows - 1], WALL);
        Arrays.fill(maze[0], WALL);
        for (int i = 1; i < rows - 1; i++) {
            maze[i][0] = WALL;
            maze[i][cols - 1] = WALL;
        }

        createEntrance(maze, random);
        createExit(maze, random);
        createEntities(maze, random, difficulty);

        return maze;
    }

    /**
     * Adds entities such as traps and enemies to the maze.
     * @param maze The maze to modify.
     * @param random A Random instance for randomness.
     * @param difficulty The difficulty modifier which determines the number of traps and enemies.
     */
    public static void createEntities(int[][] maze, Random random, float difficulty) {
        List<Point> chests = new ArrayList<>();

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == PATH && countSurroundingTiles(maze, j, i, WALL) >= 7 && random.nextInt(4) == 0) {
                    int chestType = 20 + random.nextInt(4);
                    maze[i][j] = chestType; //TreasureChest
                    chests.add(new Point(j, i));
                }
                if (maze[i][j] == PATH && random.nextInt((int) (300 / difficulty)) == 0) {
                    maze[i][j] = 11; //Enemy
                }
                if (maze[i][j] == PATH && ((inBounds(maze, j - 1, i) && maze[i][j - 1] == WALL
                        && inBounds(maze, j + 1, i) && maze[i][j + 1] == WALL)
                        || (inBounds(maze, j, i - 1) && maze[i - 1][j] == WALL
                        && inBounds(maze, j, i + 1) && maze[i + 1][j] == WALL))
                        && random.nextInt((int) (10 / difficulty)) == 0) {
                    maze[i][j] = 13; // LaserTrap
                }
                if (maze[i][j] == PATH && random.nextInt((int) (150 / difficulty)) == 0) {
                    maze[i][j] = 14; // spike trap
                }
                if (maze[i][j] == PATH && random.nextInt((int) (150 * difficulty)) == 0) {
                    maze[i][j] = 15; // orb
                }
            }
        }
        Point keyChest = chests.get(random.nextInt(chests.size()));
        maze[keyChest.y][keyChest.x] = 24;
    }

    /**
     * Randomly places the entrance where the player will start the game.
     * @param maze The maze to modify.
     * @param random A Random instance to determine the position of the entrance.
     */
    private static void createEntrance(int[][] maze, Random random) {
        int x = random.nextInt(1,maze[0].length - 1);
        int y = random.nextInt(1, maze.length - 1);

        maze[y][x] = 4;
    }

    /**
     * Randomly places the exit of the maze.
     * @param maze The maze to modify.
     * @param random A Random instance to determine the position of the exit.
     */
    private static void createExit(int[][] maze, Random random) {
        int perimeter = 2 * (maze.length + maze[0].length) - 4;
        int exitPos = random.nextInt(perimeter);

        int x = -1, y = -1;

        if (exitPos < maze[0].length) {
            x = 0;
            y = exitPos;
        } else if (exitPos < maze[0].length + maze.length - 1) {
            x = exitPos - maze[0].length + 1;
            y = maze[0].length - 1;
        } else if (exitPos < 2 * maze[0].length + maze.length - 2) {
            x = maze.length - 1;
            y = 2 * maze[0].length + maze.length - 3 - exitPos;
        } else {
            x = perimeter - exitPos;
            y = 0;
        }

        if (countSurroundingTiles(maze, x, y, PATH) == 3) {
            maze[y][x] = 12;
        } else {
            createExit(maze, random);
        }
    }

    /**
     * Counts the number of tiles of a certain type around a given point in the maze. Used to find patterns in the maze
     * for appropriate entity placements.
     * @param maze The maze to search.
     * @param x The x position of the point.
     * @param y The y position of the point.
     * @param type The tile type to count.
     * @return The number of tiles of the given type surrounding the given point.
     */
    private static int countSurroundingTiles(int[][] maze, int x, int y, int type) {
        int count = 0;

        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (inBounds(maze, nx, ny) && maze[ny][nx] == type) {
                count++;
            }
        }

        return count;
    }

    /**
     * Sets all tiles of an area of the maze to a given type.
     * @param maze The maze to modify.
     * @param x The x position of the middle of the area.
     * @param y The y position of the middle of the area.
     * @param size The side length of the area.
     * @param type The tile type to apply.
     */
    private static void setArea(int[][] maze, int x, int y, int size, int type) {
        for (int i = -size / 2; i < size / 2 + size % 2; i++) {
            for (int j = -size / 2; j < size / 2 + size % 2; j++) {
                if (inBounds(maze, x + j, y + i)) {
                    maze[y + i][x + j] = type;
                }
            }
        }
    }

    /**
     * Depth-first search algorithm used to carve a path into the maze.
     * @param maze An empty maze consisting only of walls.
     * @param startX The x position to start the search from.
     * @param startY The y position to start the search from.
     * @param random A Random instance to randomly select a direction in the path search.
     */
    private static void dfs(int[][] maze, int startX, int startY, Random random) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startX, startY});
        maze[startY][startX] = PATH;

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int x = current[0];
            int y = current[1];

            List<Integer> directions = Arrays.asList(0, 1, 2, 3);
            Collections.shuffle(directions, random);

            boolean foundNextCell = false;

            for (int dir : directions) {
                int nx = x + DX[dir] * 2;
                int ny = y + DY[dir] * 2;

                if (inBounds(maze, nx, ny) && maze[ny][nx] == WALL) {
                    maze[y + DY[dir]][x + DX[dir]] = PATH;
                    maze[ny][nx] = PATH;

                    stack.push(new int[]{nx, ny});
                    foundNextCell = true;
                    break;
                }
            }

            if (!foundNextCell) {
                stack.pop();
            }
        }
    }

    /**
     * Checks if a given point is contained in the maze.
     * @param maze The maze to examine.
     * @param x The x position of the point.
     * @param y The y position of the point.
     * @return true if the x and y values don't exceed the dimensions of the maze, otherwise false.
     */
    public static boolean inBounds(int[][] maze, int x, int y) {
        return y > 0 && y < maze.length && x > 0 && x < maze[0].length;
    }

    /**
     * Saves a maze in a file.
     * @param maze The maze in integer array representation.
     * @param filename The name of the file to save to maze in.
     */
    public static void saveMaze(int[][] maze, String filename) {
        Properties prop = new Properties();
        try {
            FileOutputStream file = new FileOutputStream(filename);
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[i].length; j++) {
                    prop.setProperty(i + "," + j, "" + maze[i][j]);
                }
            }
            prop.store(file, null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Returns the maze as a map with Points as keys and integers representing tile types as values.
     * @return A map containing the walls and paths of the maze.
     */
    public Map<Point, Integer> getMazeMap() {
        return mazeMap;
    }

    /**
     * Returns the maze as a map with Points as keys and integers representing entity types as values.
     * @return A map containing the locations of entities in the maze.
     */
    public Map<Point, Integer> getEntityMap() {
        return entityMap;
    }

    /**
     * Returns the texture used for the tiles.
     * @return The texture used for the tiles
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Returns the size of the maze.
     * @return The size of the maze.
     */
    public int getSize() {
        return size;
    }
}
