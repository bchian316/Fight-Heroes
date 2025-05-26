import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public abstract class Entity implements canAttack, hasHealth, Drawable {
    //move static methods from Game into here
    private final String name;
    private double x, y;
    private int health;
    private final int size;
    private final int maxHealth;
    private final int speed;
    private final int reload;
    private int reloadTimer;
    private final AttackStats attk;


    private Image image;

    public Entity(String name, String addOn, int x, int y, int size, int health, int speed, int reload, AttackStats attk) {
        this.name = name;
        this.x = this.getCenterX();
        this.y = this.getCenterY();
        this.size = size;
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
        this.reload = reload;
        this.reloadTimer = (int) (Math.random() * this.reload);
        this.attk = attk;
        this.image = new ImageIcon("assets/" + addOn + "/" + this.name + ".png").getImage()
                .getScaledInstance(this.size, this.size, Image.SCALE_DEFAULT);
    }

    public Entity(String addOn, int x, int y, Mage mage) {
        //for Player
        this(mage.getName(), addOn, x, y, mage.getSize(), mage.getHealth(), mage.getSpeed(), mage.getReload(),
                mage.getAttackStats());
    }

    public void changeX(double change) {
        this.x += change;
    }

    public void changeY(double change) {
        this.y += change;
    }
    public void setX(double value) {
        this.x = value;
    }

    public void setY(double value) {
        this.y = value;
    }

    public int setBorders(Map map, double dx, double dy){
        //returns true if there is collision
        //this method sets the entity to a position where it is not colliding with walls (squares)
        int collisions = 0;
        this.x += dx;
        Tile xTile = map.returnWallCollided(this.getCenterX(), this.getCenterY(), this.size);
        if (xTile != null) {
            // the +-1 stops the weird glitch where a fast enemy keeps triggering a wall collision
            this.x = Game.getVectorX(Game.getAngle(0, 0, dx, dy), this.size/2.0);
            collisions++;
        }
        this.y += dy;
        Tile yTile = map.returnWallCollided(this.getCenterX(), this.getCenterY(), this.size);
        if (yTile != null) { //set y border
            this.x = Game.getVectorY(Game.getAngle(0, 0, dx, dy), this.size/2.0);
            collisions++;
        }
        return collisions; //if is 2, then the entity is stuck (cuz it is blocked on 2 sides)
        //if is one, then he only hit one wall and is 'sliding'
    }

    //for updating (movement)
    public int getSpeed() {
        return this.speed;
    }

    public void reload() {
        if (this.reloadTimer < this.reload) {
            this.reloadTimer += Game.updateDelay();
            if (this.reloadTimer > this.reload) {
                this.reloadTimer = this.reload;
            }
        }
    }

    public void load(){
        this.reloadTimer = this.reload;
    }
    
    //these two are for drawing health bars later
    public int getHealth() {
        return this.health;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public void fullHeal() {
        this.health = this.maxHealth;
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
    public void getDamaged(int damage) {
        this.health -= damage;
    }
    
    public AttackStats getAttackStats() {
        return this.attk;
    }

    @Override
    public boolean isLoaded() {
        return (this.reloadTimer >= this.reload);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.image, (int) this.x, (int) this.y, null);
        //draw health bars by overriding
    }

    public abstract ArrayList<Projectile> attack();

}