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
        this.g.attemptPlayerAttack((double) evt.getX(), (double) evt.getY());
    }
    
    @Override
    public void mouseReleased(MouseEvent evt) {}
    
    @Override
    public void mouseEntered(MouseEvent evt) {}

    @Override
    public void mouseExited(MouseEvent evt) {}

    @Override
    public void mouseClicked(MouseEvent evt) {}


    
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
    public void keyTyped(KeyEvent e) {}

    public Set<Integer> getPressedKeys() {
        return this.pressedKeys;
    }
    
}