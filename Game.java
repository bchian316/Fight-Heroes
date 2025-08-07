import java.awt.Font;
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
    

    private static final Font FONT = new Font("harrington", Font.BOLD, 30);
    private final Player player;
    
    public final static int NUM_LEVELS = Level.LEVELS.length;

    private final Map map = new Map(GameRunner.SCREENWIDTH, GameRunner.SCREENHEIGHT);
    
    private final Listener l = new Listener(this);
    private final ArrayList<Projectile> playerProjectiles = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Projectile> enemyProjectiles = new ArrayList<>();

    private final ArrayList<DamageCounter> damageCounters = new ArrayList<>();

    private final Portal portal = new Portal((GameRunner.SCREENWIDTH - Portal.SIZE) / 2, Tile.SIZE);
    
    public Game() {
        super();

        this.setFont(FONT);
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
        
        this.requestFocusInWindow();

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
        }
    }

    public void attemptPlayerSpecial(double targetX, double targetY) {
        if (this.player.isSpecialLoaded()) {
            Game.addProjectiles(this.playerProjectiles,
                    this.player.special(targetX, targetY));
        }
    }

    private void loadLevel(int levelNumber) {
        this.map.setMap(Level.LEVELS[levelNumber].getWalls());
        this.enemies.clear();
        this.enemies.addAll(Arrays.asList(Level.LEVELS[levelNumber].getEnemies()));
    }
    
    private void update() {//update frame
        //update includes removing
        if (this.player.isDead()) {
            System.out.println("YOU DIED!");
            System.exit(0);
        }

        //update entities
        this.addDamageCounter(this.player.update(l.getPressedKeys(), this.map));
        this.updateEnemies(); //will add damage counters
        this.spawnEnemies();

        //check good guy projectiles
        this.checkProjectileCollision(this.enemies, this.playerProjectiles);
        Game.updateProjectiles(this.playerProjectiles);
        this.map.checkProjectiles(this.playerProjectiles); //hit walls
        
        //bad guy projectiles
        this.checkProjectileCollision(this.player, this.enemyProjectiles);
        Game.updateProjectiles(this.enemyProjectiles);
        this.map.checkProjectiles(this.enemyProjectiles); //hit walls

        this.updateDamageCounters();

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
        this.player.loadSpecial();
        this.player.clearStatusEffects();
    }

    
    private static void updateProjectiles(ArrayList<Projectile> projectiles) {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile currentProj = projectiles.get(i);
            if (currentProj.shouldKillSelf()) {
                if (currentProj.canSplit()) { //this is for splitting without impact (Gene)
                    Game.addProjectiles(projectiles, currentProj.split());
                }
                projectiles.remove(i);
                continue;
            }
            currentProj.update();
            //if currentproj is colliding with enemy
        }
    }
    
    public static void addProjectiles(ArrayList<Projectile> projectiles, ArrayList<Projectile> newProjectiles) {
        projectiles.addAll(newProjectiles);
    }

    private void checkProjectileCollision(ArrayList<? extends Entity> entities, ArrayList<Projectile> projectiles) {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile currentProj = projectiles.get(i);
            for (Entity e : entities) {
                if (e.isHit(currentProj) && !currentProj.damagedAlready(e)) {
                    this.addDamageCounter(e.getDamaged(currentProj.getDamage(), currentProj.getColor()));
                    e.addStatusEffect(currentProj.getStatusEffect());
                    currentProj.addHitEnemy(e);

                    if (currentProj.splitsOnImpact()) {
                        Game.addProjectiles(projectiles, currentProj.split());
                    }
                    if (currentProj.donePierce()) {//by piercing too many entities
                        projectiles.remove(i);
                        break;
                    }

                }
            }
        }
    }
    private void checkProjectileCollision(Entity e, ArrayList<Projectile> projectiles) {
        ArrayList<Entity> oneEntity = new ArrayList<>();
        oneEntity.add(e);
        checkProjectileCollision(oneEntity, projectiles);
    }

    private void updateEnemies() {
        for (int i = enemies.size() - 1; i >= 0; i--) {

            if (enemies.get(i).isDead()) {
                enemies.remove(i);
                continue;
            }
            this.addDamageCounter(enemies.get(i).update(this.player.getCenterX(), this.player.getCenterY(), this.enemyProjectiles, this.map));
            //if currentproj is colliding with enemy
        }
    }
    
    private void spawnEnemies() {
        //when enemies spawn more enemies
        for (int i = enemies.size() - 1; i >= 0; i--) {

            if (enemies.get(i) instanceof SpawnerEnemy) {
                SpawnerEnemy spawnerEnemy = (SpawnerEnemy) (enemies.get(i));
                if (spawnerEnemy.spawnLoaded()) {
                    enemies.addAll(spawnerEnemy.spawn(this.map));
                }
            }
            //if currentproj is colliding with enemy
        }
    }

    private void addDamageCounter(DamageCounter d) {
        if (d != null) {
            this.damageCounters.add(d);
        }
    }

    private void addDamageCounter(ArrayList<DamageCounter> d) {
        for (DamageCounter d_ : d) {
            this.addDamageCounter(d_);
        }
    }

    private void updateDamageCounters() {
        for (int i = this.damageCounters.size() - 1; i >= 0; i--) {
            if (this.damageCounters.get(i).lifetimeOver()) {
                this.damageCounters.remove(i);
                continue;
            }
            this.damageCounters.get(i).update();
        }
    }
    
    
    @Override
    public void paintComponent(Graphics g) {//draw stuff
        super.paintComponent(g);
        g.drawString("", 0, 0); //initialize the font
        
        this.map.draw(g);
        
        Game.drawProjectiles(g, this.playerProjectiles);
        Game.drawProjectiles(g, this.enemyProjectiles);
        
        this.player.draw(g);
        
        this.drawEnemies(g);

        this.portal.draw(g);

        this.drawDamageCounters(g);
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

    private void drawDamageCounters(Graphics g) {
        for (DamageCounter d : this.damageCounters) {
            d.draw(g);
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