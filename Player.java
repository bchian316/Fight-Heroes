import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.ImageIcon;

public class Player implements canAttack, Drawable{
    private final PlayerType playerType;
    private double x, y;
    private int health;
    private int level = 1;
    private int reloadTimer = 0; //player can shoot
    

    private final Image image;

    public Player(PlayerType playerType, int x, int y) {
        this.playerType = playerType;
        this.x = x;
        this.y = y;
        this.health = this.playerType.getMaxHealth();

        
        this.image = new ImageIcon("assets/Fire Mage.png").getImage().getScaledInstance(this.playerType.getSize(), this.playerType.getSize(), Image.SCALE_DEFAULT);     
        
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
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
            this.reloadTimer++;
        }
    }
    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        return this.playerType.createProjectiles(this.x, this.y, targetX, targetY);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.image, (int) this.x, (int) this.y, null);
    }
}