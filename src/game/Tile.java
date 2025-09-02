package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;

public class Tile implements Drawable, HasHealth {

    public static final int COLLISION_CUSHION = 8; //the wall has an up-to (exclusive) 8 pixel cushion for entities, not projectiles
    public static final int SIZE = 25;

    private static final Image[] WALL_IMAGES = Game.imageLoader("assets/terrain/wall.png", SIZE);
    
    private static final HashMap<Character, Image[]> TERRAIN_IMAGES = new HashMap<>();
    private static final Image[] GROUND_IMAGES = Game.imageLoader("assets/terrain/ground.png", SIZE);
    private static final Image[] BORDER_IMAGES = Game.imageLoader("assets/terrain/border.png", SIZE);
    private static final Image[] MUD_IMAGES = Game.imageLoader("assets/terrain/mud.png", SIZE);
    private static final Image[] WATER_IMAGES = Game.imageLoader("assets/terrain/water.png", SIZE);
    
    private static final HashMap<Character, StatusEffect> STATUS_EFFECTS = new HashMap<>();
    private static final StatusEffect MUD_EFFECT = new StatusEffect(null, 0, 0, 0, -0.6, 0, 0, new Color(138, 88, 8));
    private static final StatusEffect WATER_EFFECT = new StatusEffect(null, 0, 0, 0, -0.4, -1, 0, new Color(0, 121, 170));
    
    private static final int WALL_MAX_HEALTH = 240;
    private static final int WALL_HEALTH_INTERVAL = WALL_MAX_HEALTH / WALL_IMAGES.length;
    
    static {

        //load statuseffects
        STATUS_EFFECTS.put('x', null); //for no status effect
        STATUS_EFFECTS.put('u', null); //for wall
        STATUS_EFFECTS.put('m', MUD_EFFECT); //for mud
        STATUS_EFFECTS.put('w', WATER_EFFECT); //for water

        //load terrain images
        TERRAIN_IMAGES.put('x', GROUND_IMAGES);
        TERRAIN_IMAGES.put('u', BORDER_IMAGES);
        TERRAIN_IMAGES.put('m', MUD_IMAGES);
        TERRAIN_IMAGES.put('w', WATER_IMAGES);
    }

    private final int x, y; //should be the coords
    private int health;
    private final boolean breakable;
    private final char c;
    private Image image = null; //should only be border or ground, not wall

    private final StatusEffect statusEffect; //never update so it doesn't expire, never call updateAndExpire

    public Tile(int x, int y, int health, char c) {
        //char will determine the "terrain" such as border, mud, ground, not the wall health
        //any type of tile can have wall health
        this.x = x;
        this.y = y;
        this.health = health;
        this.breakable = c != 'u';
        this.c = c;
        this.statusEffect = STATUS_EFFECTS.get(c);
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
        return this.y + Tile.SIZE / 2;
    }

    public Color getColor() {
        if (this.statusEffect == null) {
            return new Color(0, 0, 0);//default black
        }
        return this.statusEffect.getColor();
    }

    public int getTickHealthChange() {
        if (this.statusEffect == null) {
            return 0;
        }
        return this.statusEffect.getTickHealthChange();
    }

    public double getRegenChange() {
        if (this.statusEffect == null) {
            return 0;
        }
        return this.statusEffect.getRegenChange();
    }

    public double getDefenseChange() {
        if (this.statusEffect == null) {
            return 0;
        }
        return this.statusEffect.getDefenseChange();
    }

    public double getSpeedChange() {
        if (this.statusEffect == null) {
            return 0;
        }
        return this.statusEffect.getSpeedChange();
    }

    public double getReloadChange() {
        if (this.statusEffect == null) {
            return 0;
        }
        return this.statusEffect.getReloadChange();
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
                g.drawImage(Tile.WALL_IMAGES[Tile.WALL_IMAGES.length
                        - ((this.health + Tile.WALL_HEALTH_INTERVAL - 1) / Tile.WALL_HEALTH_INTERVAL)],
                                (int)(this.x - offsetX), (int)(this.y - offsetY), null);
            }
        }
    }

    @Override
    public void drawHealthBar(Graphics g, double offsetX, double offsetY) {}
    
    public final void setImage() {
        if (!this.breakable) { //is a border
            this.image = randImage(BORDER_IMAGES);
            return;
        }
        this.image = randImage(TERRAIN_IMAGES.get(this.c));
    }

    private static Image randImage(Image[] images) {
        return images[(int) (Math.random() * images.length)];
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