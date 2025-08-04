import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel {
    public static final double FPS = 30.0;
    public static final Mage[] mages = { new DarkMage(), new EarthMage(), new ExplodyMage(),
            new FireMage(), new IceMage(), new LightMage(), new LightningMage(), new NatureMage(), new PlasmaMage(),
            new PulseMage(), new WaterMage(), new WaveMage(), new WindMage(), new CloudMage(), new DonovanMage()};
    private final Player player;
    
    public final static int NUM_LEVELS = Level.LEVELS.length;

    private final Map map = new Map(GameRunner.SCREENWIDTH, GameRunner.SCREENHEIGHT);
    
    private final Listener l = new Listener(this);
    private final ArrayList<Projectile> playerProjectiles = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Projectile> enemyProjectiles = new ArrayList<>();

    private final Portal portal = new Portal((GameRunner.SCREENWIDTH - Portal.SIZE) / 2, Tile.SIZE);
    
    public Game() {
        super();
        this.addMouseListener(l);
        this.addKeyListener(l);
        //choose player
        for (int i = 0; i < Game.mages.length - 1; i++) {
            System.out.println(i + ". " + Game.mages[i].toString());
        }
        System.out.println("Choose class (0-13):");
        try (Scanner scanner = new Scanner(System.in)) {
            this.player = new Player(Game.mages[scanner.nextInt()]);
        }

        new Timer((int) (1000.0 / Game.FPS), _ -> this.update()).start(); // Do action FPS times in one second
        //1000/FPS is the delay (time between each frame)
        //no need reference to timer cuz it already does stuff itself
        
        this.loadLevel(this.player.getLevelNumber());
        this.requestFocusInWindow();
    }

    public static double updateDelay() {
        return 1000.0 / Game.FPS;
    }


    public void attemptPlayerAttack(double targetX, double targetY) {
        if (this.player.isLoaded()) {
            Game.addProjectiles(this.playerProjectiles,
                    this.player.attack(targetX, targetY));
            this.player.unload();
        }
    }

    private void loadLevel(int levelNumber) {
        this.map.setMap(Level.LEVELS[levelNumber].getWalls());
        this.enemies.clear();
        this.enemies.addAll(Arrays.asList(Level.LEVELS[levelNumber].getEnemies()));
    }
    
    private void update() {//update frame
        if (this.player.isDead()) {
            System.out.println("YOU DIED!");
            System.exit(0);
        }
        //will not need this line later once we implement wall physics
        this.player.update(l.getPressedKeys(), this.map);
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

        //check if projectiles hit walls
        this.map.checkProjectiles(this.enemyProjectiles);
        this.map.checkProjectiles(this.playerProjectiles);

        if (this.enemies.isEmpty() && this.player.getLevelNumber() < Level.LEVELS.length-1) {
            this.portal.show();
        }
        if (this.portal.isVisible() && this.portal.inPortal(this.player.getCenterX(), this.player.getCenterY())) {
            this.passThroughPortal();
        }

        repaint();
    }

    private void passThroughPortal() {
        this.enemies.clear();
        this.playerProjectiles.clear();
        this.enemyProjectiles.clear();

        this.player.incrementLevelNumber();
        this.loadLevel(this.player.getLevelNumber());
        this.portal.hide();
        this.player.setToStartCoords();
        this.player.fullHeal();
        this.player.load();
        this.player.clearStatusEffects();
    }

    
    private static void removeProjectiles(ArrayList<Projectile> projectiles) {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile currentProj = projectiles.get(i);
            if (currentProj.shouldKillSelf()) {
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
    
    private static void updateProjectiles(ArrayList<Projectile> projectiles) {
        for (Projectile p : projectiles) {
            p.update();
        }
    }

    private void checkPlayerProjectiles() {//if player projectiles hit enemy
        for (int i = this.playerProjectiles.size() - 1; i >= 0; i--) {
            Projectile currentProj = this.playerProjectiles.get(i);
            for (Enemy e : enemies) {
                if (e.isHit(currentProj) && !currentProj.damagedAlready(e)) {
                    e.getDamaged(currentProj.getDamage());
                    currentProj.addHitEnemy(e);
                    if (currentProj.splitsOnImpact()) {
                        Game.addProjectiles(this.playerProjectiles, currentProj.split());
                    }
                    if (currentProj.donePierce()) {//by piercing too many enemies
                        this.playerProjectiles.remove(i);
                        break;
                    }
                }
            }
            //if currentproj is colliding with enemy
        }
    }
    private void checkEnemyProjectiles() { //if enemy projectiles hit player
        for (int i = this.enemyProjectiles.size() - 1; i >= 0; i--) {
            Projectile currentProj = this.enemyProjectiles.get(i);
            if (this.player.isHit(currentProj) && !currentProj.damagedAlready(this.player)) {
                this.player.getDamaged(currentProj.getDamage());
                this.player.addStatusEffect(currentProj.getStatusEffect());
                currentProj.addHitEnemy(player);
                if (currentProj.splitsOnImpact()) {
                    Game.addProjectiles(this.enemyProjectiles, currentProj.split());
                }
                if (currentProj.donePierce()) {//pierce too many good guys
                    this.enemyProjectiles.remove(i);
                    break;
                }
            }
            //if currentproj is colliding with enemy
        }
    }
    
    private void updateEnemies() {
        for (Enemy e : this.enemies) {
            e.update(this.player.getCenterX(), this.player.getCenterY(), this.enemyProjectiles, this.map);
        }
    }

    private void removeEnemies() {
        for (int i = enemies.size() - 1; i >= 0; i--) {

            if (enemies.get(i).isDead()) {

                enemies.remove(i);
            }
            //if currentproj is colliding with enemy
        }
    }
    
    private void spawnEnemies() {
        //when enemies spawn more enemies
        for (int i = enemies.size() - 1; i >= 0; i--) {

            if (enemies.get(i) instanceof SpawnerEnemy) {
                SpawnerEnemy spawnerEnemy = (SpawnerEnemy)(enemies.get(i));
                if (spawnerEnemy.spawnLoaded()) {
                    enemies.addAll(spawnerEnemy.spawn(this.map));
                }
            }
            //if currentproj is colliding with enemy
        }
    }


    
    
    @Override
    public void paintComponent(Graphics g) {//draw stuff
        super.paintComponent(g);
        
        this.map.draw(g);
        
        Game.drawProjectiles(g, this.playerProjectiles);
        Game.drawProjectiles(g, this.enemyProjectiles);
        
        this.player.draw(g);
        
        this.drawEnemies(g);

        this.portal.draw(g);
    }
    private static void drawProjectiles(Graphics g, ArrayList<Projectile> projectiles) {
        for (Projectile p : projectiles) {
            p.draw(g);
        }
    }

    private void drawEnemies(Graphics g) {
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