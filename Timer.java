public class Timer {
    private boolean running = true;
    private final int time;
    private int timer = 0;

    public Timer(int time, int timerStart) {
        //for starting the timer at a specific value
        this.timer = timerStart;
        this.time = time;
    }

    public Timer(int time) {
        //create timer at 0
        this(time, 0);
    }

    public void pause() {
        this.running = false;
    }

    public void start() {
        this.running = true;
    }

    public void reset() {
        this.timer = 0;
    }

    public void beDone() {
        this.timer = this.time;
    }

    public boolean isDone() {
        return this.timer >= this.time;
    }

    public double doneFraction() {
        return ((double) this.timer) / ((double) this.time);
    }

    public void update(double reduction) {
        //0.8 means 80% buff, -0.8 is -80%
        if (this.running) {
            this.timer += Game.updateDelay() + (int) (Game.updateDelay() * reduction);
            if (this.timer > this.time) {
                this.timer = this.time;
            }
        }
    }

    public void update() {
        this.update(0);
    }
}