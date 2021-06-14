package com.jumping.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.jumping.game.util.Values;

public class AssetsManagerImpl implements AssetsManager, Disposable {
    private final InputMultiplexer inputMultiplexer;

    private final Texture fallbackTexture;
    private final TextureAtlas atlas;
    private final Skin skin;

    private final Label.LabelStyle labelStyleSmall, labelStyle, labelStyleBig;
    private final TextField.TextFieldStyle textFieldStyleBig;
    private final TextButton.TextButtonStyle textBtnStyle;

    // todo use AssetManager
    public AssetsManagerImpl() {
        this.fallbackTexture = new Texture(Gdx.files.internal(Values.FALLBACK_TEXTURE));

        this.atlas = new TextureAtlas(Gdx.files.internal(Values.ATLAS_NAME));
        this.skin = new Skin(this.atlas);

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);
        fontParameter.borderColor = Color.valueOf("3b3b3b");
        fontParameter.color = Values.FONT_COLOR;
        fontParameter.minFilter = Texture.TextureFilter.Linear;
        fontParameter.magFilter = Texture.TextureFilter.Linear;
        fontParameter.borderWidth = Values.FONT_BORDER_WIDTH;
        fontParameter.size = Values.FONT_SIZE;

        BitmapFont font = fontGenerator.generateFont(fontParameter);
        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        fontParameter.size = Values.FONT_SIZE_SMALL;
        BitmapFont fontSmall = fontGenerator.generateFont(fontParameter);
        labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = fontSmall;

        fontParameter.size = Values.FONT_SIZE_BIG;
        BitmapFont fontBig = fontGenerator.generateFont(fontParameter);
        labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = fontBig;

        textFieldStyleBig = new TextField.TextFieldStyle();
        textFieldStyleBig.cursor = getDrawable(Values.CURSOR);
        textFieldStyleBig.font = font;
        textFieldStyleBig.fontColor = Values.FONT_COLOR;

        textBtnStyle = new TextButton.TextButtonStyle();
        textBtnStyle.up = get9Drawable(Values.BTN_UP);
        textBtnStyle.down = get9Drawable(Values.BTN_DOWN);
        textBtnStyle.font = fontSmall;

    }

    public void update(float dt) {
    }

    /**
     * Loads all queued assets. Use with care, will block thread if many/big assets need to be loaded.
     */
    public void loadAll() {
    }

    @Override
    public void addInputProcessor(InputProcessor processor) {
        inputMultiplexer.addProcessor(processor);
    }

    @Override
    public void removeInputProcessor(InputProcessor processor) {
        inputMultiplexer.removeProcessor(processor);
    }

    @Override
    public void clearInputProcessors() {
        inputMultiplexer.clear();
    }

    @Override
    public Sprite getSprite(String name) {
        TextureAtlas.AtlasRegion region = atlas.findRegion(name); // todo remove: only debug
        Sprite s = new Sprite(region);
        s.setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());

        return s;
    }

    @Override
    public TextureRegion getTextureRegion(String name) {
        TextureAtlas.AtlasRegion region = atlas.findRegion(name); // todo remove: only debug
        if(region == null) return new TextureRegion(fallbackTexture);

        return region;
    }

    @Override
    public Drawable getDrawable(String name) {
        return skin.getDrawable(name);
    }

    @Override
    public Drawable get9Drawable(String name) {
        return new NinePatchDrawable(skin.getPatch(name));
    }

    @Override
    public SpriteDrawable getSpriteDrawable(String name) {
        return new SpriteDrawable(getSprite(name));
    }

    @Override
    public SpriteDrawable getBackground(String name) {
        Sprite s = new Sprite(new Texture(Gdx.files.internal(name)));
        return new SpriteDrawable(s);
    }

    @Override
    public Label.LabelStyle labelStyleSmall() {
        return labelStyleSmall;
    }

    @Override
    public Label.LabelStyle labelStyle() {
        return labelStyle;
    }

    @Override
    public Label.LabelStyle labelStyleBig() {
        return labelStyleBig;
    }

    @Override
    public TextField.TextFieldStyle textFieldStyleBig() {
        return textFieldStyleBig;
    }

    @Override
    public TextButton.TextButtonStyle textBtnStyle() {
        return textBtnStyle;
    }

    @Override
    public void dispose() {
        fallbackTexture.dispose();
        atlas.dispose();
    }
}
