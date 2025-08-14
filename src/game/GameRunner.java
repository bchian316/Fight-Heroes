//MAKE SURE WE ARE IN Desktop/Fight Heroes> Directory

package game;

import javax.swing.JFrame;

public class GameRunner {
    public static final String NAME = "Fight Heroes";
    public static final int SCREENWIDTH = 800;
    public static final int SCREENHEIGHT = 600;

    public static void main(String[] args) {

        JFrame window = new JFrame(NAME);

        Game g = new Game();
        window.setContentPane(g);

		window.setSize(SCREENWIDTH + 10,SCREENHEIGHT + 30);
        window.setLocation(150,25);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(false);

        g.requestFocusInWindow();
    }
}