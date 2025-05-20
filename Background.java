import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Background implements Drawable{
    private final int WIDTH, HEIGHT;
    public static final int NUMBGIMAGES = 10;
    public static final int NUMWALLIMAGES = 4;
    public static final int BGIMAGESIZE = 50;
    public static final int WALLIMAGESIZE = 50;

    private final Image[] groundImages = new Image[NUMBGIMAGES];
    private final Image[] wallImages = new Image[NUMWALLIMAGES];

    private final Image[][] bg;

    public Background(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.loadImages();
        this.bg = new Image[this.getWallY() / BGIMAGESIZE + 1][this.getWallX()/ BGIMAGESIZE + 1];
        this.setBackground();
    }

    public final int getWallX() {
        return this.WIDTH - WALLIMAGESIZE - GameRunner.WIDTHOFFSET;
    }
    public final int getWallY(){
        return this.HEIGHT - WALLIMAGESIZE - GameRunner.HEIGHTOFFSET;
    }
  
    private final void loadImages(){
        for (int i = 0; i < NUMWALLIMAGES; i++) {
            wallImages[i] = new ImageIcon("assets/terrain/w" + Integer.toString(i + 1) + ".png").getImage()
                    .getScaledInstance(WALLIMAGESIZE, WALLIMAGESIZE, Image.SCALE_DEFAULT);
        }
        for (int i = 0; i < NUMBGIMAGES; i++) {
            groundImages[i] = new ImageIcon("assets/terrain/t" + Integer.toString(i + 1) + ".png").getImage()
                    .getScaledInstance(BGIMAGESIZE, BGIMAGESIZE, Image.SCALE_DEFAULT);
        }
    }

    public final void setBackground() {
        //loads groundImages into 2d array
        //called every new level
        for (int i = 0; i <= this.getWallY(); i+= BGIMAGESIZE) {
            for (int j = 0; j <= this.getWallX(); j += BGIMAGESIZE) {
                if (i == 0 || j == 0 || i == this.getWallY() || j == this.getWallX()){
                    this.bg[i / BGIMAGESIZE][j / BGIMAGESIZE] = this.wallImages[(int) (Math.random() * NUMWALLIMAGES)];
                } else {
                    this.bg[i/BGIMAGESIZE][j/BGIMAGESIZE] = this.groundImages[(int)(Math.random()*NUMBGIMAGES)];
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        for (int i = 0; i <= this.getWallY(); i+= BGIMAGESIZE) {
            for (int j = 0; j <= this.getWallX(); j+= BGIMAGESIZE) {
                g.drawImage(bg[i/BGIMAGESIZE][j/BGIMAGESIZE], j, i, null);
            }
        }
    }
}