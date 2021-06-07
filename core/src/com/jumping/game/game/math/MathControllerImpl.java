package com.jumping.game.game.math;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.jumping.game.game.GameManager;
import com.jumping.game.assets.AssetsManager;
import com.jumping.game.game.elements.MathAttachment;
import com.jumping.game.game.ui.GameUIController;
import com.jumping.game.util.MathUtils;
import com.jumping.game.util.Values;

public class MathControllerImpl implements MathController {
    private Table contentTable, exerciseTable;
    private Label exerciseLabel, answerLabel;

    private GameManager gameManager;
    private GameUIController controller;

    private String[] addStringList, subStringList, mulStringList, divStringList;

    private MathExercise currentExercise;
    private MathAttachment attachment;

    private String answerString = "";

    private boolean active;

    private Runnable onCorrectMath;

    public MathControllerImpl(GameManager gameManager, GameUIController controller, AssetsManager assetsManager,
                              Runnable onCorrectMath) {
        this.gameManager = gameManager;
        this.controller = controller;
        this.onCorrectMath = onCorrectMath;

        this.addStringList = loadList(Values.ADD_EXERCISE_FILE); // todo maybe async?
        this.subStringList = loadList(Values.SUB_EXERCISE_FILE);
        this.mulStringList = loadList(Values.MUL_EXERCISE_FILE);
        this.divStringList = loadList(Values.DIV_EXERCISE_FILE);

        buildUI(assetsManager);
    }

    private void buildUI(AssetsManager assetsManager) {
        contentTable = new Table();
        contentTable.setFillParent(true);
        contentTable.setVisible(false);
        contentTable.setTouchable(Touchable.disabled);
        contentTable.top();

        exerciseLabel = new Label("", assetsManager.labelStyleBig());
        exerciseLabel.setAlignment(Align.center);

        assetsManager.addInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if(!active) return false;

                if(keycode == Input.Keys.BACK || keycode == Input.Keys.ENTER)
                    closeKeyboard();
                if(keycode == Input.Keys.DEL)
                    deleteChar();

                return true;
            }

            @Override
            public boolean keyTyped(char character) {
                if(!active) return false;

                if(Character.isDigit(character)) digitTyped(character);
                if(answerString.isEmpty() && character == '-') digitTyped(character);
                return true;
            }
        });

        answerLabel = new Label(Values.ANSWER_TEXT, assetsManager.labelStyleBig());
        answerLabel.setAlignment(Align.center);

        exerciseTable = new Table();
        exerciseTable.add(exerciseLabel).row();
        exerciseTable.add(answerLabel).growX().row();
        exerciseTable.background(assetsManager.get9Drawable(Values.EXERCISE_BACKGROUND));

        contentTable.add(exerciseTable).top().growX().pad(Values.EXERCISE_PADDING).row();
    }

    private void closeKeyboard() {
        Gdx.input.setOnscreenKeyboardVisible(false);

        if(currentExercise.isCorrect(answerString)) {
            answerCorrect();
            attachment.remove();
        } else {
            answerWrong();
        }

        answerString = "";
        updateAnswer();
    }

    private void answerCorrect() {
        gameManager.resumeUpdateSlow();
        onCorrectMath.run();
        removeContent();
    }

    private void removeContent() {
        active = false;
        contentTable.remove();
    }

    private void addContent() {
        active = true;
        controller.showMathDialog(contentTable);
    }

    private void answerWrong() {
        gameManager.resumeUpdate();
        gameManager.gameOver();

        removeContent();
    }

    private void digitTyped(char c) {
        answerString += c;

        updateAnswer();
    }

    private void deleteChar() {
        if(answerString.isEmpty()) return;

        answerString = answerString.substring(0, answerString.length()-1);

        updateAnswer();
    }

    private void updateAnswer() {
        answerLabel.setText(Values.ANSWER_TEXT + answerString);
    }

    private String[] loadList(String l) {
        FileHandle file = Gdx.files.internal(l);
        String str = file.readString();
        return str.split(Values.NEW_LINE);
    }

    @Override
    public void showMathExercise(MathAttachment attachment) {
        this.attachment = attachment;
        loadExercise();

        gameManager.pauseUpdate();
        addContent();

        Gdx.input.setOnscreenKeyboardVisible(true, Input.OnscreenKeyboardType.NumberPad);
    }

    private void loadExercise() {
        // todo reuse and collect used exercises // logic!
        // todo make dependend on the height/level of the player or so, so you use a mixture of add/sub/mul/div ...
        String[] list = getExerciseList(MathUtils.getRandomX(0, 3));
        currentExercise = new MathExercise(list[MathUtils.getRandomX(0, list.length-1)]);

        exerciseLabel.setText(currentExercise.getExerciseQuestion());
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
