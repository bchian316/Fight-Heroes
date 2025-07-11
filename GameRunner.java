import javax.swing.JFrame;

public class GameRunner {
    public static final String NAME = "Fight Heroes";
    public static final int SCREENWIDTH = 825;
    public static final int WIDTHOFFSET = 25;
    public static final int SCREENHEIGHT = 650;
    public static final int HEIGHTOFFSET = 50;
    public static void main(String[] args){
        JFrame window = new JFrame(NAME);

        Game g = new Game();
        window.setContentPane(g);

		window.setSize(SCREENWIDTH,SCREENHEIGHT);
        window.setLocation(150,25);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(false);

        g.requestFocusInWindow();
    }
}