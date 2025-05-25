import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.ImageIcon;

public class Player implements canAttack, Drawable, hasHealth {
    private static final int BAROFFSETY = 5;

    //health bar width is the size of the player
    private static final int BIGHEALTHBARHEIGHT = 15;
    private static final int HEALTHBARHEIGHT = 10;
    private static final Color HEALTHBAR_BGCOLOR = new Color(0, 0, 0);
    private static final Color HEALTHBAR_FGCOLOR = new Color(0, 255, 0); //display green over red
    private static final Color HEALTHBAR_REGENCOLOR = new Color(0, 150, 0); //display green over red


    private static final int RELOADBARWIDTH = 50;
    private static final int RELOADBARHEIGHT = 10;
    private static final Color RELOADBAR_BGCOLOR = new Color(80, 80, 80);
    private static final Color RELOADBAR_MGCOLOR = new Color(150, 150, 150);
    private static final Color RELOADBAR_FGCOLOR = new Color(190, 190, 0); //dark gray, light gray, then yellow


    private final Mage mage;
    private double x, y;
    private int health;
    private int levelNumber = 7;
    private double reloadTimer = 0; //player can shoot

    private double regenTimer = 0; //when to regen
    private static final double REGENTIME = 3000; //3secs for regen
    private double regenTickTimer = 0;
    private static final double REGENTICKTIME = 500;
    private boolean isHealing = false; //if regening
    

    private final Image image;

    public Player(Mage mage, double x, double y) {
        this.mage = mage;
        this.x = x - this.getSize() / 2;
        this.y = y - this.getSize() / 2;
        this.health = this.getHealth();

        this.image = new ImageIcon("assets/mages/" + this.mage.getName() + ".png").getImage()
                .getScaledInstance(this.getSize(), this.getSize(), Image.SCALE_DEFAULT);
    }

    private void heal() {
        if (this.isHealing){
            this.regenTickTimer += Game.updateDelay();
            if (this.regenTickTimer >= REGENTICKTIME) {
                regenTickTimer = 0;
                this.health += this.mage.getRegen();
                if (this.health > this.getHealth()) {
                    this.health = this.getHealth();
                }
            }   
        }
    }

    
    private void startHealing() {
        if (!this.isHealing){
            this.regenTimer += Game.updateDelay();
            if (this.regenTimer >= REGENTIME) {
                regenTimer = 0;
                this.isHealing = true;
            }   
        }
    }
    
    private void stopHealing() {
        this.isHealing = false;
        this.regenTimer = 0;
    }

    @Override
    public boolean isDead() {
        return this.health <= 0;
    }

    public void setToStartCoords() {
        this.x = Game.PLAYERSTARTX;
        this.y = Game.PLAYERSTARTY;
    }

    public int getLevelNumber() {
        return this.levelNumber;
    }
    
    public void incrementLevelNumber() {
        this.levelNumber++;
    }

    @Override
    public boolean isLoaded() {
        return (this.reloadTimer == this.mage.getReload());
    }

    public void resetReloadTimer() {
        this.reloadTimer = 0;
    }

    public void load() {//instantly load reload timer
        this.reloadTimer = this.mage.getReload();
    }

    public final int getHealth() {
        return this.mage.getHealth();
    }

    public final int getSize() {
        return this.mage.getSize();
    }

    public final int getSpeed() {
        return this.mage.getSpeed();
    }

    public void fullHeal() {
        this.health = this.getHealth();
    }
    
    @Override
    public void getDamaged(int damage) {
        this.stopHealing();
        this.health -= damage;
    }

    @Override
    public boolean isHit(Projectile p) {
        return Game.getDistance(p.getCenterX(), p.getCenterY(), this.getCenterX(),
                this.getCenterY()) <= (p.getSize() + this.getSize()) / 2.0;
    }

    @Override
    public double getCenterX() {
        return this.x + getSize() / 2.0;
    }

    @Override
    public double getCenterY() {
        return this.y + getSize() / 2.0;
    }

    private void reload() {
        
        this.reloadTimer += Game.updateDelay();
        if (this.reloadTimer > this.mage.getReload()) {
            reloadTimer = this.mage.getReload();
        }   
        
    }

