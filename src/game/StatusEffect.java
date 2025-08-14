package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class StatusEffect {
    private static final int ICON_SIZE = 30;
    private static final int ICON_BORDER_WIDTH = 6;
    private static final int DAMAGE_TICK_LENGTH = 1000;

    private final String name;
    private final int tickHealthChange;
    private final Timer tickDamageTimer = new Timer(DAMAGE_TICK_LENGTH);
    private final double regenChange, defenseChange, speedChange, reloadChange; //these are percentages (0.3 is -30% speed or -30% reload or + 30% extra damage)
    //stop regenChange, reduce health over time, increase damage taken over time, reduce speedChange, reduce reloadChange
    private final Timer timer;
    private final int time;
    private final Image image;
    private final Color color;

    public StatusEffect(String name, int tickHealthChange, double regenChange, double defenseChange, double speedChange, double reloadChange, int time, Color color) {
        this.name = name;
        this.tickHealthChange = tickHealthChange;
        this.regenChange = regenChange;
        this.defenseChange = defenseChange;
        this.speedChange = speedChange;
        this.reloadChange = reloadChange;
        this.time = time;
        
        this.timer = new Timer(this.time);
        
        this.image = new ImageIcon("assets/status effects/" + this.name + ".png").getImage()
                .getScaledInstance(StatusEffect.ICON_SIZE, StatusEffect.ICON_SIZE, Image.SCALE_DEFAULT);
        this.color = color;
    }

    public String getName() {
        return name;
    }
    
    public int getTickHealthChange() {//make sure to only call once per frame
        if (this.updateTickDamageTimer()) {
            return tickHealthChange;
        }
        return 0;
    }

    public double getRegenChange() {
        return regenChange;
    }

    public double getDefenseChange() {
        return defenseChange;
    }

    public double getSpeedChange() {
        return speedChange;
    }

    public double getReloadChange() {
        return reloadChange;
    }

    public int getTime() {
        return time;
    }

    public void resetTimer() {
        this.timer.reset();
    }

    public Color getColor() {
        return this.color;
    }

    public void drawIcon(Graphics g, int x, int y) {
        //draws in middle of x, y
        //if(Drawable.inScreen(x - StatusEffect.ICON_SIZE/2, y - StatusEffect.ICON_SIZE/2, StatusEffect.ICON_SIZE, StatusEffect.ICON_SIZE))
        //dont need, the player and entities checks if he is drawable
        g.setColor(this.color);
        g.fillOval(x - StatusEffect.ICON_SIZE/2, y - StatusEffect.ICON_SIZE/2, StatusEffect.ICON_SIZE, StatusEffect.ICON_SIZE);
        g.drawImage(this.image, x - StatusEffect.ICON_SIZE/2, y - StatusEffect.ICON_SIZE/2, null);
        g.setColor(Color.WHITE);
        for (int i = 1; i < StatusEffect.ICON_BORDER_WIDTH; i++) {
            g.drawArc(x - StatusEffect.ICON_SIZE/2 - i/2, y - StatusEffect.ICON_SIZE/2 - i/2, StatusEffect.ICON_SIZE+i, StatusEffect.ICON_SIZE+i, 90, (int) (360 * this.timer.doneFraction()));
            
        }
    }

    public boolean updateTickDamageTimer() {
        if (this.tickDamageTimer.update()) {
            this.tickDamageTimer.reset();
            return true;
        }
        return false;
    }

    public boolean updateAndExpire() {
        return this.timer.update();
    }

    public StatusEffect copy() {
        return new StatusEffect(name, tickHealthChange, regenChange, defenseChange, speedChange, reloadChange, time, color);
    }
}