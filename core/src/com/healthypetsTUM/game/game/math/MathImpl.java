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
import com.healthypetsTUM.game.game.elements.MathAttachment;
import com.healthypetsTUM.game.util.MathUtils;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.interfaces.VoidRunnableInt;
import com.healthypetsTUM.game.util.ui.Overlay;

public class MathImpl extends Overlay implements MathController {
    private final Label timerLabel;
    private final Label exerciseLabel;
    private final Label answerLabel;

    private final String[] addStringList;
    private final String[] subStringList;
    private final String[] mulStringList;
    private final String[] divStringList;

    private MathExercise currentExercise;
    private MathAttachment attachment;
    private String answerString = "";

    private boolean active;
    private long endTime;

    private VoidRunnableInt onCorrectMath;
    private Runnable onWrongMath, onMathShow;

    private int mathTime = Values.MATH_TIME;

    private boolean openKeyboard, closeKeyboard;

    public MathImpl(AssetsManager assetsManager, VoidRunnableInt onCorrectMath,
                    Runnable onWrongMath, Runnable onMathShow) {
        this(assetsManager, onCorrectMath, onWrongMath, onMathShow, true, true);
    }

    public MathImpl(AssetsManager assetsManager, VoidRunnableInt onCorrectMath,
                    Runnable onWrongMath, Runnable onMathShow, boolean openKeyboard,
                    boolean closeKeyboard) {
        super(assetsManager, Values.MATH_HEADER, Align.top);

        this.openKeyboard = openKeyboard;
        this.closeKeyboard = closeKeyboard;

        this.onCorrectMath = onCorrectMath;
        this.onWrongMath = onWrongMath;
        this.onMathShow = onMathShow;

        this.addStringList = loadList(Values.ADD_EXERCISE_FILE);
        this.subStringList = loadList(Values.SUB_EXERCISE_FILE);
        this.mulStringList = loadList(Values.MUL_EXERCISE_FILE);
        this.divStringList = loadList(Values.DIV_EXERCISE_FILE);

        Image clockImg = new Image(assetsManager.getDrawable(Values.CLOCK_ICON));
        clockImg.setScaling(Scaling.fillY);
        timerLabel = new Label(getTimeString(), assetsManager.labelStyle());
        timerLabel.setAlignment(Align.left);

        Table timerTable = new Table();
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
                if(keycode == Input.Keys.DEL || keycode == Input.Keys.NUMPAD_SUBTRACT || keycode == Input.Keys.PERIOD)
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

        Table exerciseTable = new Table();
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
        if(closeKeyboard) Gdx.input.setOnscreenKeyboardVisible(false);

        if(currentExercise.isCorrect(answerString)) {
            answerCorrect();
            if(attachment != null) attachment.remove();
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
                if(closeKeyboard) Gdx.input.setOnscreenKeyboardVisible(false);
                answerWrong();
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
        closeInstantly();
        onCorrectMath.run((int)(endTime-TimeUtils.millis())/1000);
    }

    private void answerWrong() {
        Sounds.wrong();
        closeInstantly();
        onWrongMath.run();

        answerString = "";
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
        onMathShow.run();
        this.attachment = attachment;
        loadExercise();

        showInstantly();

        if(openKeyboard) Gdx.input.setOnscreenKeyboardVisible(true, Input.OnscreenKeyboardType.NumberPad);
    }

    public void showSoftKeyboard() {
        Gdx.input.setOnscreenKeyboardVisible(true, Input.OnscreenKeyboardType.NumberPad);
    }

    public void closeSoftKeyboard() {
        Gdx.input.setOnscreenKeyboardVisible(false);
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

    public void setOnCorrectMath(VoidRunnableInt onCorrectMath) {
        this.onCorrectMath = onCorrectMath;
    }

    public void setMathTime(int time) {
        this.mathTime = time;
    }

    public void setOpenKeyboard(boolean openKeyboard) {
        this.openKeyboard = openKeyboard;
    }

    public void setCloseKeyboard(boolean closeKeyboard) {
        this.closeKeyboard = closeKeyboard;
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