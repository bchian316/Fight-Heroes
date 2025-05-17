import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel {
    public static final double FPS = 30.0;
    public static final double PLAYERSTARTX = ((double)GameRunner.SCREENWIDTH)/2;
    public static final double PLAYERSTARTY = GameRunner.SCREENHEIGHT-150;
    private final Player player = new Player(new DarkMage(), PLAYERSTARTX, PLAYERSTARTY);
    
    public static final Level[] LEVELS = {
            new Level(new Enemy[] { new Wight(50.0, 50.0)}),
            new Level(new Enemy[] { new Zombie(50.0, 50.0),
                                    new Zombie(250.0, 50.0),
                                    new Zombie(550.0, 50.0)}),
            new Level(new Enemy[] { new Zombie(400.0, 200.0),
                                    new Skeleton(200.0, 150.0),
                                    new Skeleton(300.0, 150.0) }),
            new Level(new Enemy[] { new Zombie(150.0, 250.0),
                                    new Zombie(350.0, 250.0),
                                    new Ghost(200.0, 150.0),
                                    new Ghost(700, 300),
                                    new Skeleton(100, 300) }),
            new Level(new Enemy[] { new Mummy(300.0, 100.0),
                                    new Ghost(100, 100),
                                    new Skeleton(100.0, 100.0),
                                    new Skeleton(700.0, 100.0)}),
            new Level(new Enemy[] { new Ghost(350.0, 100.0),
                                    new ZombieHut(100.0, 150.0),
                                    new Zombie(200.0, 150.0),                       
                                    new Skeleton(700, 300) }),
            new Level(new Enemy[] { new Frankenstein(250.0, 200.0),
                                    new ZombieHut(700.0, 150.0),
                                    new Mummy(250, 100),
                                    new Mummy(550, 100) }),
            new Level(new Enemy[] { new Vampire(350.0, 50.0),
                                    new Skeleton(500.0, 250.0),
                                    new Mummy(400.0, 250.0),
                                    new Zombie(500.0, 150.0),
                                    new Zombie(700.0, 150.0)}),
            new Level(new Enemy[] { new Ghost(350.0, 50.0),
                                    new Ghost(450.0, 50.0),
                                    new ZombieHut(300.0, 50.0),
                                    new Skeleton(500.0, 350.0),
                                    new Vampire(300.0, 50.0),
                                    new Ghost(400.0, 200.0)}),
            new Level(new Enemy[] { new Ghost(550.0, 150.0),
                                    new Ghost(250.0, 250.0),
                                    new Vampire(400.0, 50.0),
                                    new Zombie(700.0, 150.0),                        
                                    new ZombieHut(200, 300),
                                    new Skeleton(300.0, 100.0),
                                    new Mummy(300.0, 100.0)}),
            new Level(new Enemy[] { new Ghost(0.0, 550.0),
                                    new Zombie(100.0, 250.0),
                                    new Zombie(550.0, 50.0),                        
                                    new SkeletonShaman(200, 300),
                                    new Mummy(300.0, 100.0) }),
            new Level(new Enemy[] { new Mummy(500.0, 350.0),
                                    new Mummy(300.0, 350.0),
                                    new Zombie(700.0, 150.0),                 
                                    new SkeletonShaman(200, 300)}),
            new Level(new Enemy[] { new Mummy(400.0, 150.0),
                                    new ZombieHut(200.0, 650.0),
                                    new Zombie(600.0, 50.0),
                                    new SkeletonShaman(200, 300) }),
            new Level(new Enemy[] { new ZombieHut(500.0, 350.0),
                                    new Mummy(300.0, 350.0),
                                    new ZombieHut(700.0, 150.0),                 
                                    new Vampire(500, 100)}),
            new Level(new Enemy[] { new SkeletonShaman(500.0, 350.0),
                                    new ZombieHut(600.0, 100.0),
                                    new Mummy(200.0, 350.0),
                                    new Ghost(50.0, 50.0),
                                    new Ghost(750.0, 50.0),
                                    new Ghoul(500.0, 200.0) }),
            new Level(new Enemy[] { new Ben(400.0, 100.0)})
    };
    private final Background bg = new Background(GameRunner.SCREENWIDTH, GameRunner.SCREENHEIGHT);
    
    private final Listener l = new Listener(this);
    private final ArrayList<Projectile> playerProjectiles = new ArrayList<>();

    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Projectile> enemyProjectiles = new ArrayList<>();

    private final Portal portal = new Portal((GameRunner.SCREENWIDTH - Portal.SIZE) / 2, Background.WALLIMAGESIZE);
    
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


    public void attemptPlayerAttack(double targetX, double targetY) {
        if (this.player.isLoaded()) {
            Game.addProjectiles(this.getPlayerProjectiles(),
                    this.player.attack(targetX, targetY));
            this.player.resetReloadTimer();
        }
    }

    public final void loadLevel(int levelNumber) {
        this.bg.setBackground();
        this.enemies.clear();
        this.enemies.addAll(Arrays.asList(LEVELS[levelNumber-1].getEnemies()));
    }
    
    public void update() {//update frame
        if (this.player.isDead()) {
            System.out.println("YOU DIED!");
            System.exit(0);
        }
        this.player.update(l.getPressedKeys(), Background.WALLIMAGESIZE, Background.WALLIMAGESIZE, this.bg.getWallX(), this.bg.getWallY());
        this.checkPlayerProjectiles();
        Game.removeProjectiles(this.playerProjectiles, this.bg);
        Game.updateProjectiles(this.playerProjectiles);
        //bad guys
        this.checkEnemyProjectiles();
        this.updateEnemies();
        this.removeEnemies();
        this.spawnEnemies();

        Game.removeProjectiles(this.enemyProjectiles, this.bg);
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
        this.player.fullHeal();
        this.player.load();
    }

    public ArrayList<Projectile> getPlayerProjectiles() {
        return this.playerProjectiles;
    }

    public ArrayList<Projectile> getEnemyProjectiles() {
        return this.enemyProjectiles;
    }
    
    public static void removeProjectiles(ArrayList<Projectile> projectiles, Background bg) {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile currentProj = projectiles.get(i);
            if (currentProj.shouldKillSelf(bg)) {
                if (currentProj.canSplit()) { //this is for splitting without impact (Gene)
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

    public void checkPlayerProjectiles() {//removes player projectiles
        for (int i = playerProjectiles.size() - 1; i >= 0; i--) {
            Projectile currentProj = playerProjectiles.get(i);
            for (Enemy e : enemies) {
                if (e.isHit(currentProj) && !currentProj.damagedAlready(e)) {
                    e.getDamaged(currentProj.getDamage());
                    currentProj.addHitEnemy(e);
                    if (currentProj.splitsOnImpact()) {
                        Game.addProjectiles(playerProjectiles, currentProj.split());
                    }
                    if (currentProj.donePierce()) {//by piercing too many enemies
                        playerProjectiles.remove(i);
                        break;
                    }
                }
            }
            //if currentproj is colliding with enemy
        }
    }
    public void checkEnemyProjectiles() {
        for (int i = enemyProjectiles.size() - 1; i >= 0; i--) {
            Projectile currentProj = enemyProjectiles.get(i);
            if (this.player.isHit(currentProj) && !currentProj.damagedAlready(this.player)) {
                this.player.getDamaged(currentProj.getDamage());
                currentProj.addHitEnemy(player);
                if (currentProj.splitsOnImpact()) {
                    Game.addProjectiles(enemyProjectiles, currentProj.split());
                }
                if (currentProj.donePierce()) {//pierce too many good guys
                    enemyProjectiles.remove(i);
                    break;
                }
            }
            //if currentproj is colliding with enemy
        }
    }
    
    public void updateEnemies() {
        for (Enemy e : this.enemies) {
            e.update(this.player.getCenterX(), this.player.getCenterY(), this.enemyProjectiles, Background.WALLIMAGESIZE, Background.WALLIMAGESIZE, this.bg.getWallX(), this.bg.getWallY());
        }
    }

    public void removeEnemies() {
        for (int i = enemies.size() - 1; i >= 0; i--) {

            if (enemies.get(i).isDead()) {

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


    
    
    @Override
    public void paintComponent(Graphics g) {//draw stuff
        super.paintComponent(g);
        
        this.bg.draw(g);
        
        Game.drawProjectiles(g, this.playerProjectiles);
        Game.drawProjectiles(g, this.enemyProjectiles);
        
        this.player.draw(g);
        
        this.drawEnemies(g);

        this.portal.draw(g);
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