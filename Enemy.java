
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public abstract class Enemy implements hasHealth, canAttack, Drawable {
    private static final int BAROFFSETY = 5;

    private static final int HEALTHBARHEIGHT = 10;
    private static final Color HEALTHBAR_BGCOLOR = new Color(0, 0, 0);
    private static final Color HEALTHBAR_FGCOLOR = new Color(255, 0, 0); //display green over red

    private final String name;
    private double x, y;
    private int health;
    private final int size;
    private final int maxHealth;
    private final int speed;
    private final int reload;
    private int reloadTimer;
    private double moveTargetX, moveTargetY;
    private final AttackStats attk;

    private final int passiveRange; //should always stay within this range

    private final Image image;

    public Enemy(String name, int x, int y, int size, int health, int speed, int reload, int passiveRange, int passiveTime,
            AttackStats attk) {
        this.x = (double) (x - size / 2);
        this.y = (double) (y - size / 2);
        this.name = name;
        this.size = size;
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
        this.reload = reload;
        this.passiveRange = passiveRange;
        this.reloadTimer = (int) (Math.random() * this.reload);
        this.attk = attk;
        this.moveTargetX = this.getCenterX();
        this.moveTargetY = this.getCenterY();

        this.image = new ImageIcon("assets/monsters/" + this.name + ".png").getImage()
                .getScaledInstance(this.size, this.size, Image.SCALE_DEFAULT);
    }
    
    @Override
    public boolean isDead() {
        return this.health <= 0;
    }

    @Override
    public final double getCenterX() {
        return this.x + this.size / 2;
    }

    @Override
    public final double getCenterY() {
        return this.y + this.size / 2;
    }

    

    //so it can't be overridden
    private void setMoveTarget(double playerX, double playerY){
        double randAngle = Math.random() * Math.PI * 2; //in radians
        double randMagnitude = Math.random() * this.passiveRange;
        this.moveTargetX = (int) (Game.getVectorX(randAngle, randMagnitude) + playerX);
        this.moveTargetY = (int) (Game.getVectorY(randAngle, randMagnitude) + playerY);
    }

    public int getSize() {
        return this.size;
    }

    public int getSpeed() {
        return this.speed;
    }
    
    public Image getImage() {
        return this.image;
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
    public void drawHealthBar(Graphics g) {
        //call in draw method
        g.setColor(HEALTHBAR_BGCOLOR);
        g.fillRect((int) this.getCenterX() - this.size/2, (int) this.y + this.size + BAROFFSETY,
                this.size, HEALTHBARHEIGHT);
        g.setColor(HEALTHBAR_FGCOLOR);
        g.fillRect((int)this.getCenterX() - this.size/2, (int)this.y + this.size + BAROFFSETY, (int)(this.size * ((double)this.health/this.maxHealth)), HEALTHBARHEIGHT);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.getImage(), (int) this.x, (int) this.y, null);
        this.drawHealthBar(g);
    }

    @Override
    public abstract ArrayList<Projectile> attack(double targetX, double targetY); //spawn projectiles


    public void update(double playerX, double playerY, ArrayList<Projectile> enemyProjectiles, Tile[][] walls) {
        
        //move to target
        double dx = Game.getVectorX(
                Game.getAngle(this.getCenterX(), this.getCenterY(), this.moveTargetX, this.moveTargetY), this.speed);
        double dy = Game.getVectorY(
                Game.getAngle(this.getCenterX(), this.getCenterY(), this.moveTargetX, this.moveTargetY), this.speed);
        
        boolean collided = false;
        this.x += dx;
        Tile xTile = Game.returnWallCollided(walls, this.getCenterX(), this.getCenterY(), this.getSize());
        if (xTile != null) {
            if (dx < 0) {
                this.x = xTile.getX() + Tile.IMAGE_SIZE - Tile.COLLISION_CUSHION;
            } else if (dx > 0) {
                this.x = xTile.getX() - this.getSize() + Tile.COLLISION_CUSHION;
            }
            collided = true;
        }
        this.y += dy;
        Tile yTile = Game.returnWallCollided(walls, this.getCenterX(), this.getCenterY(), this.getSize());
        if (yTile != null) { //set y border
            if (dy < 0) {
                this.y = yTile.getY() + Tile.IMAGE_SIZE - Tile.COLLISION_CUSHION;
            } else if (dy > 0) {
                this.y = yTile.getY() - this.getSize() + Tile.COLLISION_CUSHION;
            }
            collided = true;
        }
        if (collided) {
            this.setMoveTarget(playerX, playerY);
        }
        //set targets
        if (Game.getDistance(this.getCenterX(), this.getCenterY(), this.moveTargetX, this.moveTargetY) < this.speed
                || Game.getDistance(this.moveTargetX, moveTargetY, playerX, playerY) > this.passiveRange) {
            //enemy has reached the spot or player has left the spot outside of passive range, find new spot
            this.setMoveTarget(playerX, playerY);
        }
                
                //reload and attack
        if (this.reloadTimer < this.reload) {
            this.reloadTimer += Game.updateDelay();
        }
        if (this.reloadTimer >= this.reload) {
            Game.addProjectiles(enemyProjectiles, this.attack(playerX, playerY));
            this.reloadTimer = 0;
        }

    }

    @Override
    public boolean isHit(Projectile p) {
        if (this.isDead()) {
            return false;
        }
        return Game.getDistance(p.getCenterX(), p.getCenterY(), this.getCenterX(), this.getCenterY()) <= (p.getSize() + this.size)/2.0;
    }
}