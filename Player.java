import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.ImageIcon;

public class Player implements canAttack, Drawable, hasHealth, Moveable {
    private static final int BAROFFSETY = 5;

    //health bar width is the size of the player
    private static final int BIGHEALTHBARHEIGHT = 15;
    private static final int HEALTHBARHEIGHT = 10;
    private static final Color HEALTHBAR_BGCOLOR = new Color(0, 0, 0);
    private static final Color HEALTHBAR_FGCOLOR = new Color(0, 255, 0); //display green over red

    private static final int RELOADBARWIDTH = 50;
    private static final int RELOADBARHEIGHT = 10;
    private static final Color RELOADBAR_BGCOLOR = new Color(80, 80, 80);
    private static final Color RELOADBAR_MGCOLOR = new Color(150, 150, 150);
    private static final Color RELOADBAR_FGCOLOR = new Color(190, 190, 0); //dark gray, light gray, then yellow


    private final Mage mage;
    private double x, y;
    private int health;
    private int levelNumber = 1;
    private double reloadTimer = 0; //player can shoot
    

    private final Image image;

    public Player(Mage mage, double x, double y) {
        this.mage = mage;
        this.x = x - this.mage.getSize() / 2;
        this.y = y - this.mage.getSize() / 2;
        this.health = this.mage.getMaxHealth();

        this.image = new ImageIcon("assets/mages/" + this.mage.getName() + ".png").getImage()
                .getScaledInstance(this.mage.getSize(), this.mage.getSize(), Image.SCALE_DEFAULT);
    }

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

    public int getSize() {
        return this.mage.getSize();
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    public void fullHeal() {
        this.health = this.mage.getMaxHealth();
    }
    
    @Override
    public void getDamaged(int damage) {
        this.health -= damage;
    }

    public boolean isHit(Projectile p) {
        return Game.getDistance(p.getCenterX(), p.getCenterY(), this.getCenterX(), this.getCenterY()) <= (p.getSize() + this.mage.getSize())/2.0;
    }

    public double getX() {
        return this.x;
    }

    public double getCenterX() {
        return this.x + mage.getSize()/2.0;
    }

    public double getY() {
        return this.y;
    }

    public double getCenterY() {
        return this.y + mage.getSize()/2.0;
    }

    public void update(Set<Integer> pressedKeys, int borderX1, int borderY1, int borderX2, int borderY2) {
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
            this.x += dx * this.mage.getSpeed();
            this.y += dy * this.mage.getSpeed();
            this.setBorders(borderX1, borderY1, borderX2, borderY2);
            //now the player moves at 1 pixel (bc we divided by the distance, and distance/distance = 1)
            //multiply to get to moveSpeed
        }

        if (this.reloadTimer < this.mage.getReload()) {
            this.reloadTimer += Game.updateDelay();
            if (this.reloadTimer > this.mage.getReload()) {
                this.reloadTimer = this.mage.getReload();
            }
        }
    }

    @Override
    public boolean setBorders(int borderX1, int borderY1, int borderX2, int borderY2) {
        if (this.x + this.mage.getSize() > borderX2) {
            this.x = borderX2 - this.mage.getSize();
            return true;
        } else if (this.x < borderX1) {
            this.x = borderX1;
            return true;
        }
        if (this.y + this.mage.getSize() > borderY2) {
            this.y = borderY2 - this.mage.getSize();
            return true;
        } else if (this.y < borderY1) {
            this.y = borderY1;
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        //center x and center y
        return this.mage.createProjectiles(this.x + this.mage.getSize()/2, this.y + this.mage.getSize()/2, targetX, targetY);
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
        g.fillRect((int) this.getCenterX() - this.mage.getSize()/2, (int) this.y + this.mage.getSize() + BAROFFSETY,
                this.mage.getSize(), HEALTHBARHEIGHT);
        g.setColor(HEALTHBAR_FGCOLOR);
        g.fillRect((int)this.getCenterX() - this.mage.getSize()/2, (int)this.y + this.mage.getSize() + BAROFFSETY, (int)(this.mage.getSize() * ((double)this.health/this.mage.getMaxHealth())), HEALTHBARHEIGHT);
    }


    public void drawBigHealthBar(Graphics g) {
        g.setColor(HEALTHBAR_BGCOLOR);
        g.fillRect(0, GameRunner.SCREENHEIGHT - GameRunner.HEIGHTOFFSET, GameRunner.SCREENWIDTH, Player.BIGHEALTHBARHEIGHT);
        g.setColor(HEALTHBAR_FGCOLOR);
        g.fillRect(0, GameRunner.SCREENHEIGHT - GameRunner.HEIGHTOFFSET, (int)(GameRunner.SCREENWIDTH * ((double)this.health/this.mage.getMaxHealth())), Player.BIGHEALTHBARHEIGHT);
    }

    public void drawReloadBar(Graphics g) {
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