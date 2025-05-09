import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel {
    public static final double FPS = 30.0;
    private final Listener l = new Listener(this);
    private final Player player = new Player(new FireMage(), 100, 100);
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    
    public Game() {
        super();
        ActionListener action = evt -> this.update(); //create an action listener using lambda expression
        new Timer((int)(1000.0/Game.FPS), action).start(); // Do action every 30 ms
        //no need reference to timer cuz it already does stuff itself
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
        for (int i = this.projectiles.size() - 1; i >= 0; i--) {
            if (this.projectiles.get(i).shouldKillSelf()) {
                this.projectiles.remove(i);
            }
        }
    }

    public void updateProjectiles() {
        for (Projectile p : this.projectiles) {
            p.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {//draw stuff
        super.paintComponent(g);
        //draw player
        for (Projectile p : this.projectiles) {
            p.draw(g);
        }
        this.player.draw(g);
    }

    public void addProjectiles(ArrayList<Projectile> projectiles){
        this.projectiles.addAll(projectiles);
    }
}