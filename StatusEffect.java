
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;

import javax.swing.ImageIcon;

public class StatusEffect {
    private static final int ICON_SIZE = 30;
    private static final int ICON_BORDER_WIDTH = 6;
    private static final int DAMAGE_TICK_LENGTH = 1000;

    private final String name;
    private final int tickDamage;
    private int tickDamageTimer;
    private final double regenReduction, defenseReduction, speedReduction, reloadReduction; //these are percentages (0.3 is -30% speed or -30% reload or + 30% extra damage)
    //stop regenReduction, reduce health over time, increase damage taken over time, reduce speedReduction, reduce reloadReduction
    private double timer = 0;
    private final int time;
    private final Image image;
    private final Color color;

    public StatusEffect(String name, int tickDamage, double regenReduction, double defenseReduction, double speedReduction, double reloadReduction, int time, Color color) {
        this.name = name;
        this.tickDamage = tickDamage;
        this.regenReduction = regenReduction;
        this.defenseReduction = defenseReduction;
        this.speedReduction = speedReduction;
        this.reloadReduction = reloadReduction;
        this.time = time;
        this.image = new ImageIcon("assets/status effects/" + this.name + ".png").getImage()
                .getScaledInstance(StatusEffect.ICON_SIZE, StatusEffect.ICON_SIZE, Image.SCALE_DEFAULT);
        this.color = color;
    }

    public String getName() {
        return name;
    }
    
    public int getTickDamage() {
        if (this.updateTickDamageTimer()) {
            return tickDamage;
        }
        return 0;
    }

    public double getRegenReduction() {
        return regenReduction;
    }

    public double getDefenseReduction() {
        return defenseReduction;
    }

    public double getSpeedReduction() {
        return speedReduction;
    }

    public double getReloadReduction() {
        return reloadReduction;
    }

    public int getTime() {
        return time;
    }

    public void resetTimer() {
        this.timer = 0;
    }

    public void drawIcon(Graphics g, int x, int y) {
        //draws in middle of x, y
        g.setColor(this.color);
        g.fillOval(x - StatusEffect.ICON_SIZE/2, y - StatusEffect.ICON_SIZE/2, StatusEffect.ICON_SIZE, StatusEffect.ICON_SIZE);
        g.drawImage(this.image, x - StatusEffect.ICON_SIZE/2, y - StatusEffect.ICON_SIZE/2, null);
        g.setColor(Color.WHITE);
        for (int i = 0; i < StatusEffect.ICON_BORDER_WIDTH; i++) {
            g.drawArc(x - StatusEffect.ICON_SIZE/2 - i/2, y - StatusEffect.ICON_SIZE/2 - i/2, StatusEffect.ICON_SIZE+i, StatusEffect.ICON_SIZE+i, 90, (int) (360 * this.timer / this.time));
            
        }
    }

    public boolean updateTickDamageTimer() {
        this.tickDamageTimer += Game.updateDelay();
        if (this.tickDamageTimer >= StatusEffect.DAMAGE_TICK_LENGTH) {
            this.tickDamageTimer = 0;
            return true;
        }
        return false;
    }

    public boolean updateAndExpire() {
        this.timer += Game.updateDelay();
        if (this.timer >= this.time) {
            this.timer = 0; //reset timer for the next application: enemies will always have the same statusEffect object
            return true; //statusEffect is over
        }
        return false;
    }
}