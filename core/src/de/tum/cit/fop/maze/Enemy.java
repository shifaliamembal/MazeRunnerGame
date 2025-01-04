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
//    private static final float DAMAGE_COOLDOWN = 0.5f;
//    private float

    private List<Point> playerPath;
    private List<Point> patrolPath;
    private Maze maze;
    private Player player;
    private TextureRegion currentFrame;
    private boolean dir;
    private float damageCooldown;
    private float waitTime;


    public Enemy(int x, int y, Maze maze, Player player) {
        super(x, y, player);
        this.maze = maze;

        this.player = player;
        playerPath = bfs(new Point(super.x / GameScreen.tileSize, super.y / GameScreen.tileSize),
                new Point(player.getX() / GameScreen.tileSize, player.getY() / GameScreen.tileSize));
        patrolPath = new ArrayList<>();
        waitTime = 1;
    }

    protected void loadAssets() {
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
        frameCounter += delta;
        damageCooldown -= delta;

        handleMovement(delta);

        handleProximity();

        batch.draw(currentFrame, x - (float) GameScreen.tileSize / 2, y - (float) GameScreen.tileSize / 2, GameScreen.tileSize, GameScreen.tileSize);
    }

    private void handleMovement(float delta) {
        int xDiff = 0;
        int yDiff = 0;
        int speed;
        List<Point> currentPath = new ArrayList<>();

        waitTime -= delta;

        boolean chasePlayer = playerPath.size() <= 10;
        if (playerPath.size() <= 10) {
            currentPath = playerPath;
            speed = (int) (6 * GameScreen.tileSize * delta);
            patrolPath = Collections.emptyList();
        } else {
            if (patrolPath.isEmpty()) {
                patrolPath = randomPath();
            }
            currentPath = patrolPath;
            speed = (int) (3 * GameScreen.tileSize * delta);
        }

        if (!currentPath.isEmpty()) {
            xDiff = currentPath.get(0).x * GameScreen.tileSize + GameScreen.tileSize / 2 - x;
            yDiff = currentPath.get(0).y * GameScreen.tileSize + GameScreen.tileSize / 2 - y;
        }

        if (waitTime <= 0) {
            if (!currentPath.isEmpty() && currentPath.get(0).x == 0 && currentPath.get(0).y == 0) {
                waitTime += 2f;
                patrolPath.remove(0);
                return;
            }
            currentFrame = animations.get(0).getKeyFrame(frameCounter, true);

            if (xDiff > 0 && xDiff > speed) {
                x += speed;
                if (dir) {
                    for (Animation<TextureRegion> a : animations) {
                        for (TextureRegion t : a.getKeyFrames()) {
                            t.flip(true, false);
                        }
                    }
                    dir = false;
                }
            } else if (xDiff < 0 && -xDiff > speed) {
                x -= speed;
                if (!dir) {
                    for (Animation<TextureRegion> a : animations) {
                        for (TextureRegion t : a.getKeyFrames()) {
                            t.flip(true, false);
                        }
                    }
                    dir = true;
                }
            }
            if (yDiff > 0 && yDiff > speed) {
                y += speed;
            } else if (yDiff < 0 && -yDiff > speed) {
                y -= speed;
            }
            if (Math.abs(xDiff) + Math.abs(yDiff) < speed * 3) {
                playerPath = bfs(new Point(x / GameScreen.tileSize, y / GameScreen.tileSize),
                        new Point(player.getX() / GameScreen.tileSize, player.getY() / GameScreen.tileSize));
                if (chasePlayer) {
                    if (!playerPath.isEmpty())
                        playerPath.remove(0);
                } else {
                    patrolPath.remove(0);
                }
            }

        } else {
            currentFrame = animations.get(1).getKeyFrame(frameCounter, true);
            playerPath = bfs(new Point(x / GameScreen.tileSize, y / GameScreen.tileSize),
                    new Point(player.getX() / GameScreen.tileSize, player.getY() / GameScreen.tileSize));
        }
    }

    private void handleProximity(){
//        int enemyTileX = x / GameScreen.tileSize;
//        int enemyTileY = y / GameScreen.tileSize;
//
//        int playerTileX = player.getX() / GameScreen.tileSize;
//        int playerTileY = player.getY() / GameScreen.tileSize;

//        int distanceX = Math.abs(x - player.getX());
//        int distanceY = Math.abs(y - player.getY());
//
//        if (distanceX + distanceY == 1){
//            player.updateHealth(-ADJACENT_DAMAGE);
//        }
//        else if (distanceX + distanceY <= 2){
//            player.updateHealth(-CLOSE_PROXIMITY_DAMAGE);
//        }

        if (playerDistance() < GameScreen.tileSize && damageCooldown <= 0) {
            player.updateHealth(-ADJACENT_DAMAGE);
            damageCooldown = 0.5f;
        }
    }

    private List<Point> bfs(Point start, Point target) {
        Queue<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        Map<Point, Point> parent = new HashMap<>();

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

    private List<Point> randomPath() {
        List<Point> path = new ArrayList<>();
        Point current = new Point(x / GameScreen.tileSize, y / GameScreen.tileSize);
        Random random = new Random();
        int dir;

        path.add(new Point(0,0));
        path.add(new Point(0,0));

        while (path.size() < 20) {
            dir = random.nextInt(5);
            if (dir == 4) {
                path.add(new Point(0 ,0));
                continue;
            }
            Point next = new Point(current.x + DIRECTIONS[dir][0], current.y + DIRECTIONS[dir][1]);
            if (maze.getMazeMap().containsKey(next) && maze.getMazeMap().get(next) != 0) {
                path.add(next);
                current = next;
            }
        }
        return path;
    }

}
