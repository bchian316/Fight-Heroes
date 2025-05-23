import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Map implements Drawable{
    private final int WIDTH, HEIGHT;
    public static final int NUMBGIMAGES = 6;
    public static final int NUMBORDERIMAGES = 4;
    public static final int MAPIMAGESIZE = 50;
    public static final int BORDERIMAGESIZE = 50;

    private final Image[] groundImages = new Image[NUMBGIMAGES];
    private final Image[] wallImages = new Image[NUMBORDERIMAGES];

    private final Image[][] bg;

    public Map(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.loadImages();
        this.bg = new Image[this.getWallY() / MAPIMAGESIZE + 1][this.getWallX()/ MAPIMAGESIZE + 1];
        this.setMap();
    }

    public final int getWallX() {
        return this.WIDTH - BORDERIMAGESIZE - GameRunner.WIDTHOFFSET;
    }
    public final int getWallY(){
        return this.HEIGHT - BORDERIMAGESIZE - GameRunner.HEIGHTOFFSET;
    }
  
    private void loadImages(){
        for (int i = 0; i < NUMBORDERIMAGES; i++) {
            wallImages[i] = new ImageIcon("assets/terrain/border/" + Integer.toString(i + 1) + ".png").getImage()
                    .getScaledInstance(BORDERIMAGESIZE, BORDERIMAGESIZE, Image.SCALE_DEFAULT);
        }
        for (int i = 0; i < NUMBGIMAGES; i++) {
            groundImages[i] = new ImageIcon("assets/terrain/tile/" + Integer.toString(i + 1) + ".png").getImage()
                    .getScaledInstance(MAPIMAGESIZE, MAPIMAGESIZE, Image.SCALE_DEFAULT);
        }
    }

    public final void setMap() {
        //loads groundImages into 2d array
        //called every new level
        for (int i = 0; i <= this.getWallY(); i+= MAPIMAGESIZE) {
            for (int j = 0; j <= this.getWallX(); j += MAPIMAGESIZE) {
                if (i == 0 || j == 0 || i == this.getWallY() || j == this.getWallX()){
                    this.bg[i / MAPIMAGESIZE][j / MAPIMAGESIZE] = this.wallImages[(int) (Math.random() * NUMBORDERIMAGES)];
                } else {
                    this.bg[i/MAPIMAGESIZE][j/MAPIMAGESIZE] = this.groundImages[(int)(Math.random()*NUMBGIMAGES)];
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        for (int i = 0; i <= this.getWallY(); i+= MAPIMAGESIZE) {
            for (int j = 0; j <= this.getWallX(); j+= MAPIMAGESIZE) {
                g.drawImage(bg[i/MAPIMAGESIZE][j/MAPIMAGESIZE], j, i, null);
            }
        }
    }
}