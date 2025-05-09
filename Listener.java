import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

public class Listener implements MouseListener, KeyListener {
    private final Game g;
    private final Set<Integer> pressedKeys = new HashSet<>();
    
    public Listener(Game g) {
        this.g = g;
        this.g.addMouseListener(this);
        this.g.addKeyListener(this);
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        //player attacks
        this.g.requestFocusInWindow();
        if (this.g.getPlayer().isLoaded()) {
            this.g.addProjectiles(this.g.getPlayer().attack((double) evt.getX(), (double) evt.getY()));
            this.g.getPlayer().resetReloadTimer();
        }
        

    } // end mousePressed()

    /**
     * Called whenever the user releases the mouse button. If the user was drawing 
     * a curve, the curve is done, so we should set dragging to false
     */
    @Override
    public void mouseReleased(MouseEvent evt) {
        
    }
    
    @Override
    public void mouseEntered(MouseEvent evt) {
    } // Some empty routines.

    @Override
    public void mouseExited(MouseEvent evt) {
    } //    (Required by the MouseListener

    @Override
    public void mouseClicked(MouseEvent evt) {
    }


    
    @Override
    public void keyPressed(KeyEvent e) {
        //use this
        this.pressedKeys.add(e.getKeyCode());
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        this.pressedKeys.remove(e.getKeyCode());
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    public Set<Integer> getPressedKeys() {
        return this.pressedKeys;
    }
    
}