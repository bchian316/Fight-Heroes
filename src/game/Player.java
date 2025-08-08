package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.ImageIcon;

import mages.Mage;

import java.awt.Image;

public final class Player extends Entity {
    public static final int PLAYERSTARTX = GameRunner.SCREENWIDTH / 2;
    public static final int PLAYERSTARTY = GameRunner.SCREENHEIGHT - 162;

    private static final Image STAT_FRAME = new ImageIcon("assets/Stat Frame.png").getImage();
    private static final int BAROFFSET = 5;

    private static final int BIGBARX = 40;
    private static final int BIGBARY = 14;
    private static final int BIGBARYCUSHION = 40;
    private static final int BIGBARWIDTH = 145;
    private static final int BIGBARHEIGHT = 26;

    private static final int HEALTHBARWIDTH = 50;
    private static final int HEALTHBARHEIGHT = 10;
    private static final Color HEALTHBAR_BGCOLOR = new Color(0, 0, 0);
    private static final Color HEALTHBAR_MGCOLOR = new Color(0, 255, 0); //display green over red
    private static final Color HEALTHBAR_FGCOLOR = new Color(0, 180, 0); //display green over red
    private static final Color HEALTHBAR_REGENCOLOR = new Color(0, 150, 0); //display green over red

    private static final int RELOADBARWIDTH = 10;
    private static final int RELOADBARHEIGHT = 50;
    private static final Color RELOADBAR_BGCOLOR = new Color(80, 80, 80);
    private static final Color RELOADBAR_MGCOLOR = new Color(150, 150, 150);
    private static final Color RELOADBAR_FGCOLOR = new Color(190, 190, 0); //dark gray, light gray, then yellow

    private static final int SPECIALBARWIDTH = 10;
    private static final int SPECIALBARHEIGHT = 50;
    private static final Color SPECIALBAR_BGCOLOR = new Color(80, 80, 80);
    private static final Color SPECIALBAR_MGCOLOR = new Color(255, 150, 0);
    private static final Color SPECIALBAR_FGCOLOR = new Color(240, 0, 0);

    private final Mage mage; //just for attacking

    private final Timer specialTimer;

    private int levelNumber = 0;
    
    private final int regenRate;
    private static final int REGENTIME = 3000; //3secs for regen
    private static final int REGENTICKTIME = 500;
    private final Timer regenTimer = new Timer(REGENTIME); //when to regen
    private final Timer regenTickTimer = new Timer(REGENTICKTIME);
    private boolean isHealing = false; //if regening
    
    public Player(Mage mage) {
        super("mages", Player.PLAYERSTARTX, Player.PLAYERSTARTY, mage);
        this.mage = mage;
        this.specialTimer = new Timer(this.mage.getSpecialReload());
        this.regenRate = mage.getRegen();
        this.load();
        this.loadSpecial();
    }

    public void setToStartCoords() {
        this.setX(Player.PLAYERSTARTX - this.getSize() / 2);
        this.setY(Player.PLAYERSTARTY - this.getSize() / 2);
    }

    public int getLevelNumber() {
        return this.levelNumber;
    }

    public void incrementLevelNumber() {
        this.levelNumber++;
    }

    private void regenerate() { //update the regen health
        if (this.isHealing) {
            if (this.regenTickTimer.update()) {
                regenTickTimer.reset();
                this.heal(this.regenRate);
            }
        } else if (!this.isHealing) {
            if (this.regenTimer.update()) {
                this.startHealing();
            }
        }
    }

    public void startHealing() { //for certain supers
        regenTimer.pause();
        this.isHealing = true;
        regenTickTimer.reset();
    }

    private void stopHealing() {
        this.isHealing = false;
        this.regenTimer.reset();
        this.regenTimer.start();
    }

    @Override
    public DamageCounter getDamaged(int damage, Color c) {
        this.stopHealing();
        return super.getDamaged(damage, c);
        
    }

