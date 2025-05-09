import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel {
    public static final double FPS = 30.0;
    private final Listener l = new Listener(this);
    private final Player player = new Player(new WindMage(), 100, 100);
    private final ArrayList<Projectile> playerProjectiles = new ArrayList<>();
    
    public Game() {
        super();
        ActionListener action = evt -> this.update(); //create an action listener using lambda expression
        new Timer((int) (1000.0 / Game.FPS), action).start(); // Do action FPS times in one second
        //1000/FPS is the delay (time between each frame)
        //no need reference to timer cuz it already does stuff itself
    }

    public static double updateDelay() {
        return 1000.0 / Game.FPS;
    }

    
    public Player getPlayer() {
        return this.player;
    }
    
    public void update() {//update frame
        this.player.update(l.getPressedKeys());
        this.removeProjectiles();
        this.updateProjectiles();
        //delete bad bullets
        repaint();
    }
    
    public void removeProjectiles() {
        for (int i = this.playerProjectiles.size() - 1; i >= 0; i--) {
            if (this.playerProjectiles.get(i).shouldKillSelf()) {
                this.playerProjectiles.remove(i);
            }
        }
    }

    public void updateProjectiles() {
        for (Projectile p : this.playerProjectiles) {
            p.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {//draw stuff
        super.paintComponent(g);
        //draw player
        for (Projectile p : this.playerProjectiles) {
            p.draw(g);
        }
        this.player.draw(g);
    }

    public void addProjectiles(ArrayList<Projectile> playerProjectiles) {
        this.playerProjectiles.addAll(playerProjectiles);
    }
    
    public static double getAngle(double startX, double startY, double targetX, double targetY) {
        //returns angle IN RADIANS
        if (startX == targetX) {
            //prevent division by 0
            return 0;
        }
        return Math.atan2(targetY - startY, targetX - startX);
    }
}