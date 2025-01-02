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

    private static final int CLOSE_PROXIMITY_DAMAGE = 5;
    private static final int ADJACENT_DAMAGE = 10;

    private List<Point> path;
    private Maze maze;
    private Player player;
    private TextureRegion currentFrame;
    private boolean dir;


    public Enemy(int x, int y, Maze maze, Player player) {
        super(x, y, player);
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
        spriteSheets.add(new Texture(Gdx.files.internal("walk_bot(walk).png")));
        spriteSheets.add(new Texture(Gdx.files.internal("walk_bot(idle).png")));

        int frameWidth = 16;
        int frameHeight = 16;

        int animationFrames = 7;

        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> idleFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(spriteSheets.get(0), col * frameWidth + col * 2, 0, frameWidth, frameHeight));
        }
        idleFrames.add(new TextureRegion(spriteSheets.get(1), 0, 0, frameWidth, frameHeight));
        idleFrames.add(new TextureRegion(spriteSheets.get(1), frameWidth + 2, 0, frameWidth, frameHeight));

        animations.add(new Animation<>(0.1f, walkFrames));
        animations.add(new Animation<>(0.5f, idleFrames));
    }

    public void draw(SpriteBatch batch, float delta) {
        int xDiff = 0;
        int yDiff = 0;

        if (!path.isEmpty()) {
            xDiff = path.get(0).x * GameScreen.tileSize + GameScreen.tileSize / 2 - x;
            yDiff = path.get(0).y * GameScreen.tileSize + GameScreen.tileSize / 2 - y;
        }

        int speed = (int) (6 * GameScreen.tileSize * delta);
        frameCounter += delta;

        if (path.size() < 10 && frameCounter > 3) {
            currentFrame = animations.get(0).getKeyFrame(frameCounter, true);
            if (dir != player.getX() < x) {
                for (Animation<TextureRegion> a : animations) {
                    for (TextureRegion t : a.getKeyFrames()) {
                        t.flip(true, false);
                    }
                }
                dir = player.getX() < x;
            }

            if (xDiff > 0 && xDiff > speed) {
                x += speed;
            } else if (xDiff < 0 && -xDiff > speed) {
                x -= speed;
            }
            if (yDiff > 0 && yDiff > speed) {
                y += speed;
            } else if (yDiff < 0 && -yDiff > speed) {
                y -= speed;
            }
            if (Math.abs(xDiff) + Math.abs(yDiff) < speed * 3) {
                path = bfs(new Point(x / GameScreen.tileSize, y / GameScreen.tileSize),
                        new Point(player.getX() / GameScreen.tileSize, player.getY() / GameScreen.tileSize));
                if (!path.isEmpty())
                    path.remove(0);
            }

        } else {
            currentFrame = animations.get(1).getKeyFrame(frameCounter, true);
            path = bfs(new Point(x / GameScreen.tileSize, y / GameScreen.tileSize),
                    new Point(player.getX() / GameScreen.tileSize, player.getY() / GameScreen.tileSize));
        }
        batch.draw(currentFrame, x - (float) GameScreen.tileSize / 2, y - (float) GameScreen.tileSize / 2, GameScreen.tileSize, GameScreen.tileSize);
    }

    private void handleProximity(){
        int enemyTileX = x / GameScreen.tileSize;
        int enemyTileY = y / GameScreen.tileSize;

        int playerTileX = player.getX() / GameScreen.tileSize;
        int playerTileY = player.getY() / GameScreen.tileSize;

        int distanceX = Math.abs(enemyTileX-playerTileX);
        int distanceY = Math.abs(enemyTileY-playerTileY);

        if (distanceX + distanceY == 1){
            player.updateHealth(-ADJACENT_DAMAGE);
        }
        else if (distanceX + distanceY <= 2){
            player.updateHealth(-CLOSE_PROXIMITY_DAMAGE);
        }
    }

    public List<Point> bfs(Point start, Point target) {
        Queue<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        Map<Point, Point> parent = new HashMap<>(); // Keeps track of parent cells

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.equals(target)) {
                List<Point> path = new ArrayList<>();
                for (Point at = target; at != null; at = parent.get(at)) {
                    path.add(at);
                }
                Collections.reverse(path);
                return path;
            }

            for (int[] direction : DIRECTIONS) {
                Point neighbor = new Point(
                        current.x + direction[0],
                        current.y + direction[1]
                );

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
