package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Enemy robots that patrol the maze and chase and attack the player when he comes close.
 */
public class Enemy extends Entity {

    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    private List<Point> playerPath;
    private List<Point> patrolPath;
    private Maze maze;
    private Player player;
    private TextureRegion currentFrame;
    private boolean dir;
    private float damageCooldown;
    private float pathCooldown;
    private float waitTime;
    private float attackTime;
    private float difficulty;
    private boolean dead;
    private Sound attackSound;



    /**
     * Constructor for the Enemy class. Sets its coordinates, references to the maze and player
     * as well as the difficulty setting.
     * @param x The x position of the enemy.
     * @param y The y position of the enemy.
     * @param maze The maze in which the game takes place.
     * @param player The player with which to interact.
     * @param difficulty The difficulty modifier of the current level.
     */
    public Enemy(int x, int y, Maze maze, Player player, float difficulty) {
        super(x, y, player);
        this.maze = maze;
        this.difficulty = difficulty;

        this.player = player;
        playerPath = new ArrayList<>();
        patrolPath = new ArrayList<>();
        waitTime = 3;
        damageCooldown = 3;
        pathCooldown = 1;
        attackTime = 0;
        dead = false;
        attackSound = Gdx.audio.newSound(Gdx.files.internal("enemyattack.mp3"));

    }

    protected void loadAssets() {
        spriteSheets.add(new Texture(Gdx.files.internal("Spider.png")));

        int frameWidth = 96;
        int frameHeight = 96;

        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> idleFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> attackFrames = new Array<>(TextureRegion.class);
        Array<TextureRegion> deathFrames = new Array<>(TextureRegion.class);

        for (int col = 0; col < 10; col++) {
            walkFrames.add(new TextureRegion(spriteSheets.get(0), col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
        }
        for (int col = 0; col < 4; col++) {
            idleFrames.add(new TextureRegion(spriteSheets.get(0), col * frameWidth, 4 * frameHeight, frameWidth, frameHeight));
        }
        for (int col = 0; col < 11; col++) {
            attackFrames.add(new TextureRegion(spriteSheets.get(0), col * frameWidth, 0, frameWidth, frameHeight));
        }
        for (int col = 0; col < 9; col++) {
            deathFrames.add(new TextureRegion(spriteSheets.get(0), col * frameWidth, frameHeight, frameWidth, frameHeight));
        }

        animations.add(new Animation<>(0.02f, walkFrames));
        animations.add(new Animation<>(0.1f, idleFrames));
        animations.add(new Animation<>(0.035f, attackFrames));
        animations.add(new Animation<>(0.1f, deathFrames));
        currentFrame = walkFrames.get(0);
    }

    public void draw(SpriteBatch batch, float delta) {
        frameCounter += delta;

        if (!dead && maze.getMazeMap().get(new Point(x / GameScreen.tileSize, y / GameScreen.tileSize)) == 2) {
            die();
        }

        if (dead) {
            batch.draw(animations.get(3).getKeyFrame(frameCounter, false), x - GameScreen.tileSize,
                    y - GameScreen.tileSize / 2, GameScreen.tileSize * 2, GameScreen.tileSize * 2);
            return;
        }

        attackTime -= delta;
        damageCooldown -= delta;

        if (attackTime <= 0) {
            handleMovement(delta);
        }

        handleProximity();

        batch.draw(currentFrame, x - GameScreen.tileSize, y - GameScreen.tileSize / 2,
                GameScreen.tileSize * 2, GameScreen.tileSize * 2);
    }

    /**
     * Calculates where the enemy should walk.
     * @param delta The time in seconds since the last render.
     */
    private void handleMovement(float delta) {
        int xDiff = 0;
        int yDiff = 0;
        int speed;
        List<Point> currentPath = new ArrayList<>();

        waitTime -= delta;
        pathCooldown -= delta;

        boolean chasePlayer = !playerPath.isEmpty() && playerPath.size() <= 10;

        if (!playerPath.isEmpty() && playerPath.size() <= 10) {
            currentPath = playerPath;
            speed = (int) (9 * GameScreen.tileSize * delta);
            patrolPath = Collections.emptyList();
        } else {
            if (patrolPath.isEmpty()) {
                patrolPath = randomPath();
            }
            currentPath = patrolPath;
            speed = (int) (4 * GameScreen.tileSize * delta);
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
            if (pathCooldown <= 0) {
                playerPath = bfs(new Point(x / GameScreen.tileSize, y / GameScreen.tileSize),
                        new Point(player.getX() / GameScreen.tileSize, player.getY() / GameScreen.tileSize));
                pathCooldown = 0.05f;
            }
        }
    }

    /**
     * Handles behaviour when the player is close, initiating the attack animation and dealing damage.
     */
    private void handleProximity(){
        if (playerDistance() < GameScreen.tileSize && attackTime <= 0) {
            frameCounter = 0;
            currentFrame = animations.get(2).getKeyFrame(frameCounter, true);
            attackTime = animations.get(2).getAnimationDuration();
        } else if (attackTime >= 0) {
            currentFrame = animations.get(2).getKeyFrame(frameCounter, true);
            if (damageCooldown <= 0 && frameCounter > animations.get(2).getFrameDuration() * 6 && playerDistance() < GameScreen.tileSize * 1.5) {
                player.updateHealth((int) (-10 * difficulty));
                damageCooldown = 0.5f;
                attackSound.play();
            }
        }
    }

    /**
     * Breadth-first search algorithm used to find the shortest path to the player.
     * @param start The current position of the enemy, used as the starting point for the search.
     * @param target The current position of the player, used as thr target for the search.
     * @return The shortest list of points the enemy needs to traverse to get to the player.
     *         An empty list if the player can't be found in a certain number of iterations.
     */
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

                try {
                    if (maze.getMazeMap().get(neighbor) != 0 && !visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                        parent.put(neighbor, current);
                    }
                } catch (NullPointerException e) {}
            }
            if (visited.size() > 400) {
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    /**
     * A random path used for the patrolling behavior.
     * @return A list of points which serves as an arbitrary path for patrolling. Points with coordinates (0,0)
     *         indicate the enemy should wait for some time before going to the next point.
     */
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

    /**
     * Kills the enemy and rewards the player with points.
     */
    public void die() {
        dead = true;
        player.addPoints(200);
        frameCounter = 0;
    }

    public void dispose() {
        if (attackSound != null) {
            attackSound.dispose();
        }
    }
}
