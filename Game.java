import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel {
    public static final double FPS = 30.0;
    public static final double PLAYERSTARTX = ((double)GameRunner.SCREENWIDTH)/2;
    public static final double PLAYERSTARTY = GameRunner.SCREENHEIGHT-100;
    private final Player player = new Player(new DonovanMage(), PLAYERSTARTX, PLAYERSTARTY);
    
    public static final Level[] LEVELS = {
            new Level(new Enemy[] { new Zombie(50.0, 50.0)}),
            new Level(new Enemy[] { new Zombie(50.0, 50.0),
                                    new Zombie(250.0, 50.0),
                                    new Zombie(550.0, 50.0) }),
            new Level(new Enemy[] { new Zombie(100.0, 200.0),
                                    new Zombie(400.0, 200.0),
                                    new Skeleton(200.0, 150.0),
                                    new Skeleton(300.0, 150.0) }),
            new Level(new Enemy[] { new Zombie(150.0, 250.0),
                                    new Zombie(350.0, 250.0),
                                    new Ghost(200.0, 150.0),
                                    new Skeleton(700, 300),
                                    new Skeleton(100, 300) }),
            new Level(new Enemy[] { new Ghost(350.0, 450.0),
                                    new Ghost(450.0, 450.0),
                                    new Zombie(100.0, 150.0),
                                    new Zombie(200.0, 150.0),
                                    new Zombie(600.0, 150.0),
                                    new Zombie(700.0, 150.0),                        
                                    new Skeleton(700, 300),
                                    new Skeleton(100, 300) }),
            new Level(new Enemy[] { new Ghost(350.0, 450.0),
                                    new Ghost(450.0, 450.0),
                                    new Ghost(400.0, 150.0),
                                    new Skeleton(300.0, 250.0),
                                    new Zombie(500.0, 250.0),
                                    new Zombie(700.0, 250.0),
                                    new Zombie(500.0, 150.0),
                                    new Zombie(700.0, 150.0),                        
                                    new Skeleton(700, 300),
                                    new Skeleton(100, 300),
                                    new SkeletonShaman(400, 300) }),
        new Level(new Enemy[] { new Ghost(350.0, 450.0),
                                    new Ghost(450.0, 450.0),
                                    new Ghost(300.0, 450.0),
                                    new Ghost(500.0, 350.0),
                                    new Ghost(300.0, 350.0),
                                    new Ghost(400.0, 200.0),
                                    new Zombie(700.0, 550.0),
                                    new Zombie(500.0, 550.0),
                                    new Zombie(600.0, 450.0),
                                    new Zombie(400.0, 450.0),
                                    new Zombie(700.0, 150.0),                        
                                    new SkeletonShaman(300, 300),
                                    new SkeletonShaman(100, 400),
                                    new SkeletonShaman(200, 500)})
                                    
            
        };
    
    private final Listener l = new Listener(this);
    private final ArrayList<Projectile> playerProjectiles = new ArrayList<>();

    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Projectile> enemyProjectiles = new ArrayList<>();

    private final Portal portal = new Portal((GameRunner.SCREENWIDTH - Portal.SIZE) / 2, 0);
    
    public Game() {
        super();
        ActionListener action = evt -> this.update(); //create an action listener using lambda expression
        new Timer((int) (1000.0 / Game.FPS), action).start(); // Do action FPS times in one second
        //1000/FPS is the delay (time between each frame)
        //no need reference to timer cuz it already does stuff itself
        this.loadLevel(this.player.getLevelNumber());
    }

    public static double updateDelay() {
        return 1000.0 / Game.FPS;
    }

    
    public Player getPlayer() {
        return this.player;
    }

    public final void loadLevel(int levelNumber){
        this.enemies.clear();
        this.enemies.addAll(Arrays.asList(LEVELS[levelNumber-1].getEnemies()));
    }
    
    public void update() {//update frame
        if (this.player.isDead()) {
            System.out.println("YOU DIED!");
            System.exit(0);
        }
        this.player.update(l.getPressedKeys());
        this.checkPlayerProjectiles();
        Game.removeProjectiles(this.playerProjectiles);
        Game.updateProjectiles(this.playerProjectiles);
        //bad guys
        this.checkEnemyProjectiles();
        this.updateEnemies();
        this.removeEnemies();
        this.spawnEnemies();

        Game.removeProjectiles(this.enemyProjectiles);
        Game.updateProjectiles(this.enemyProjectiles);

        if (this.enemies.isEmpty()) {
            this.portal.show();
        }
        if (this.portal.isVisible() && this.portal.inPortal(this.player.getCenterX(), this.player.getCenterY())) {
            this.passThroughPortal();
        }

        repaint();
    }

    public void passThroughPortal() {
        this.enemies.clear();
        this.playerProjectiles.clear();
        this.enemyProjectiles.clear();

        this.player.incrementLevelNumber();
        this.loadLevel(this.player.getLevelNumber());
        this.portal.hide();
        this.player.setToStartCoords();
    }

    public ArrayList<Projectile> getPlayerProjectiles() {
        return this.playerProjectiles;
    }

    public ArrayList<Projectile> getEnemyProjectiles() {
        return this.enemyProjectiles;
    }
    
    public static void removeProjectiles(ArrayList<Projectile> projectiles) {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile currentProj = projectiles.get(i);
            if (currentProj.shouldKillSelf()) {
                if (currentProj.canSplit()) {
                    Game.addProjectiles(projectiles, currentProj.split());
                }
                projectiles.remove(i);
            }
            //if currentproj is colliding with enemy
        }
    }
    
    public static void addProjectiles(ArrayList<Projectile> projectiles, ArrayList<Projectile> newProjectiles) {
        projectiles.addAll(newProjectiles);
    }
    
    public static void updateProjectiles(ArrayList<Projectile> projectiles) {
        for (Projectile p : projectiles) {
            p.update();
        }
    }

    public void checkPlayerProjectiles() {
        for (int i = playerProjectiles.size() - 1; i >= 0; i--) {
            Projectile currentProj = playerProjectiles.get(i);
            for (Enemy e : enemies) {
                if (e.isHit(currentProj)) {
                    e.getDamaged(currentProj.getDamage());
                    if (currentProj.splitsOnImpact()) {
                        Game.addProjectiles(playerProjectiles, currentProj.split());
                    }
                    playerProjectiles.remove(i);
                    break;
                }
            }
            //if currentproj is colliding with enemy
        }
    }
    public void checkEnemyProjectiles() {
        for (int i = enemyProjectiles.size() - 1; i >= 0; i--) {
            Projectile currentProj = enemyProjectiles.get(i);
            if (this.player.isHit(currentProj)) {
                this.player.getDamaged(currentProj.getDamage());
                if (currentProj.splitsOnImpact()) {
                    Game.addProjectiles(enemyProjectiles, currentProj.split());
                }
                enemyProjectiles.remove(i);
            }
            //if currentproj is colliding with enemy
        }
    }
    
    public void updateEnemies() {
        for (Enemy e : this.enemies) {
            e.update(this.player.getCenterX(), this.player.getCenterY(), this.enemyProjectiles);
        }
    }

    public void removeEnemies() {
        for (int i = enemies.size() - 1; i >= 0; i--) {

            if (enemies.get(i).getHealth() <= 0) {

                enemies.remove(i);
            }
            //if currentproj is colliding with enemy
        }
    }
    
    public void spawnEnemies() {
        //when enemies spawn more enemies
        for (int i = enemies.size() - 1; i >= 0; i--) {

            if (enemies.get(i) instanceof canSpawn) {
                canSpawn spawnerEnemy = (canSpawn)(enemies.get(i));
                if (spawnerEnemy.spawnLoaded()) {
                    enemies.addAll(spawnerEnemy.spawn(this.player.getCenterX(), this.player.getCenterY()));
                }
            }
            //if currentproj is colliding with enemy
        }
    }


    public static void drawProjectiles(Graphics g, ArrayList<Projectile> projectiles) {
        for (Projectile p : projectiles) {
            p.draw(g);
        }
    }

    public void drawEnemies(Graphics g) {
        for (Enemy e : this.enemies) {
            e.draw(g);
        }
    }


    @Override
    public void paintComponent(Graphics g) {//draw stuff
        super.paintComponent(g);
        //draw player
        Game.drawProjectiles(g, this.playerProjectiles);
        Game.drawProjectiles(g, this.enemyProjectiles);

        this.player.draw(g);
        this.drawEnemies(g);

        this.portal.draw(g);
    }

    
    public static double getAngle(double startX, double startY, double targetX, double targetY) {
        //returns angle IN RADIANS
        if (startX == targetX) {
            //prevent division by 0
            return 0;
        }
        return Math.atan2(targetY - startY, targetX - startX);
    }

    public static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    public static double getDistance(double dx, double dy) {
        return getDistance(0, 0, dx, dy);
    }
    
    public static double getVectorX(double angle, double magnitude) {
        return Math.cos(angle) * magnitude;
    }

    public static double getVectorY(double angle, double magnitude) {
        return Math.sin(angle) * magnitude;
    }
}