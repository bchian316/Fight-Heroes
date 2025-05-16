
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public abstract class Enemy implements hasHealth, canAttack, Drawable, Moveable {
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

    private final Image image;

    public Enemy(String name, double x, double y, int size, int health, int speed, int reload,
            AttackStats attk) {
        this.x = x - size / 2;
        this.y = y - size / 2;
        this.name = name;
        this.size = size;
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
        this.reload = reload;
        this.reloadTimer = (int)(Math.random() * this.reload);
        this.attk = attk;
        this.moveTargetX = this.getCenterX();
        this.moveTargetY = this.getCenterY();

        this.image = new ImageIcon("assets/monsters/" + this.name + ".png").getImage()
                .getScaledInstance(this.size, this.size, Image.SCALE_DEFAULT);
    }
    
    public boolean isDead() {
        return this.health <= 0;
    }

    public final double getCenterX() {
        return this.x + this.size / 2;
    }

    public final double getCenterY() {
        return this.y + this.size / 2;
    }

    @Override
    public boolean setBorders(int borderX1, int borderY1, int borderX2, int borderY2) {
        if (this.x + this.size > borderX2) {
            this.x = borderX2 - this.size;
            return true;
        } else if (this.x < borderX1) {
            this.x = borderX1;
            return true;
        }
        if (this.y + this.size > borderY2) {
            this.y = borderY2 - this.size;
            return true;
        } else if (this.y < borderY1) {
            this.y = borderY1;
            return true;
        }
        return false;
    }

    //so it can't be overridden
    private void setMoveTarget(double playerX, double playerY) {
        double randAngle = Math.random() * Math.PI * 2; //in radians
        double randMagnitude = Math.random() * this.attk.range();
        this.moveTargetX = (int)(Game.getVectorX(randAngle, randMagnitude) + playerX);
        this.moveTargetY = (int)(Game.getVectorY(randAngle, randMagnitude) + playerY);
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
    public int getHealth() {
        return health;
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

    public void update(double playerX, double playerY, ArrayList<Projectile> enemyProjectiles, int borderX1, int borderY1, int borderX2, int borderY2) {
        if (Game.getDistance(this.getCenterX(), this.getCenterY(), this.moveTargetX, this.moveTargetY) < this.speed
            || Game.getDistance(playerX, playerY, this.moveTargetX, this.moveTargetY) > this.attk.range()) {
            //enemy has reached the spot or player has abandoned the spot, find new spot
            this.setMoveTarget(playerX, playerY);
        }
        //move to target
        this.x += Game.getVectorX(
                Game.getAngle(this.getCenterX(), this.getCenterY(), this.moveTargetX, this.moveTargetY), this.speed);
        this.y += Game.getVectorY(
                Game.getAngle(this.getCenterX(), this.getCenterY(), this.moveTargetX, this.moveTargetY), this.speed);

        if (this.setBorders(borderX1, borderY1, borderX2, borderY2)) {
            this.setMoveTarget(playerX, playerY);
        }
        
        if (this.reloadTimer < this.reload) {
            this.reloadTimer += Game.updateDelay();
        }
        if (this.reloadTimer >= this.reload) {
            Game.addProjectiles(enemyProjectiles, this.attack(playerX, playerY));
            this.reloadTimer = 0;
        }
    }

    public boolean isHit(Projectile p) {
        if (this.isDead()) {
            return false;
        }
        return Game.getDistance(p.getCenterX(), p.getCenterY(), this.getCenterX(), this.getCenterY()) <= (p.getSize() + this.size)/2.0;
    }
}