package com.healthypetsTUM.game.util.interfaces;

public interface GoogleFit {
    void getStepCountToday(StepListener listener);
    void getStepCountYesterday(StepListener listener);
    void getStepCountLast24(StepListener listener);
    void signIn();
}
