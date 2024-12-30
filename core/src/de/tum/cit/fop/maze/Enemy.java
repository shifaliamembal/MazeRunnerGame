package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Enemy extends Entity {

    //private static final int[] DX = {0, 0, -1, 1};
    //private static final int[] DY = {-1, 1, 0, 0};
    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    private List<Point> path;
    private Maze maze;
    private Player player;
    private TextureRegion currentFrame;
    private float frameCounter;


    public Enemy(int x, int y, Maze maze, Player player) {
        super(x, y);
        this.maze = maze;
        //System.out.println("Spawn: " + x + " " + y);

        this.player = player;
        path = bfs(new Point(super.x / GameScreen.tileSize, super.y / GameScreen.tileSize),
                new Point(player.getX() / GameScreen.tileSize, player.getY() / GameScreen.tileSize));

//        for (var entry : maze.getMazeMap().entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }
    }

    public void loadAssets() {
        Texture walkSheet = new Texture(Gdx.files.internal("walk_bot(walk).png"));

        int frameWidth = 16;
        int frameHeight = 16;

        int animationFrames = 7;

        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < animationFrames; col++) {
//            if (col == animationFrames - 1)
//                frameWidth -= 2;
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth + col * 2, 0, frameWidth, frameHeight));
        }

        animations.add(new Animation<>(0.1f, walkFrames));
    }

    public void draw(SpriteBatch batch, float delta) {

        currentFrame = animations.get(0).getKeyFrame(frameCounter, true);

        batch.draw(currentFrame, x - (float) GameScreen.tileSize / 2, y - (float) GameScreen.tileSize / 2, GameScreen.tileSize, GameScreen.tileSize);

        if (path.isEmpty()) {
            return;
        }

        int xDiff = path.get(0).x * GameScreen.tileSize + GameScreen.tileSize / 2 - x;
        int yDiff = path.get(0).y * GameScreen.tileSize + GameScreen.tileSize / 2 - y;
        int speed = (int) (5 * GameScreen.tileSize * delta);
        frameCounter += delta;
        // System.out.println(path.get(0).x + "," + path.get(0).y);


        if (xDiff > 0 && xDiff > speed) {
            x += speed;
        } else if (xDiff < 0 && -xDiff > speed) {
            x -= speed;
            //currentFrame.flip(true, false);
        }
        if (yDiff > 0 && yDiff > speed) {
            y += speed;
        } else if (yDiff < 0 && -yDiff > speed) {
            y -= speed;
        }
        if (Math.abs(xDiff) + Math.abs(yDiff) < speed * 3) {
            path = bfs(new Point(x / GameScreen.tileSize, y / GameScreen.tileSize),
                    new Point(player.getX() / GameScreen.tileSize, player.getY() / GameScreen.tileSize));
            path.remove(0);
            //System.out.println(path.size());
//            for (Point p : path) {
//                System.out.println(p.x + " " + p.y);
//            }
        }
        //System.out.println(xDiff + " " + yDiff);
    }

    public List<Point> bfs(Point start, Point target) {
        Queue<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        Map<Point, Point> parent = new HashMap<>(); // Keeps track of parent cells

        //System.out.println("Start: " + start.x + " " + start.y);
        //System.out.println("Target: " + target.x + " " + target.y);
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            // Check if we reached the goal
            if (current.equals(target)) {
                List<Point> path = new ArrayList<>();
                for (Point at = target; at != null; at = parent.get(at)) {
                    path.add(at);
                }
                Collections.reverse(path);
                return path;
            }
            // Explore neighbors
            for (int[] direction : DIRECTIONS) {
                Point neighbor = new Point(
                        current.x + direction[0],
                        current.y + direction[1]
                );

                // Check if the neighbor is valid and traversable
                if (maze.getMazeMap().containsKey(neighbor) && maze.getMazeMap().get(neighbor) != 0 && !visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                }
            }
        }
        return Collections.emptyList();
    }

}
