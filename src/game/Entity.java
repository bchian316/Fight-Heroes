package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import mages.Mage;

public abstract class Entity implements CanAttack, HasHealth, Drawable {
    private static final int ANIMATION_TICK = 200;
    
    private static final int STATUS_EFFECT_CUSHION = 45; //distance between status effect centers (size is 30)
    private static final int STATUS_EFFECT_OFFSET = 20;
    //move static methods from Game into here
    private final String name;
    private double x, y;
    private int health;
    private final int size;
    private final int maxHealth;
    private final double speed;
    private final int reload;

    private Image[] images;
    private final Timer animationTimer = new Timer(ANIMATION_TICK, (int) (Math.random() * ANIMATION_TICK));
    private int animationIndex = 0;
    

    private final Timer reloadTimer;
    
    private final ArrayList<StatusEffect> statusEffects = new ArrayList<>();
    private Tile currentTile = new Tile(0, 0); //the tile the Entity is standing on
    
    public Entity(String name, String addOn, int x, int y, int size, int health, double speed, int reload) {
        this.name = name;
        //no going out of screen
        this.x = Math.max(Math.min(x - size/2, Map.MAP_WIDTH - size - Tile.SIZE), Tile.SIZE + size/2);
        this.y = Math.max(Math.min(y - size/2, Map.MAP_HEIGHT - size - Tile.SIZE), Tile.SIZE + size/2);
        this.size = size;
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
        this.reload = reload;
        this.reloadTimer = new Timer(this.reload, (int) (Math.random() * this.reload));
        try {
            BufferedImage fullImage = ImageIO.read(new File("assets/" + addOn + "/" + this.name + ".png"));
            this.images = new Image[fullImage.getWidth() / fullImage.getHeight()];
            for (int i = 0; i < this.images.length; i++) {
                this.images[i] = fullImage.getSubimage(i * fullImage.getHeight(), 0, fullImage.getHeight(), fullImage.getHeight()).getScaledInstance(this.size, this.size, Image.SCALE_DEFAULT);
            }
        } catch (IOException ex) {
            this.images = new Image[0];
            System.out.println("io exception");
        }
    }

    public Entity(String addOn, int x, int y, Mage mage) {
        //for Player
        this(mage.getName(), addOn, x, y, mage.getSize(), mage.getHealth(), mage.getSpeed(), mage.getReload());
    }