    public ArrayList<DamageCounter> update(Set<Integer> pressedKeys, Map map) {
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
        double magnitude = Game.getDistance(dx, dy);
        if (magnitude != 0) {//prevent division by 0
            dx /= magnitude;
            dy /= magnitude;
            dx *= this.getSpeed();
            dy *= this.getSpeed();
            this.setBorders(map, dx, dy, true);
        }

        this.regenerate();

        this.specialTimer.update();

        return super.update();
    }

    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        //check if loaded first
        //center x and center y
        this.stopHealing();
        this.unload();
        return this.mage.createProjectiles(this.getCenterX(), this.getCenterY(), targetX, targetY);
    }

    public ArrayList<Projectile> special(double targetX, double targetY) {
        //check if loaded first
        this.stopHealing();
        this.specialTimer.reset();
        return this.mage.special(this, targetX, targetY);
    }

    public void loadSpecial() {
        this.specialTimer.beDone();
    }

    public boolean isSpecialLoaded() {
        return this.specialTimer.isDone();
    }

    private double getSpecialFraction() {
        return this.specialTimer.doneFraction();
    }
    
    @Override
    public void draw(Graphics g) {
        super.draw(g);
        this.drawHealthBar(g);
        this.drawReloadBar(g);
        this.drawSpecialBar(g);

        g.drawImage(STAT_FRAME, 0, 0, null);
        if (this.isHealing) {
            this.drawBigBar(g, BIGBARX, BIGBARY, this.getHealthFraction(), HEALTHBAR_REGENCOLOR, HEALTHBAR_MGCOLOR, HEALTHBAR_FGCOLOR);
        } else {
            this.drawBigBar(g, BIGBARX, BIGBARY, this.getHealthFraction(), HEALTHBAR_BGCOLOR, HEALTHBAR_MGCOLOR,
                    HEALTHBAR_FGCOLOR);
        }
        this.drawBigBar(g, BIGBARX, BIGBARY + BIGBARYCUSHION, this.getReloadFraction(), RELOADBAR_BGCOLOR, RELOADBAR_MGCOLOR, RELOADBAR_FGCOLOR);
        this.drawBigBar(g, BIGBARX, BIGBARY + BIGBARYCUSHION*2, this.getSpecialFraction(), SPECIALBAR_BGCOLOR, SPECIALBAR_MGCOLOR, SPECIALBAR_FGCOLOR);

    }

    @Override
    public void drawHealthBar(Graphics g) {
        //call in draw method
        g.setColor(HEALTHBAR_BGCOLOR);
        g.fillRect((int) this.getCenterX() - Player.HEALTHBARWIDTH / 2, (int) this.getY() + this.getSize() + BAROFFSET,
                Player.HEALTHBARWIDTH, Player.HEALTHBARHEIGHT);
        if (this.isHealing) {
            g.setColor(HEALTHBAR_REGENCOLOR);
            g.fillRect((int) this.getCenterX() - Player.HEALTHBARWIDTH / 2,
                    (int) this.getY() + this.getSize() + BAROFFSET,
                    Player.HEALTHBARWIDTH, Player.HEALTHBARHEIGHT);
        }
        g.setColor(HEALTHBAR_MGCOLOR);
        g.fillRect((int) this.getCenterX() - Player.HEALTHBARWIDTH / 2, (int) this.getY() + this.getSize() + BAROFFSET,
                (int) (Player.HEALTHBARWIDTH * this.getHealthFraction()), Player.HEALTHBARHEIGHT);
    }

    
    
    private void drawReloadBar(Graphics g) { //to the right of player
        g.setColor(Player.RELOADBAR_BGCOLOR);
        g.fillRect((int) this.getCenterX() + this.getSize() / 2 + Player.BAROFFSET,
                (int) this.getCenterY() - Player.RELOADBARHEIGHT / 2,
                Player.RELOADBARWIDTH, Player.RELOADBARHEIGHT);
        g.setColor(Player.RELOADBAR_MGCOLOR);
        g.fillRect((int) this.getCenterX() + this.getSize() / 2 + Player.BAROFFSET,
                (int) this.getCenterY() + Player.RELOADBARHEIGHT / 2
                - (int) (Player.RELOADBARHEIGHT * this.getReloadFraction()),
                Player.RELOADBARWIDTH,
                (int) (Player.RELOADBARHEIGHT * this.getReloadFraction()));
        if (this.isLoaded()) {
            g.setColor(Player.RELOADBAR_FGCOLOR);
            g.fillRect((int) this.getCenterX() + this.getSize() / 2 + Player.BAROFFSET,
                    (int) this.getCenterY() - Player.RELOADBARHEIGHT / 2,
                    Player.RELOADBARWIDTH, Player.RELOADBARHEIGHT);
        }
    }


    private void drawSpecialBar(Graphics g) { //to the right of player
        g.setColor(Player.SPECIALBAR_BGCOLOR);
        g.fillRect((int) this.getCenterX() - this.getSize() / 2 - Player.BAROFFSET - Player.SPECIALBARWIDTH,
                (int) this.getCenterY() - Player.SPECIALBARHEIGHT / 2,
                Player.SPECIALBARWIDTH, Player.SPECIALBARHEIGHT);
        g.setColor(Player.SPECIALBAR_MGCOLOR);
        g.fillRect((int) this.getCenterX() - this.getSize() / 2 - Player.BAROFFSET - Player.SPECIALBARWIDTH,
                (int) this.getCenterY() + Player.SPECIALBARHEIGHT / 2
                        - (int) (Player.SPECIALBARHEIGHT * this.getSpecialFraction()),
                Player.SPECIALBARWIDTH,
                (int) (Player.SPECIALBARHEIGHT * this.getSpecialFraction()));
        if (this.isSpecialLoaded()) {
            g.setColor(Player.SPECIALBAR_FGCOLOR);
            g.fillRect((int) this.getCenterX() - this.getSize() / 2 - Player.BAROFFSET - Player.SPECIALBARWIDTH,
                    (int) this.getCenterY() - Player.SPECIALBARHEIGHT / 2,
                    Player.SPECIALBARWIDTH, Player.SPECIALBARHEIGHT);
        }
    }

    private void drawBigBar(Graphics g, int x, int y, double widthFraction, Color bgColor, Color mgColor, Color fgColor) {
        g.setColor(bgColor);
        g.fillRect(x,y, BIGBARWIDTH,BIGBARHEIGHT);
        
        g.setColor(mgColor);
        g.fillRect(x, y, (int) (BIGBARWIDTH * widthFraction), BIGBARHEIGHT);
        
        if (widthFraction >= 1) {
            g.setColor(fgColor);
            g.fillRect(x, y, BIGBARWIDTH, BIGBARHEIGHT);
        } else {
            g.setColor(fgColor);
            String s = (int)(widthFraction*100) + "%";
            int textWidth = g.getFontMetrics(g.getFont()).stringWidth(s);
    
            g.drawString(s, x + BIGBARWIDTH/2 - textWidth/2, y + BIGBARHEIGHT - 2);
        }
        
    }
}