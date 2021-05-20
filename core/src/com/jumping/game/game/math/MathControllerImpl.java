package com.jumping.game.game.math;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.jumping.game.game.GameManager;
import com.jumping.game.game.assets.AssetsManager;
import com.jumping.game.game.ui.GameUIController;
import com.jumping.game.util.MathUtils;
import com.jumping.game.util.Values;

public class MathControllerImpl implements MathController {
    private Table contentTable, exerciseTable;
    private Label exerciseLabel;

    private GameManager gameManager;
    private GameUIController controller;

    private String[] addStringList, subStringList, mulStringList, divStringList;

    private MathExercise currentExercise;

    public MathControllerImpl(GameManager gameManager, GameUIController controller, AssetsManager assetsManager) {
        this.gameManager = gameManager;
        this.controller = controller;

        this.addStringList = loadList(Values.ADD_EXERCISE_FILE); // todo maybe async?
        this.subStringList = loadList(Values.SUB_EXERCISE_FILE);
        this.mulStringList = loadList(Values.MUL_EXERCISE_FILE);
        this.divStringList = loadList(Values.DIV_EXERCISE_FILE);

        contentTable = new Table();
        contentTable.setFillParent(true);
        contentTable.setVisible(false);
        contentTable.setTouchable(Touchable.disabled);

        exerciseLabel = new Label("", assetsManager.labelStyleBig());
        exerciseLabel.setAlignment(Align.center);
        exerciseTable = new Table();
        exerciseTable.add(exerciseLabel);
        exerciseTable.background(assetsManager.getDrawable(Values.EXERCISE_BACKGROUND));

        contentTable.add(exerciseTable).top().grow().pad(Values.EXERCISE_PADDING);
    }

    private String[] loadList(String l) {
        FileHandle file = Gdx.files.internal(l);
        String str = file.readString();
        return str.split(Values.NEW_LINE);
    }

    @Override
    public void showMathExercise() {
        loadExercise();

        gameManager.pauseUpdate();
        controller.showMathDialog(contentTable);
    }

    private void buildContentTable() {

    }

    private void loadExercise() {
        // todo reuse and collect used exercises // logic!
        // todo make dependend on the height/level of the player or so, so you use a mixture of add/sub/mul/div ...
        String[] list = getExerciseList(MathUtils.getRandomX(0, 3));
        currentExercise = new MathExercise(list[MathUtils.getRandomX(0, list.length-1)]);
    }

    /**
     * Returns list with index i
     * 0: addList
     * 1: subList
     * 2: mulList
     * 3: divList
     */
    private String[] getExerciseList(int i) {
        switch (i) {
            case 0:
                return addStringList;
            case 1:
                return subStringList;
            case 2:
                return mulStringList;
            case 3:
                return divStringList;
            default:
                System.out.println("Error, Exercise list with index " + i + " does not exist!");
                return addStringList;
        }
    }
}
