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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

    private final Table correctTable;
    private final Table wrongTable;
    private final Label correctAnswerLabel;

    private final TextButton nextBtn;

    private final String[] addStringList;
    private final String[] subStringList;
    private final String[] mulStringList;
    private final String[] divStringList;

    private MathExercise currentExercise;
    private MathAttachment attachment;
    private String answerString = "-";

    private boolean active;
    private long endTime;

    private VoidRunnableInt onCorrectMath;
    private Runnable onWrongMath, onMathShow;

    private int mathTime = Values.MATH_TIME;

    private boolean openKeyboard, closeKeyboard, isCorrect, useNext;

    private int answerTime;

    public MathImpl(AssetsManager assetsManager, VoidRunnableInt onCorrectMath,
                    Runnable onWrongMath, Runnable onMathShow) {
        this(assetsManager, onCorrectMath, onWrongMath, onMathShow, true, true);
    }

    public MathImpl(AssetsManager assetsManager, VoidRunnableInt onCorrectMath,
                    Runnable onWrongMath, Runnable onMathShow, boolean openKeyboard,
                    boolean closeKeyboard) {
        super(assetsManager, Values.MATH_HEADER, Align.top);

        closeButton.getListeners().clear();
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                active = false;
                closeInstantly();

                correctTable.setVisible(false);
                wrongTable.setVisible(false);
                showNext(false);
                answerString = "";
                updateAnswer();

                if(isCorrect)
                    MathImpl.this.correct();
                else
                    MathImpl.this.wrong();

                if(MathImpl.this.closeKeyboard) Gdx.input.setOnscreenKeyboardVisible(false);
            }
        });

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
                else if(keycode == Input.Keys.DEL)
                    deleteChar();
                else
                    return false;

                return true;
            }

            @Override
            public boolean keyTyped(char character) {
                if(!active) return false;

                if(Character.isDigit(character)) digitTyped(character);
                return true;
            }
        });

        answerLabel = new Label(Values.ANSWER_TEXT, assetsManager.labelStyle());
        answerLabel.setAlignment(Align.left);

        Table exerciseTable = new Table();
        exerciseTable.add(exerciseLabel).growX().center().padBottom(Values.PADDING).row();
        exerciseTable.add(timerTable).growX().center().padBottom(Values.PADDING).row();
        exerciseTable.add(answerLabel).center().growX().padBottom(Values.PADDING).row();

        Image correctImage = new Image(assetsManager.getDrawable(Values.CORRECT_ICON));
        correctImage.setScaling(Scaling.fit);
        Label correctLabel = new Label("Richtig!", assetsManager.labelStyle());

        correctTable = new Table();
        correctTable.add(correctImage).size(Values.BTN_SIZE_SMALL).padRight(Values.PADDING);
        correctTable.add(correctLabel).left().row();

        Image wrongImage = new Image(assetsManager.getDrawable(Values.WRONG_ICON));
        wrongImage.setScaling(Scaling.fit);
        correctAnswerLabel = new Label("", assetsManager.labelStyle());

        wrongTable = new Table();
        wrongTable.add(wrongImage).size(Values.BTN_SIZE_SMALL).padRight(Values.PADDING);
        wrongTable.add(correctAnswerLabel).left().row();

        contentTable.add(exerciseTable).center().growX().width(Values.BTN_SIZE*4f).row();
        contentTable.stack(wrongTable, correctTable).growX().row();

        TextButton.TextButtonStyle okBtnStyle = new TextButton.TextButtonStyle();
        okBtnStyle.up = assetsManager.get9Drawable(Values.BTN_UP);
        okBtnStyle.down = okBtnStyle.up;
        okBtnStyle.font = assetsManager.labelStyle().font;

        nextBtn = new TextButton("Nächste Aufgabe", okBtnStyle);
        nextBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();

                correctTable.setVisible(false);
                wrongTable.setVisible(false);

                showNext(false);
                answerString = "";
                updateAnswer();

                if(isCorrect)
                    MathImpl.this.correct();
                else
                    MathImpl.this.wrong();

                showMathExercise(attachment);
            }
        });
        nextBtn.setTransform(true);

        contentTable.add(nextBtn).growX().height(Values.BTN_SIZE).padTop(Values.PADDING_SMALL).row();
        showNext(false);

        correctTable.setVisible(false);
        wrongTable.setVisible(false);

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

        updateAnswer();
    }

    public void update(float dt) {
        if(active) {
            updateLabel();
            if(isTimerOver()) {
                closeKeyboard();
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
        useClose(true);
        active = false;

        correctTable.setVisible(true);
        showNext(useNext);
        isCorrect = true;

        answerTime = (int)(endTime-TimeUtils.millis())/1000;
    }

    private void answerWrong() {
        Sounds.wrong();
        useClose(true);
        active = false;
        isCorrect = false;

        wrongTable.setVisible(true);
        showNext(useNext);
    }

    private void digitTyped(char c) {
        answerString += c;

        updateAnswer();
    }

    private void deleteChar() {
        if(answerString.isEmpty()) return;

        answerString = answerString.substring(0, answerString.length()-1);
        if(currentExercise.isNegative() && answerString.trim().isEmpty()) answerString = "-";

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
        useClose(false);

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
        if(currentExercise.isNegative()) answerString = "-";
        else answerString = "";
        correctAnswerLabel.setText(currentExercise.getWholeExercise());

        updateAnswer();
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

    private void correct() {
        onCorrectMath.run(answerTime);
    }

    private void wrong() {
        onWrongMath.run();
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

    public void useNext(boolean use) {
        this.useNext = use;
    }

    private void showNext(boolean use) {
        nextBtn.setVisible(use);
        if(use) {
            nextBtn.setTouchable(Touchable.enabled);
        } else {
            nextBtn.setTouchable(Touchable.disabled);
        }
    }
}