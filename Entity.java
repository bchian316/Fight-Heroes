import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public abstract class Entity implements CanAttack, HasHealth, Drawable {
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
        //no going out of screen
        this.x = Math.max(Math.min(x - size/2, GameRunner.SCREENWIDTH - GameRunner.WIDTHOFFSET - size - Tile.SIZE), Tile.SIZE + size/2);
        this.y = Math.max(Math.min(y - size/2, GameRunner.SCREENHEIGHT - GameRunner.HEIGHTOFFSET - size - Tile.SIZE), Tile.SIZE + size/2);
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


    public int setBorders(Map map, double dx, double dy) {
        //returns 0 if no collision, 1 if x collision, 2 if y collision, and 3 if both
        //this implements moving also
        //returns 1 or 2 or 3 if there is collision
        //this method sets the entity to a position where it is not colliding with walls (squares)
        int collisions = 0;
        //should not be stuck in wall rn
        
        this.x += dx;

        Tile xTile = map.returnWallCollided(this.getCenterX(), this.getCenterY(), this.size);
        if (xTile != null) {
            //the vectorX expression sets the centerX to where it should be, subtract half of size to get the x (top left corner)
            /*double centerX = xTile.getX() + Tile.SIZE + Game.getVectorX(
                    Game.getAngle(xTile.getClosestX(this.getCenterX()), xTile.getClosestY(this.getCenterY()), this.getCenterX(), this.getCenterY()),
                    this.size / 2.0); //where the centerX should be
            System.out.print("CenterX:");
            System.out.println(centerX);
            this.x = centerX - this.size / 2.0; */
            this.x -= dx;
            collisions++;
        }
        
        this.y += dy;
        
        Tile yTile = map.returnWallCollided(this.getCenterX(), this.getCenterY(), this.size);
        if (yTile != null) { //set y border
            /*double centerY = yTile.getY() + Tile.SIZE + Game.getVectorY(
                    Game.getAngle(yTile.getClosestX(this.getCenterX()), yTile.getClosestY(this.getCenterY()), this.getCenterX(), this.getCenterY()),
                    this.size / 2.0); //where the centerY should be
            System.out.print("CenterY:");
            System.out.println(centerY);
            this.y = centerY - this.size/2.0; */
            this.y -= dy; //cancel movement
            collisions+=2;
        }

        //allocate for wall movement reduction
        if(xTile == null && yTile != null){
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

    public void load() {
        this.reloadTimer = this.reload;
    }

    public void unload() {
        this.reloadTimer = 0;
    }
    
    public double getReloadFraction() {
        return ((double) this.reloadTimer) / ((double) this.reload);
    }
    public double getHealthFraction() {
        return ((double) this.health) / ((double) this.maxHealth);
    }

    public void fullHeal() {
        this.health = this.maxHealth;
    }

    public void heal(int health) {
        //only adds health, not for regen purposes
        this.health += health;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
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

    @Override
    public abstract ArrayList<Projectile> attack(double targetX, double targetY);

    @Override
    public boolean isHit(Projectile p) {
        if (this.isDead()) {
            return false;
        }
        return Game.getDistance(p.getCenterX(), p.getCenterY(), this.getCenterX(), this.getCenterY()) < (p.getSize() + this.size)/2.0;
    }

}