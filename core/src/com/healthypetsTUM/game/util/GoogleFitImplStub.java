package com.healthypetsTUM.game.util;

import com.healthypetsTUM.game.util.interfaces.GoogleFit;

public class GoogleFitImplStub implements GoogleFit {
    @Override
    public int getStepCountToday() {
        return 0;
    }

    @Override
    public int getStepCountYesterday() {
        return 0;
    }

    @Override
    public int getStepCountLast24() {
        return 0;
    }

    @Override
    public void signIn() {}
}
