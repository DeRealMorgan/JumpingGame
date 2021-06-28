package com.healthypetsTUM.game.game.math;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.game.GameManager;
import com.healthypetsTUM.game.game.elements.MathAttachment;
import com.healthypetsTUM.game.util.MathUtils;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.interfaces.VoidRunnableInt;
import com.healthypetsTUM.game.util.ui.Overlay;

public class MathImpl extends Overlay implements MathController {
    private Table exerciseTable, timerTable;
    private Label timerLabel, exerciseLabel, answerLabel;

    private Image clockImg;
    private GameManager gameManager;

    private String[] addStringList, subStringList, mulStringList, divStringList;

    private MathExercise currentExercise;
    private MathAttachment attachment;
    private String answerString = "";

    private boolean active;
    private long endTime;

    private VoidRunnableInt onCorrectMath;
    private int mathTime = Values.MATH_TIME;

    public MathImpl(GameManager gameManager, AssetsManager assetsManager, VoidRunnableInt onCorrectMath) {
        super(assetsManager, Values.MATH_HEADER, Align.top);

        this.gameManager = gameManager;
        this.onCorrectMath = onCorrectMath;

        this.addStringList = loadList(Values.ADD_EXERCISE_FILE);
        this.subStringList = loadList(Values.SUB_EXERCISE_FILE);
        this.mulStringList = loadList(Values.MUL_EXERCISE_FILE);
        this.divStringList = loadList(Values.DIV_EXERCISE_FILE);

        clockImg = new Image(assetsManager.getDrawable(Values.CLOCK_ICON));
        clockImg.setScaling(Scaling.fillY);
        timerLabel = new Label(getTimeString(), assetsManager.labelStyle());
        timerLabel.setAlignment(Align.left);

        timerTable = new Table();
        timerTable.setTouchable(Touchable.disabled);
        timerTable.add(clockImg).padRight(Values.PADDING);
        timerTable.add(timerLabel).growX().row();

        exerciseLabel = new Label("", assetsManager.labelStyle());
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

        answerLabel = new Label(Values.ANSWER_TEXT, assetsManager.labelStyle());
        answerLabel.setAlignment(Align.left);

        exerciseTable = new Table();
        exerciseTable.add(exerciseLabel).growX().center().padBottom(Values.PADDING).row();
        exerciseTable.add(timerTable).growX().center().padBottom(Values.PADDING).row();
        exerciseTable.add(answerLabel).center().growX().row();

        contentTable.add(exerciseTable).center().growX().width(Values.BTN_SIZE*4f).row();
        contentTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                closeKeyboard();
            }
        });

        //------------

        useClose(false);

        closeInstantly();
        super.scaleOverlay();
    }

    private void closeKeyboard() {
        Gdx.input.setOnscreenKeyboardVisible(false);
        gameManager.enablePause();
        if(currentExercise.isCorrect(answerString)) {
            answerCorrect();
            attachment.remove();
        } else {
            answerWrong();
        }

        answerString = "";
        updateAnswer();
    }

    public void update(float dt) {
        if(active) {
            updateLabel();
            if(isTimerOver()) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                answerWrong();
                gameManager.enablePause();
            }
        }
    }

    private void updateLabel() {
        timerLabel.setText(getTimeString());
    }

    private boolean isTimerOver() {
        return endTime <= TimeUtils.millis();
    }

    private String getTimeString() {
        int s = (int)Math.max(0, (endTime-TimeUtils.millis())/1000);
        if(s >= 10) return "00:" + s;
        else return "00:0" + s;
    }

    private void answerCorrect() {
        Sounds.correct();
        gameManager.resumeUpdateSlow();
        onCorrectMath.run((int)(endTime-TimeUtils.millis())/1000);
        closeInstantly();
    }

    private void answerWrong() {
        Sounds.wrong();
        gameManager.resumeUpdate();
        gameManager.gameOver();

        closeInstantly();
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
        gameManager.disablePause();

        showInstantly();

        Gdx.input.setOnscreenKeyboardVisible(true, Input.OnscreenKeyboardType.NumberPad);
    }

    private void loadExercise() {
        // todo reuse and collect used exercises // logic!
        // todo make dependend on the height/level of the player or so, so you use a mixture of add/sub/mul/div ...
        String[] list = getExerciseList(MathUtils.getRandomX(0, 3));
        currentExercise = new MathExercise(list[MathUtils.getRandomX(0, list.length-1)]);

        endTime = TimeUtils.millis() + mathTime;
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

    public void setMathTime(int time) {
        this.mathTime = time;
    }

    @Override
    public void showInstantly() {
        active = true;
        super.showInstantly();
    }

    @Override
    public void closeInstantly() {
        active = false;
        super.closeInstantly();
    }
}