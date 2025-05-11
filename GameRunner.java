import javax.swing.JFrame;

public class GameRunner {
    public static final int SCREENWIDTH = 700;
    public static final int SCREENHEIGHT = 500;
    public static void main(String[] args){
        JFrame window = new JFrame("Game");

        Game g = new Game();

		window.setSize(SCREENWIDTH,SCREENHEIGHT);
        window.setContentPane(g);
        window.setLocation(150,100);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(true);
        window.requestFocusInWindow();
    }
}