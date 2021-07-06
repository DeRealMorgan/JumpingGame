package com.healthypetsTUM.game.character.ui.overlay;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.util.Musics;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.store.DataUtils;
import com.healthypetsTUM.game.util.store.UserData;
import com.healthypetsTUM.game.util.ui.Overlay;
import com.healthypetsTUM.game.util.ui.OverlayUtils;
import com.healthypetsTUM.game.util.ui.ToggleButton;

public class SettingsOverlay extends Overlay {
    private final ToggleButton musicButton;
    private final ToggleButton soundButton;
    private final Slider musicSlider;
    private final Slider soundSlider;

    private final Musics musics;

    public SettingsOverlay(AssetsManager assetsManager) {
        super(assetsManager, Values.SETTINGS_HEADER);
        musics = assetsManager.getMusics();
        //------------

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();

                UserData userData = DataUtils.getUserData();
                userData.setPlayMusic(!musicButton.isToggled());
                userData.setMusicVolume((int)musicSlider.getValue());
                userData.setPlaySound(!soundButton.isToggled());
                userData.setSoundVolume((int)soundSlider.getValue());
                DataUtils.storeUserData();

                close();
            }
        });

        //-------------

        musicButton = new ToggleButton(assetsManager.getDrawable(Values.MUSIC_ON),
                assetsManager.getDrawable(Values.MUSIC_OFF));
        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();

                musics.mute(musicButton.isToggled());
            }
        });

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.knob = assetsManager.getDrawable(Values.SLIDER_KNOB);
        sliderStyle.background = assetsManager.getDrawable(Values.SLIDER_BAR);
        sliderStyle.knob.setMinHeight(sliderStyle.background.getMinHeight()*2);

        musicSlider = new Slider(0, 100, 1, false, sliderStyle);
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int val = (int)musicSlider.getValue();
                musics.changeVolume(val);
            }
        });

        soundButton = new ToggleButton(assetsManager.getDrawable(Values.SOUNDS_ON),
                assetsManager.getDrawable(Values.SOUNDS_OFF));
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.mute(soundButton.isToggled());
                if(!soundButton.isToggled()) Sounds.click();
            }
        });

        soundSlider = new Slider(0, 100, 1, false, sliderStyle);
        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int val = (int)soundSlider.getValue();
                Sounds.changeVolume(val);
            }
        });

        Label musicLabel = new Label("Musik:", assetsManager.labelStyle());
        Label soundsLabel = new Label("Sounds:", assetsManager.labelStyle());

        Table musicTable = new Table();
        musicTable.add(musicLabel).width(Values.BTN_SIZE*2).row();
        musicTable.add(musicButton.getImage()).size(Values.BTN_SIZE).spaceRight(Values.SPACING);
        musicTable.add(musicSlider).size(Values.BTN_SIZE*3, Values.BTN_SIZE).row();

        Table soundTable = new Table();
        soundTable.add(soundsLabel).width(Values.BTN_SIZE*2).row();
        soundTable.add(soundButton.getImage()).size(Values.BTN_SIZE).spaceRight(Values.SPACING).colspan(2);
        soundTable.add(soundSlider).size(Values.BTN_SIZE*3, Values.BTN_SIZE).row();

        Table soundsTable = new Table();
        soundsTable.add(musicTable).width(Values.BTN_SIZE*6f).spaceBottom(Values.SPACING).row();
        soundsTable.add(soundTable).width(Values.BTN_SIZE*6f).row();
        contentTable.add(soundsTable).width(Values.BTN_SIZE*6f).row();

        UserData userData = DataUtils.getUserData();
        musicSlider.setValue(userData.getMusicVolume());
        soundSlider.setValue(userData.getSoundVolume());
        musicButton.setToggled(!userData.playMusic());
        soundButton.setToggled(!userData.playSound());
    }

    public void show() {
        OverlayUtils.show(table);
    }

    public void close() {
        OverlayUtils.close(table);
    }

    public void closeInstantly() {
        OverlayUtils.closeInstantly(table);
    }
}