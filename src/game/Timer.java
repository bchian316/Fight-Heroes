package game;

/**
* Provides a timer that runs when {@code update()} is called.
*
* Timers must have their {@code update()} method called periodically to run.
*/
public class Timer {
    private boolean running = true;
    private final int time;
    private int timer = 0;

    /**
	 * Constructs a Timer object that is done every {@code time} seconds
	 *
	 * @param time How long the timer is. This value is permanent and cannot be changed.
     * @param timerStart At how many seconds should the timer be prestarted. Default is 0.
	 * @return A new Timer object
	 */
    public Timer(int time, int timerStart) {
        //for starting the timer at a specific value
        this.timer = timerStart;
        this.time = time;
    }

    /**
	 * Constructs a Timer object that is done every {@code time} seconds
	 *
	 * @param time How long the timer is. This value is permanent and cannot be changed.
	 * @return A new Timer object
	 */
    public Timer(int time) {
        //create timer at 0
        this(time, 0);
    }

    /**
	 * Pauses the timer. Timers can continue running with the {@code start()} method.
	 */
    public void pause() {
        this.running = false;
    }

    /**
	 * Starts the timer if the timer was not started already.
	 */
    public void start() {
        this.running = true;
    }

    /**
	 * Resets the timer to the beginning.
	 */
    public void reset() {
        this.timer = 0;
    }

    /**
	 * Set the timer to be done instantly.
	 */
    public void beDone() {
        this.timer = this.time;
    }

    /**
	 * Checks if the timer is done.
     *
     * @return {@code true} if the timer is done, {@code false} if the timer is not done.
	 */
    public boolean isDone() {
        return this.timer >= this.time;
    }

    /**
	 * Returns the decimal representing how close the timer is to being done.
     *
     * @return the current timer counter divided by the time limit as a double.
	 */
    public double doneFraction() {
        return ((double) this.timer) / ((double) this.time);
    }

    /**
	 * Updates the timer. Call once per frame.
     *
     * @param reduction A decimal representing how much to speed or slow the update of the timer. 0.5 would be +50%
     * @return {@code true} if the timer is done, {@code false} if the timer is not done.
	 */
    public boolean update(double reduction) {
        //0.8 means 80% buff, -0.8 is -80%
        if (this.running) {
            this.timer += Game.updateDelay() + (int) (Game.updateDelay() * reduction);
            if (this.timer > this.time) {
                this.timer = this.time;
            } else if (this.timer < 0) {
                //in case for unreloader abilities
                this.timer = 0;
            }
        }
        return this.isDone();
    }

    /**
	 * Updates the timer. Call once per frame.
     *
     * @return {@code true} if the timer is done, {@code false} if the timer is not done.
	 */
    public boolean update() {
        return this.update(0);
    }

}
