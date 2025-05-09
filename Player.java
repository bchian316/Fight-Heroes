import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.ImageIcon;

public class Player implements canAttack, Drawable, hasHealth {
    private static final int BAROFFSETY = 5;

    private static final int HEALTHBARWIDTH = 50;
    private static final int HEALTHBARHEIGHT = 10;
    private static final Color HEALTHBAR_BGCOLOR = new Color(255, 0, 0);
    private static final Color HEALTHBAR_FGCOLOR = new Color(0, 255, 0); //display green over red

    private static final int RELOADBARWIDTH = 50;
    private static final int RELOADBARHEIGHT = 10;
    private static final Color RELOADBAR_BGCOLOR = new Color(80, 80, 80);
    private static final Color RELOADBAR_MGCOLOR = new Color(150, 150, 150);
    private static final Color RELOADBAR_FGCOLOR = new Color(190, 190, 0); //dark gray, light gray, then yellow


    private final PlayerType playerType;
    private double x, y;
    private int health;
    //private int level = 1;
    private double reloadTimer = 0; //player can shoot
    

    private final Image image;

    public Player(PlayerType playerType, int x, int y) {
        this.playerType = playerType;
        this.x = x;
        this.y = y;
        this.health = this.playerType.getMaxHealth();

        this.image = new ImageIcon("assets/" + this.playerType.getName() + ".png").getImage()
                .getScaledInstance(this.playerType.getSize(), this.playerType.getSize(), Image.SCALE_DEFAULT);

    }

    public boolean isLoaded() {
        return (this.reloadTimer == this.playerType.getReload());
    }

    public void resetReloadTimer() {
        this.reloadTimer = 0;
    }
    
    public void getDamaged(int damage) {
        this.health -= damage;
    }

    public double getX() {
        return this.x;
    }

    public double getCenterX() {
        return this.x + playerType.getSize()/2.0;
    }

    public double getY() {
        return this.y;
    }

    public double getCenterY() {
        return this.y + playerType.getSize()/2.0;
    }

    public Image getImage() {
        return this.image;
    }

    public void update(Set<Integer> pressedKeys) {
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
        double magnitude = Math.sqrt(dx * dx + dy * dy);
        if (magnitude != 0) {//prevent division by 0
            dx /= magnitude;
            dy /= magnitude;
            this.x += dx * this.playerType.getSpeed();
            this.y += dy * this.playerType.getSpeed();
            //now the player moves at 1 pixel (bc we divided by the distance, and distance/distance = 1)
            //multiply to get to moveSpeed
        }

        if (this.reloadTimer < this.playerType.getReload()) {
            this.reloadTimer += Game.updateDelay();
            if (this.reloadTimer > this.playerType.getReload()) {
                this.reloadTimer = this.playerType.getReload();
            }
        }
    }
    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        return this.playerType.createProjectiles(this.x, this.y, targetX, targetY);
    }

    @Override
    public void drawHealthBar(Graphics g) {
        //call in draw method
        g.setColor(Player.HEALTHBAR_BGCOLOR);
        g.fillRect((int) this.getCenterX() - Player.HEALTHBARWIDTH/2, (int) this.y + this.playerType.getSize() + Player.BAROFFSETY,
                Player.HEALTHBARWIDTH, Player.HEALTHBARHEIGHT);
        g.setColor(Player.HEALTHBAR_FGCOLOR);
        g.fillRect((int)this.getCenterX() - Player.HEALTHBARWIDTH/2, (int)this.y + this.playerType.getSize() + Player.BAROFFSETY, Player.HEALTHBARWIDTH * (this.health/this.playerType.getMaxHealth()), Player.HEALTHBARHEIGHT);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.image, (int) this.x, (int) this.y, null);
        this.drawReloadBar(g);
        this.drawHealthBar(g);
    }

    public void drawReloadBar(Graphics g) {
        g.setColor(Player.RELOADBAR_BGCOLOR);
        g.fillRect((int) this.getCenterX() - Player.RELOADBARWIDTH/2, (int) this.y - Player.RELOADBARHEIGHT - Player.BAROFFSETY,
                Player.RELOADBARWIDTH, Player.RELOADBARHEIGHT);
        g.setColor(Player.RELOADBAR_MGCOLOR);
        g.fillRect((int) this.getCenterX() - Player.RELOADBARWIDTH / 2, (int) this.y - Player.RELOADBARHEIGHT - Player.BAROFFSETY,
                (int)(Player.RELOADBARWIDTH * (this.reloadTimer / this.playerType.getReload())), Player.HEALTHBARHEIGHT);
        if(this.isLoaded()){
            g.setColor(Player.RELOADBAR_FGCOLOR);
            g.fillRect((int) this.getCenterX() - Player.RELOADBARWIDTH/2, (int) this.y - Player.RELOADBARHEIGHT - Player.BAROFFSETY,
                Player.RELOADBARWIDTH, Player.RELOADBARHEIGHT);
        }
    }
}