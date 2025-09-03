package game;

import enemies.Enemy;
import enemies.SpawnerEnemy;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import mages.*;

public class Game extends JPanel {
    private static final double FPS = 30.0;

    private static final Mage[] mages = { new DarkMage(), new EarthMage(), new ExplodyMage(),
            new FireMage(), new IceMage(), new LightMage(), new LightningMage(), new NatureMage(), new PlasmaMage(),
            new PulseMage(), new WaterMage(), new WaveMage(), new WindMage(), new CloudMage(), new DonovanMage()};
    

    private static final Font FONT = new Font("harrington", Font.BOLD, 30);
    private final Player player;

    private final Map map = new Map();
    
    private final Listener l = new Listener(this);
    private final ArrayList<Projectile> playerProjectiles = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Projectile> enemyProjectiles = new ArrayList<>();

    private final ArrayList<DamageCounter> damageCounters = new ArrayList<>();

    private final Portal portal = new Portal((GameRunner.SCREENWIDTH - Portal.SIZE) / 2, Tile.SIZE);

    private double offsetX, offsetY; //this is the game coordinates of the top left corner of the screen
    
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

        this.updateOffsets();

        this.loadLevel(this.player.getLevelNumber()); //make sure this is before the timer creation or else portal will appear while enemies is empty

        new Timer((int) (1000.0 / Game.FPS), e -> this.update()).start(); // Do action FPS times in one second
        //1000/FPS is the delay (time between each frame)
        //no need reference to timer cuz it already does stuff itself
        
        this.requestFocusInWindow();
    }

    public static double updateDelay() {
        return 1000.0 / Game.FPS;
    }


    public void attemptPlayerAttack(double targetX, double targetY) {
        //targetX and targetY are mouse coords, add offsets to get game coords
        if (this.player.isLoaded()) {
            Game.addProjectiles(this.playerProjectiles,
                    this.player.attack(targetX + this.offsetX, targetY + this.offsetY));
        }
    }

    public void attemptPlayerSpecial(double targetX, double targetY) {
        if (this.player.isSpecialLoaded()) {
            Game.addProjectiles(this.playerProjectiles,
                    this.player.special(targetX + this.offsetX, targetY + this.offsetY));
        }
    }

    private void loadLevel(int levelNumber) {
        //this.map.setMap(Level.LEVELS[levelNumber].getWalls());
        this.map.setMap("src/game/Map.txt");
        this.enemies.clear();
        this.enemies.addAll(Arrays.asList(Level.LEVELS[levelNumber].getEnemies()));
    }
    
    private void update() {//update frame
        //update includes removing
        if (this.player.isDead()) {
            System.out.println("YOU DIED!");
            System.exit(0);
        }

        this.updateOffsets();

        //update entities
        this.addDamageCounter(this.player.update(l.getPressedKeys(), this.map));
        this.updateEnemies(); //will add damage counters
        this.spawnEnemies();

        //check good guy projectiles
        this.checkProjectileCollision(this.enemies, this.playerProjectiles);
        Game.updateProjectiles(this.playerProjectiles, this.map);
        this.map.checkProjectiles(this.playerProjectiles); //hit walls

        //bad guy projectiles
        this.checkProjectileCollision(this.player, this.enemyProjectiles);
        Game.updateProjectiles(this.enemyProjectiles, this.map);
        this.map.checkProjectiles(this.enemyProjectiles); //hit walls

        this.updateDamageCounters();

        if (this.enemies.isEmpty() && this.player.getLevelNumber() < Level.LEVELS.length - 1) {
            this.portal.show();
        }
        if (this.portal.isVisible() && this.portal.inPortal(this.player.getCenterX(), this.player.getCenterY())) {
            this.passThroughPortal();
        }

        repaint();
    }
    
    private void updateOffsets() {
        this.offsetX = Math.min(Math.max(0, this.player.getCenterX() - GameRunner.SCREENWIDTH/2), Map.MAP_WIDTH-GameRunner.SCREENWIDTH);
        this.offsetY = Math.min(Math.max(0, this.player.getCenterY() - GameRunner.SCREENHEIGHT/2), Map.MAP_HEIGHT-GameRunner.SCREENHEIGHT);
    }
    
    
    private static void updateProjectiles(ArrayList<Projectile> projectiles, Map map) {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile currentProj = projectiles.get(i);
            if (currentProj.doneTraveling()) {
                if (currentProj.canSplit() && currentProj.splitsAtEnd()) { //this is for splitting without impact (Gene)
                    Game.addProjectiles(projectiles, currentProj.split());
                }
                projectiles.remove(i);
                continue;
            } else if (!map.inMap(currentProj.getCenterX(), currentProj.getCenterY())) {
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
            Enemy currentEnemy = enemies.get(i);
            if (currentEnemy.isDead() || !this.map.inMap(currentEnemy.getCenterX(), currentEnemy.getCenterY())) {
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
                    enemies.addAll(spawnerEnemy.spawn());
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
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GameRunner.SCREENWIDTH, GameRunner.SCREENHEIGHT);
        g.drawString("", 0, 0); //initialize the font

        this.map.draw(g, this.offsetX, this.offsetY);

        this.drawDrawables(g, this.playerProjectiles);
        this.drawDrawables(g, this.enemyProjectiles);

        this.player.draw(g, this.offsetX, this.offsetY);

        this.drawDrawables(g, this.enemies);

        this.portal.draw(g, this.offsetX, this.offsetY);

        this.drawDrawables(g, this.damageCounters);
    }
    
    private void drawDrawables(Graphics g, ArrayList<? extends Drawable> drawables) {
        for (Drawable d : drawables) {
            d.draw(g, this.offsetX, this.offsetY);
        }
    }

    private void passThroughPortal() {
        this.enemies.clear();
        this.playerProjectiles.clear();
        this.enemyProjectiles.clear();
        this.damageCounters.clear();

        this.player.incrementLevelNumber();
        this.loadLevel(this.player.getLevelNumber());
        this.portal.hide();
        this.player.setToStartCoords();
        this.player.fullHeal();
        this.player.load();
        this.player.loadSpecial();
        this.player.clearStatusEffects();
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

    public static double getRandomAngle() { //radians
        return Math.random() * Math.PI * 2;
    }

    public static Image[] imageLoader(String path, int size) {
        Image[] images;
        try {
            BufferedImage fullImage = ImageIO.read(new File(path));
            images = new Image[fullImage.getWidth() / fullImage.getHeight()];
            for (int i = 0; i < images.length; i++) {
                images[i] = fullImage.getSubimage(i * fullImage.getHeight(), 0, fullImage.getHeight(), fullImage.getHeight()).getScaledInstance(size, size, Image.SCALE_DEFAULT);
            }
        } catch (IOException ex) {
            images = new Image[0];
            System.out.println("io exception");
        }
        return images;
    }
}