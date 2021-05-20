package com.jumping.game.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.jumping.game.util.Values;

public class AssetsManagerImpl implements AssetsManager, Disposable {
    private final AssetManager assetManager;

    private final InputMultiplexer inputMultiplexer;

    private final TextureAtlas atlas;
    private final Skin skin;

    private final Label.LabelStyle labelStyleBig;

    public AssetsManagerImpl() {
        this.assetManager = new AssetManager(new InternalFileHandleResolver());

        this.assetManager.load(Values.FALLBACK_TEXTURE, Texture.class);
        // this.assetManager.load(Values.TEXTURE_PATH, TextureAtlas.class); // todo implement ? correct??

        this.atlas = new TextureAtlas(Gdx.files.internal(Values.ATLAS_NAME));
        this.skin = new Skin(this.atlas);

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);
        fontParameter.borderColor = Color.valueOf("3b3b3b");
        fontParameter.color = Color.valueOf("fafafa");
        fontParameter.minFilter = Texture.TextureFilter.Linear;
        fontParameter.magFilter = Texture.TextureFilter.Linear;
        fontParameter.borderWidth = Values.FONT_BORDER_WIDTH;
        fontParameter.size = Values.FONT_SIZE;

        BitmapFont font = fontGenerator.generateFont(fontParameter);
        labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = font;
    }

    public void update(float dt) {
        this.assetManager.update(Values.FPS_IN_MILLIS);
    }

    /**
     * Loads all queued assets. Use with care, will block thread if many/big assets need to be loaded.
     */
    public void loadAll() {
        this.assetManager.finishLoading();
    }

    public float getPercentLoaded() {
        return assetManager.getProgress();
    }

    @Override
    public Sprite getSprite(String name) {
        if(!assetManager.contains(name)) {// todo remove: only debug
            System.out.println("Asset with name \"" + name + "\" not found.");
            return new Sprite(assetManager.get(Values.FALLBACK_TEXTURE, Texture.class));
        }
        return new Sprite(assetManager.get(name, Texture.class));
    }

    @Override
    public Drawable getDrawable(String name) {
        if(!skin.has(name, Drawable.class)) {// todo remove: only debug
            System.out.println("Asset with name \"" + name + "\" in skin not found.");
            return new SpriteDrawable(getSprite(name));
        }
        return skin.getDrawable(name);
    }

    @Override
    public Label.LabelStyle labelStyleBig() {
        return labelStyleBig;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
