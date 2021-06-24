package com.healthypetsTUM.game.util;

import com.healthypetsTUM.game.util.interfaces.GoogleFit;

public class GoogleFitImplStub implements GoogleFit {
    @Override
    public void getStepCountToday(StepListener listener) {
        listener.stepsReturned(0);
    }

    @Override
    public void getStepCountYesterday(StepListener listener) {
        listener.stepsReturned(0);
    }

    @Override
    public void getStepCountLast24(StepListener listener) {
        listener.stepsReturned(0);
    }

    @Override
    public void signIn() {}
}
