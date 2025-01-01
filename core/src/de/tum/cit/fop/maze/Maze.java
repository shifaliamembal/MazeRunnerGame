package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.awt.Point;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.io.*;
import java.util.*;

public class Maze {
    private Properties mazeProperties;
    private Map<Point, Integer> mazeMap;
    private Map<Point, Integer> entityMap;
    private Texture texture;
    private Array<TextureRegion> textures;
//    private int rows;
//    private int cols;
    private static final int WALL = 0;
    private static final int PATH = 1;
    private static final int[] DX = {-1, 1, 0, 0};
    private static final int[] DY = {0, 0, -1, 1};

    public Maze(String filename) {
        mazeProperties = new Properties();
        mazeMap = new HashMap<>();
        entityMap = new HashMap<>();
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

//        rows = (int) mazeMap.keySet().stream()
//                .mapToDouble(Point::getY)
//                .max().orElse(-1) + 1;
//        cols = (int) mazeMap.keySet().stream()
//                .mapToDouble(Point::getX)
//                .max().orElse(-1) + 1;
    }

    private void loadTextures() {
        textures = new Array<>(TextureRegion.class);
        texture = new Texture(Gdx.files.internal("basictiles.png"));
        Texture floor = new Texture(Gdx.files.internal("floor.png"));

        int tileSize = 16;

        textures.add(new TextureRegion(texture, 0, 0, tileSize, tileSize));
        textures.add(new TextureRegion(floor, tileSize, 0, 0, 0));
        textures.add(new TextureRegion(floor, tileSize, 0, 0, 0));
        textures.add(new TextureRegion(texture, tileSize, tileSize, tileSize, tileSize));
    }

    public void draw(SpriteBatch batch) {
        for (Point point : mazeMap.keySet()) {
            if (mazeMap.get(point) <= 2) {
                batch.draw(textures.get(mazeMap.get(point)), point.x * GameScreen.tileSize, point.y * GameScreen.tileSize, GameScreen.tileSize, GameScreen.tileSize);
            } else {
                batch.draw(textures.get(textures.size - 1), point.x * GameScreen.tileSize, point.y * GameScreen.tileSize, GameScreen.tileSize, GameScreen.tileSize);
            }
        }
    }

    public static int[][] generateMaze(int rows, int cols) {
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
//                    setArea(maze, j, i, random.nextInt(1, 6), PATH);
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
        createEntities(maze, random);

        return maze;
    }

    public static void createEntities(int[][] maze, Random random) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == PATH && countSurroundingTiles(maze, j, i, WALL) >= 7 && random.nextInt(4) == 0) {
                    maze[i][j] = 10;
                    //System.out.println(i + " " + j + " " + countSurroundingTiles(maze, i, j, WALL));
                }
                if (maze[i][j] == PATH && random.nextInt(200) == 0) {
                    maze[i][j] = 11;
                }
            }
        }
    }

    private static void createEntrance(int[][] maze, Random random) {
        int x = random.nextInt(1,maze[0].length - 1);
        int y = random.nextInt(1, maze.length - 1);

        maze[y][x] = 3;
    }

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

    private static void setArea(int[][] maze, int x, int y, int size, int type) {
        for (int i = -size / 2; i < size / 2 + size % 2; i++) {
            for (int j = -size / 2; j < size / 2 + size % 2; j++) {
                if (inBounds(maze, x + j, y + i)) {
                    maze[y + i][x + j] = type;
                }
            }
        }
    }

    private static void dfs(int[][] maze, int x, int y, Random random) {
        maze[y][x] = PATH;

        List<Integer> directions = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(directions, random);

        for (int dir : directions) {
            int nx = x + DX[dir] * 2;
            int ny = y + DY[dir] * 2;

            if (inBounds(maze, nx, ny)) {
                if (maze[ny][nx] == WALL) {
                    maze[y + DY[dir]][x + DX[dir]] = PATH;
                    dfs(maze, nx, ny, random);
                }
            }
        }
    }

    public static boolean inBounds(int[][] maze, int x, int y) {
        return y > 0 && y < maze.length && x > 0 && x < maze[0].length;
    }

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

    public Map<Point, Integer> getMazeMap() {
        return mazeMap;
    }

    public Map<Point, Integer> getEntityMap() {
        return entityMap;
    }

    public Texture getTexture() {
        return texture;
    }
}
