package com.healthypetsTUM.game.util.interfaces;

import com.healthypetsTUM.game.util.StepListener;

public interface GoogleFit {
    void getStepCountToday(StepListener listener);
    void getStepCountYesterday(StepListener listener);
    void getStepCountLast24(StepListener listener);
    void signIn();
}