    public void update(Set<Integer> pressedKeys, Tile[][] walls) {
        double dx = 0;
        double dy = 0;
        if (pressedKeys.contains(KeyEvent.VK_W)) {
            dy -= 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_A)) {
            dx -= 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_S)) {
            dy += 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_D)) {
            dx += 1;
        }
        double magnitude = Game.getDistance(dx, dy);
        if (magnitude != 0) {//prevent division by 0
            dx /= magnitude;
            dy /= magnitude;
            this.x += dx * this.getSpeed();
            Tile xTile = Game.returnWallCollided(walls, this.getCenterX(), this.getCenterY(), this.getSize());
            if (xTile != null) { //set x border
                if (dx < 0) {
                    this.x = xTile.getX() + Tile.IMAGE_SIZE - Tile.COLLISION_CUSHION;
                } else if (dx > 0) {
                    this.x = xTile.getX() - this.getSize() + Tile.COLLISION_CUSHION;
                }
            }
            this.y += dy * this.getSpeed();
            Tile yTile = Game.returnWallCollided(walls, this.getCenterX(), this.getCenterY(), this.getSize());
            if (yTile != null) { //set y border
                if (dy < 0) {
                    this.y = yTile.getY() + Tile.IMAGE_SIZE - Tile.COLLISION_CUSHION;
                } else if (dy > 0) {
                    this.y = yTile.getY() - this.getSize() + Tile.COLLISION_CUSHION;
                }
            }
        }

        this.reload();
        this.heal();
        this.startHealing();
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        //center x and center y
        this.stopHealing();
        return this.mage.createProjectiles(this.x + this.getSize()/2, this.y + this.getSize()/2, targetX, targetY);
    }
    @Override
    public void draw(Graphics g) {
        g.drawImage(this.image, (int) this.x, (int) this.y, null);
        this.drawReloadBar(g);
        this.drawHealthBar(g);
        this.drawBigHealthBar(g);
    }

    @Override
    public void drawHealthBar(Graphics g) {
        //call in draw method
        g.setColor(HEALTHBAR_BGCOLOR);
        g.fillRect((int) this.getCenterX() - this.getSize()/2, (int) this.y + this.getSize() + BAROFFSETY,
                this.getSize(), HEALTHBARHEIGHT);
        if (this.isHealing) {
            g.setColor(HEALTHBAR_REGENCOLOR);
            g.fillRect((int) this.getCenterX() - this.getSize()/2, (int) this.y + this.getSize() + BAROFFSETY,
                this.getSize(), HEALTHBARHEIGHT);
        }
        g.setColor(HEALTHBAR_FGCOLOR);
        g.fillRect((int)this.getCenterX() - this.getSize()/2, (int)this.y + this.getSize() + BAROFFSETY, (int)(this.getSize() * ((double)this.health/this.getHealth())), HEALTHBARHEIGHT);
    }


    private void drawBigHealthBar(Graphics g) {
        g.setColor(HEALTHBAR_BGCOLOR);
        g.fillRect(0, GameRunner.SCREENHEIGHT - GameRunner.HEIGHTOFFSET, GameRunner.SCREENWIDTH,
                Player.BIGHEALTHBARHEIGHT);
        if (this.isHealing) {
            g.setColor(HEALTHBAR_REGENCOLOR);
            g.fillRect(0, GameRunner.SCREENHEIGHT - GameRunner.HEIGHTOFFSET, GameRunner.SCREENWIDTH,
                Player.BIGHEALTHBARHEIGHT);
        }
        g.setColor(HEALTHBAR_FGCOLOR);
        g.fillRect(0, GameRunner.SCREENHEIGHT - GameRunner.HEIGHTOFFSET, (int)(GameRunner.SCREENWIDTH * ((double)this.health/this.getHealth())), Player.BIGHEALTHBARHEIGHT);
    }

    private void drawReloadBar(Graphics g) {
        g.setColor(Player.RELOADBAR_BGCOLOR);
        g.fillRect((int) this.getCenterX() - Player.RELOADBARWIDTH/2, (int) this.y - Player.RELOADBARHEIGHT - Player.BAROFFSETY,
                Player.RELOADBARWIDTH, Player.RELOADBARHEIGHT);
        g.setColor(Player.RELOADBAR_MGCOLOR);
        g.fillRect((int) this.getCenterX() - Player.RELOADBARWIDTH / 2, (int) this.y - Player.RELOADBARHEIGHT - Player.BAROFFSETY,
                (int)(Player.RELOADBARWIDTH * (this.reloadTimer / this.mage.getReload())), Player.HEALTHBARHEIGHT);
        if(this.isLoaded()){
            g.setColor(Player.RELOADBAR_FGCOLOR);
            g.fillRect((int) this.getCenterX() - Player.RELOADBARWIDTH/2, (int) this.y - Player.RELOADBARHEIGHT - Player.BAROFFSETY,
                Player.RELOADBARWIDTH, Player.RELOADBARHEIGHT);
        }
    }
}