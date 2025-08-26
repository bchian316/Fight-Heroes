package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;

public class Tile implements Drawable, HasHealth {
    //3 types of wall: ground, border, and wall
    public static final int COLLISION_CUSHION = 8; //the wall has an up-to (exclusive) 8 pixel cushion for entities, not projectiles

    public static final int NUM_GROUND_IMAGES = 6;
    public static final int NUM_BORDER_IMAGES = 4;
    public static final int NUM_WALL_IMAGES = 16;
    public static final int SIZE = 25;

    public static final int WALL_MAX_HEALTH = 240;
    public static final int WALL_HEALTH_INTERVAL = WALL_MAX_HEALTH / NUM_WALL_IMAGES;

    public static final Image[] GROUND_IMAGES = new Image[NUM_GROUND_IMAGES];
    public static final Image[] BORDER_IMAGES = new Image[NUM_BORDER_IMAGES];
    public static final Image[] WALL_IMAGES = new Image[NUM_WALL_IMAGES];

    private static final HashMap<Character, StatusEffect> STATUS_EFFECTS = new HashMap<>();
    private static final StatusEffect MUD_EFFECT = new StatusEffect("Slow", 0, 0, 0, -0.3, 0, 0, new Color(138, 88, 8));

    private final int x, y; //should be the coords
    private int health;
    private final boolean breakable;
    private Image image = null; //should only be border or ground, not wall

    private final StatusEffect statusEffect; //never update so it doesn't expire, never call updateAndExpire

    static {
        //load images
        for (int i = 0; i < Tile.NUM_BORDER_IMAGES; i++) {
            Tile.BORDER_IMAGES[i] = new ImageIcon("assets/terrain/border/" + Integer.toString(i + 1) + ".png").getImage()
                    .getScaledInstance(Tile.SIZE, Tile.SIZE, Image.SCALE_DEFAULT);
        }
        for (int i = 0; i < Tile.NUM_GROUND_IMAGES; i++) {
            Tile.GROUND_IMAGES[i] = new ImageIcon("assets/terrain/ground/" + Integer.toString(i + 1) + ".png")
                    .getImage()
                    .getScaledInstance(Tile.SIZE, Tile.SIZE, Image.SCALE_DEFAULT);
        }
        for (int i = 0; i < Tile.NUM_WALL_IMAGES; i++) {
            Tile.WALL_IMAGES[i] = new ImageIcon("assets/terrain/wall/" + Integer.toString(i + 1) + ".png").getImage()
                    .getScaledInstance(Tile.SIZE, Tile.SIZE, Image.SCALE_DEFAULT);
        }

        //load statuseffects
        STATUS_EFFECTS.put('x', null); //for no status effect
        STATUS_EFFECTS.put('m', MUD_EFFECT); //for mud
    }

    public Tile(int x, int y, int health) {
        this.x = x;
        this.y = y;
        if (health == -1) {
            this.health = 1;
            this.breakable = false;
        } else {
            this.health = health;
            this.breakable = true;
        }
        this.statusEffect = null;
        this.setImage();
    }

    public Tile(int x, int y, int health, char c) {
        this.x = x;
        this.y = y;
        if (health == -1) {
            this.health = 1;
            this.breakable = false;
        } else {
            this.health = health;
            this.breakable = true;
        }
        this.statusEffect = STATUS_EFFECTS.get(c);
        this.setImage();
    }

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.health = 1;
        this.breakable = false;
        this.statusEffect = null;
        this.setImage();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public double getCenterX() {
        return this.x + Tile.SIZE/2;
    }

    @Override
    public double getCenterY() {
        return this.y + Tile.SIZE/2;
    }

    public int getTickHealthChange(){
        return this.statusEffect.getTickHealthChange();
    }
    
    @Override
    public DamageCounter getDamaged(int damage, Color c) {//changes image too
        if (this.breakable && !this.isDead()) {
            this.health -= damage;
        }
        this.setImage();
        return null;
    }

    @Override
    public boolean isHit(Projectile p) {
        if (this.isDead()) {//because unbreakables have a health of 1, they will not be dead and will change image
            return false;
        }
        
        return this.circleCollided(p.getCenterX(), p.getCenterY(), p.getSize());
    }
    
    @Override
    public boolean isDead() {//also for if can be walked on
        return this.health <= 0;
    }

    @Override
    public void draw(Graphics g, double offsetX, double offsetY) {
        if (Drawable.inScreen(offsetX, offsetY, this.x, this.y, Tile.SIZE, Tile.SIZE)) {
            g.drawImage(this.image, (int)(this.x - offsetX), (int)(this.y - offsetY), null);
            if (!this.isDead() && this.breakable) {//is a wall
                g.drawImage(Tile.WALL_IMAGES[Tile.NUM_WALL_IMAGES
                        - ((this.health + Tile.WALL_HEALTH_INTERVAL - 1) / Tile.WALL_HEALTH_INTERVAL)],
                                (int)(this.x - offsetX), (int)(this.y - offsetY), null);
            }
        }
    }

    @Override
    public void drawHealthBar(Graphics g, double offsetX, double offsetY) {}
    
    public final void setImage() {
        if (!this.breakable) {
            this.image = Tile.BORDER_IMAGES[(int) (Math.random() * Tile.NUM_BORDER_IMAGES)];
            return;
        }
        this.image = Tile.GROUND_IMAGES[(int) (Math.random() * Tile.NUM_GROUND_IMAGES)];
    }

    private double getClosestX(double circleX) {
        double closestX;
        if (circleX > this.x && circleX < this.x + Tile.SIZE) {
            closestX = circleX;
        } else if (circleX <= this.x) {
            closestX = this.x;
        } else {
            closestX = this.x + Tile.SIZE;
        }
        return closestX;
    }
    private double getClosestY(double circleY) {
        double closestY;
        if (circleY > this.y && circleY < this.y + Tile.SIZE) {
            closestY = circleY;
        } else if (circleY <= this.y) {
            closestY = this.y;
        } else {
            closestY = this.y + Tile.SIZE;
        }
        return closestY;
    }

    public boolean circleCollided(double cX, double cY, double cSize) {
        return Game.getDistance(this.getClosestX(cX), this.getClosestY(cY), cX, cY) + Tile.COLLISION_CUSHION < cSize/2.0;
    }
}