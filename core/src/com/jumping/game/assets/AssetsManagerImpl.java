package com.jumping.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.jumping.game.util.Values;

public class AssetsManagerImpl implements AssetsManager, Disposable {
    private final InputMultiplexer inputMultiplexer;

    private final Texture fallbackTexture;
    private final TextureAtlas atlas;
    private final Skin skin;

    private final Label.LabelStyle labelStyleBig;
    private final TextField.TextFieldStyle textFieldStyleBig;

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
        labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = font;

        textFieldStyleBig = new TextField.TextFieldStyle();
        textFieldStyleBig.cursor = getDrawable(Values.CURSOR);
        textFieldStyleBig.font = font;
        textFieldStyleBig.fontColor = Values.FONT_COLOR;
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
    public Sprite getSprite(String name) {
        TextureAtlas.AtlasRegion region = atlas.findRegion(name); // todo remove: only debug
        if(region == null) return new Sprite(fallbackTexture);
        return new Sprite(region);
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
    public Label.LabelStyle labelStyleBig() {
        return labelStyleBig;
    }

    @Override
    public TextField.TextFieldStyle textFieldStyleBig() {
        return textFieldStyleBig;
    }

    @Override
    public void dispose() {
        fallbackTexture.dispose();
        atlas.dispose();
    }
}