    private void incrementAnimationIndex() {
        this.animationIndex++;
        if (this.animationIndex >= this.images.length) {
            this.animationIndex = 0;
        }
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int getSize() {
        return this.size;
    }

    public void setX(double value) {
        this.x = value;
    }

    public void setY(double value) {
        this.y = value;
    }


    public int setBorders(Map map, double dx, double dy, boolean wallCollide) {
        //wallCollide is for wall restriction - if an enemy just spawned, don't do this so they can move through walls
        //returns 0 if no collision, 1 if x collision, 2 if y collision, and 3 if both
        //this implements moving also
        //returns 1 or 2 or 3 if there is collision
        //this method sets the entity to a position where it is not colliding with walls (squares)
        int collisions = 0;
        //should not be stuck in wall rn

        dx = dx + dx * this.speedChange();
        dy = dy + dy * this.speedChange();

        this.x += dx;

        Tile xTile = map.returnWallCollided(this.getCenterX(), this.getCenterY(), this.size);
        if (xTile != null || !map.inMap(this.getCenterX(), this.getCenterY())) {
            if (wallCollide) {
                this.x -= dx;
            }
            collisions++;
        }

        this.y += dy;

        Tile yTile = map.returnWallCollided(this.getCenterX(), this.getCenterY(), this.size);
        if (yTile != null || !map.inMap(this.getCenterX(), this.getCenterY())) {
            if (wallCollide) {
                this.y -= dy; //cancel movement
            }
            collisions += 2;
        }

        //allocate for wall movement reduction
        if (wallCollide) {
            if (xTile == null && yTile != null) {
                //collision on y but not x (left or right)
                if (dx > 0) {
                    this.x += this.speed - dx;
                } else if (dx < 0) {
                    this.x -= this.speed + dx;
                }
            } else if (xTile != null && yTile == null) {
                if (dy > 0) {
                    this.y += this.speed - dy;
                } else if (dy < 0) {
                    this.y -= this.speed + dy;
                }
            }
        }

        this.currentTile = map.returnTile(this.getCenterX(), this.getCenterY()); //reset currentTile

        return collisions; //if is 3, then the entity is stuck (cuz it is blocked on 2 sides)
        //if is 1 or 2, then he only hit one wall and is 'sliding'
    }

    
    //for updating (movement)
    public double getSpeed() {
        return this.speed;
    }

    public void reload() { //player uses this for reload reduction
        this.reloadTimer.update(this.reloadChange());
    }

    public void load() {
        this.reloadTimer.beDone();
    }

    public void unload() {
        this.reloadTimer.reset();
    }
    
    public double getReloadFraction() {
        return this.reloadTimer.doneFraction();
    }
    public double getHealthFraction() {
        return ((double) this.health) / ((double) this.maxHealth);
    }

    public void fullHeal() {
        this.health = this.maxHealth;
    }

    public void heal(int health) {
        if (health > 0) {
            //only adds health, not for regen purposes
            this.health += health + (int)(this.regenChange() * health);
            if (this.health > this.maxHealth) {
                this.health = this.maxHealth;
            }
        }
    }

    @Override
    public boolean isDead() {
        return this.health <= 0;
    }

    @Override
    public final double getCenterX() {
        return this.x + this.size / 2.0;
    }

    @Override
    public final double getCenterY() {
        return this.y + this.size / 2.0;
    }

    @Override
    public DamageCounter getDamaged(int damage, Color c) {
        //cannot heal when using this method
        int damageDealt = damage + (int) (this.defenseChange() * damage);
        if (damageDealt >= 0) { //max the damage change
            this.health -= damageDealt;
            double randAngle = Game.getRandomAngle();
            double randMagnitude = Math.random() * this.size / 2;
            return new DamageCounter(this.getCenterX() + Game.getVectorX(randAngle, randMagnitude),
                    this.getCenterY() + Game.getVectorY(randAngle, randMagnitude), damageDealt, c);
        }
        return null;
    }

    @Override
    public boolean isLoaded() {
        return this.reloadTimer.isDone();
    }

    @Override
    public void draw(Graphics g, double offsetX, double offsetY) {
        if(Drawable.inScreen(offsetX, offsetY, this.x, this.y, this.size, this.size)){
            g.drawImage(this.images[this.animationIndex], (int)(this.x - offsetX), (int)(this.y - offsetY), null);
            //draw health bars by overriding
            this.drawStatusEffects(g, offsetX, offsetY);
        }
    }

    @Override
    public abstract ArrayList<Projectile> attack(double targetX, double targetY);

    @Override
    public boolean isHit(Projectile p) {
        if (this.isDead()) {
            return false;
        }
        return Game.getDistance(p.getCenterX(), p.getCenterY(), this.getCenterX(),
                this.getCenterY()) < (p.getSize() + this.size) / 2.0;
    }

    //StatusEffect methods
    
    private void drawStatusEffects(Graphics g, double offsetX, double offsetY) {
        //for 1 item, we want 0
        //2 item: -0.5, 0.5
        //3 item: -1, 0, 1
        int index = 0;
        for (double i = -(this.statusEffects.size()-1)/2.0; i <= (this.statusEffects.size()-1)/2.0; i++) {
            this.statusEffects.get(index).drawIcon(g, (int)(this.getCenterX() + (i * STATUS_EFFECT_CUSHION) - offsetX),
                    (int)(this.getCenterY() - this.getSize()/2 - Entity.STATUS_EFFECT_OFFSET - offsetY));
            index++;
        }
    }

    public void addStatusEffect(StatusEffect e) {
        if (e == null) {
            return;
        }
        for (StatusEffect effect : this.statusEffects) {
            if(e.getName().equals(effect.getName())){
                effect.resetTimer(); //reset timer if inflicted with same effect
                return;
            }
        }
        this.statusEffects.add(e);
    }

    private void updateStatusEffects() { //for expiring status effects
        for (int i = this.statusEffects.size() - 1; i >= 0; i--) {
            if (this.statusEffects.get(i).updateAndExpire()) {
                this.statusEffects.remove(i);
            }
        }
    }

    public void clearStatusEffects() { //delete all status effects
        this.statusEffects.clear();
    }

    //statusEffect methods for accumulating total statuseffect impact
    
    private ArrayList<DamageCounter> tickHealthChange() {
        ArrayList<DamageCounter> damageCounters = new ArrayList<>();
        for (StatusEffect e : this.statusEffects) {
            int t = e.getTickHealthChange();
            if (t < 0) {
                damageCounters.add(this.getDamaged(-t, e.getColor()));
            } else if (t > 0) {
                this.heal(t);
            }
        }
        return damageCounters;
    }
    
    private double regenChange() {
        //will be used in the regenerate method
        double counter = 0;
        for (StatusEffect e : this.statusEffects) {
            counter += e.getRegenChange();
        }
        return counter;
    }

    private double defenseChange() { //will be a percentage
        double counter = 0;
        for (StatusEffect e : this.statusEffects) {
            counter += e.getDefenseChange();
        }
        return counter;
    }

    private double speedChange() { //will be a percentage, maximum 1
        //returns something like 0.8, 80% speed buff
        double counter = 0;
        for (StatusEffect e : this.statusEffects) {
            counter += e.getSpeedChange();
        }
        return counter;
    }

    private double reloadChange() { //will be a percentage, maximum 1
        double counter = 0;
        for (StatusEffect e : this.statusEffects) {
            counter += e.getReloadChange();
        }
        return counter;
    }

    public ArrayList<DamageCounter> update() {
        this.reload();
        this.updateStatusEffects();
        if (this.animationTimer.update()) {
            this.incrementAnimationIndex();
            this.animationTimer.reset();
        }
        
        return this.tickHealthChange();
    }
}