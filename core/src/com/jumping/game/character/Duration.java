package com.jumping.game.character;

public class Duration {
    private long duration;
    private long passedDuration;
    private long lastTimeStamp;

    public Duration(long duration) {
        this.duration = duration;
    }

    /**
     * Adds the difference between last timestamp and current time.
     * Returns true if time is over
     */
    public boolean addTimeStampDiff() {
        passedDuration += (System.currentTimeMillis()-lastTimeStamp);
        lastTimeStamp = System.currentTimeMillis();
        return isOver();
    }

    public void start() {
        lastTimeStamp = System.currentTimeMillis();
    }

    public void reset() {
        passedDuration = 0;
    }

    public void reset(long newDuration) {
        duration = newDuration;
        reset();
    }

    /**
     * Irreversible is over
     */
    public void setOver() {
        passedDuration = duration;
    }

    public boolean isOver() {
        return passedDuration > duration;
    }
}
