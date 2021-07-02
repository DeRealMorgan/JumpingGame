package com.healthypetsTUM.game.character.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.store.DataUtils;

import java.util.List;

public class Character {
    private final Rectangle bounds = new Rectangle();

    private final Stack leftArm;
    private final Stack rightArm;
    private final Stack leftLeg;
    private final Stack head;

    private final Container<Image> leftLegClothing;
    private final Container<Image> rightLegClothing;
    private final Container<Image> bodyClothing;
    private final Container<Image> leftArmClothing;
    private final Container<Image> rightArmClothing;
    private final Container<Image> headClothing;
    private final Container<Image> mouthImg;

    private SpriteDrawable happyMouthDrawable, sadMouthDrawable;

    public Character(AssetsManager assetsManager, Stage stage) {
        Container<Image> leftLegImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_LEG));
        leftLegClothing = getImg(assetsManager.getSpriteDrawable(Values.PIG_LEG));
        SpriteDrawable rightLegTexture = assetsManager.getSpriteDrawable(Values.PIG_LEG);
        rightLegTexture.getSprite().setFlip(true, false);
        Container<Image> rightLegImg = getImg(rightLegTexture);
        rightLegClothing = getImg(rightLegTexture);

        Container<Image> leftFootImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_FOOT));
        Container<Image> leftFootClothing = getImg(assetsManager.getSpriteDrawable(Values.PIG_FOOT));
        SpriteDrawable rightFootTexture = assetsManager.getSpriteDrawable(Values.PIG_FOOT);
        rightFootTexture.getSprite().setFlip(true, false);
        Container<Image> rightFootImg = getImg(rightFootTexture);
        Container<Image> rightFootClothing = getImg(rightFootTexture);

        Container<Image> leftArmImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_ARM));
        leftArmClothing = getImg(assetsManager.getSpriteDrawable(Values.PIG_ARM));
        SpriteDrawable rightArmTexture = assetsManager.getSpriteDrawable(Values.PIG_ARM);
        rightArmTexture.getSprite().setFlip(true, false);
        Container<Image> rightArmImg = getImg(rightArmTexture);
        rightArmClothing = getImg(rightArmTexture);

        Container<Image> leftHandImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_HAND));
        SpriteDrawable rightHandTexture = assetsManager.getSpriteDrawable(Values.PIG_HAND);
        rightHandTexture.getSprite().setFlip(true, false);
        Container<Image> rightHandImg = getImg(rightHandTexture);

        Container<Image> eyeImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_EYE));

        Container<Image> leftEarImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_EAR));
        SpriteDrawable rightEarTexture = assetsManager.getSpriteDrawable(Values.PIG_EAR);
        rightEarTexture.getSprite().setFlip(true, false);
        Container<Image> rightEarImg = getImg(rightEarTexture);

        happyMouthDrawable = assetsManager.getSpriteDrawable(Values.PIG_MOUTH);
        sadMouthDrawable = assetsManager.getSpriteDrawable(Values.PIG_MOUTH_SAD);

        Container<Image> headImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_HEAD));
        headClothing = getImg(assetsManager.getSpriteDrawable(Values.INVISIBLE_HEAD));
        Container<Image> bodyImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_BODY));
        bodyClothing = getImg(assetsManager.getSpriteDrawable(Values.PIG_BODY));
        Container<Image> noseImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_NOSE));
        mouthImg = getImg(happyMouthDrawable);
        Container<Image> hairImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_HAIR));

        leftArm = new Stack();
        rightArm = new Stack();
        leftLeg = new Stack();
        Stack rightLeg = new Stack();
        head = new Stack();
        Stack body = new Stack();

        Container<Stack> leftArmContainer = new Container<>(leftArm);
        Container<Stack> rightArmContainer = new Container<>(rightArm);
        Container<Stack> leftLegContainer = new Container<>(leftLeg);
        Container<Stack> rightLegContainer = new Container<>(rightLeg);
        Container<Stack> headContainer = new Container<>(head);
        Container<Stack> bodyContainer = new Container<>(body);

        leftArm.add(leftArmImg);
        leftArm.add(leftArmClothing);
        leftArm.add(leftHandImg);
        leftHandImg.bottom();

        rightArm.add(rightArmImg);
        rightArm.add(rightArmClothing);
        rightArm.add(rightHandImg);
        rightHandImg.bottom();

        leftLeg.add(leftLegImg);
        leftLeg.add(leftLegClothing);
        leftLeg.add(leftFootImg);
        leftLeg.add(leftFootClothing);
        leftFootImg.bottom();
        leftFootClothing.bottom();

        rightLeg.add(rightLegImg);
        rightLeg.add(rightLegClothing);
        rightLeg.add(rightFootImg);
        rightLeg.add(rightFootClothing);
        rightFootImg.bottom();
        rightFootClothing.bottom();

        body.add(bodyImg);
        body.add(bodyClothing);

        head.setTransform(true);
        head.add(leftEarImg);
        head.add(rightEarImg);
        head.add(hairImg);
        head.add(headImg);
        head.add(eyeImg);
        head.add(mouthImg);
        head.add(headClothing);
        head.add(noseImg);

        Stack character = new Stack();
        character.setFillParent(true);
        character.add(leftLegContainer);
        character.add(rightLegContainer);
        character.add(leftArmContainer);
        character.add(rightArmContainer);
        character.add(bodyContainer);
        character.add(headContainer);

        Table characterTbl = new Table();
        characterTbl.setFillParent(true);
        characterTbl.add(new Container<>(character).fill()).grow();
        stage.addActor(characterTbl);
        characterTbl.setY(-80);

        headContainer.padBottom(650);
        leftArmContainer.padRight(500).padBottom(150);
        rightArmContainer.padLeft(500).padBottom(150);
        leftLegContainer.padRight(200).padTop(420);
        rightLegContainer.padLeft(200).padTop(420);
        rightEarImg.padBottom(350).padLeft(270);
        noseImg.padTop(160);
        mouthImg.padTop(360);
        eyeImg.padBottom(60);
        hairImg.center();
        hairImg.padBottom(400).padLeft(20);
        leftEarImg.padBottom(350).padLeft(60);

        leftArm.pack();
        rightArm.pack();
        leftLeg.pack();
        rightLeg.pack();
        head.pack();
        body.pack();
        leftEarImg.pack();
        rightEarImg.pack();

        leftArm.setTransform(true);
        rightArm.setTransform(true);
        leftLegContainer.setTransform(true);
        rightLegContainer.setTransform(true);
        bodyImg.setTransform(true);
        leftEarImg.setTransform(true);
        rightEarImg.setTransform(true);
        hairImg.setTransform(true);
        head.setTransform(true);
        leftArm.setOrigin(Align.topRight);
        rightArm.setOrigin(Align.topLeft);
        leftLeg.setOrigin(Align.topRight);
        rightLeg.setOrigin(Align.topLeft);
        head.setOrigin(Align.bottom);
        leftEarImg.setOrigin(Align.bottomRight);
        rightEarImg.setOrigin(Align.bottomLeft);

        leftArm.addAction(Actions.sequence(Actions.delay(1), Actions.forever(
                Actions.sequence(Actions.rotateTo(-10, 2), Actions.rotateTo(10, 4)))));
        rightArm.addAction(Actions.forever(Actions.sequence(Actions.rotateTo(10, 2),
                Actions.rotateTo(-10, 4))));
        rightLeg.addAction(Actions.sequence(Actions.delay(1), Actions.forever(Actions.sequence(
                Actions.moveBy(0, 10, 1), Actions.moveBy(0, -10, 2)))));

        List<Integer> equiped = DataUtils.getUserData().getEquipedItems();
        for(Integer i : equiped)
            equipCloth(i, assetsManager);
    }

    public void equipCloth(int item, AssetsManager assetsManager) {
        switch (ClothPiece.bodyPart(item)) {
            case HEAD:
                headClothing.getActor().setDrawable(assetsManager.getSpriteDrawable(item+Values.SHOP_ITEM));
                return;
            case BODY:
                bodyClothing.getActor().setDrawable(assetsManager.getSpriteDrawable(item+Values.SHOP_ITEM));
                leftArmClothing.getActor().setDrawable(assetsManager.getSpriteDrawable(item+Values.SHOP_ITEM_ARM));
                SpriteDrawable rightArmDrawable = assetsManager.getSpriteDrawable(item+Values.SHOP_ITEM_ARM);
                rightArmDrawable.getSprite().setFlip(true, false);
                rightArmClothing.getActor().setDrawable(rightArmDrawable);
                return;
            case LEGS:
                leftLegClothing.getActor().setDrawable(assetsManager.getSpriteDrawable(item+Values.SHOP_ITEM));
                SpriteDrawable rightLegDrawable = assetsManager.getSpriteDrawable(item+Values.SHOP_ITEM);
                rightLegDrawable.getSprite().setFlip(true, false);
                rightLegClothing.getActor().setDrawable(rightLegDrawable);
                return;
            case SHOES:
                return;
        }
    }

    private Container<Image> getImg(SpriteDrawable drawable) {
        return new Container<>(new Image(drawable));
    }

    public boolean isOverlappingHead(Rectangle other) {
        return other.overlaps(getHeadBounds());
    }

    public boolean isOverlappingBody(Rectangle other) {
        return other.overlaps(getBodyBounds());
    }

    public Rectangle getHeadBounds() {
        return bounds.set(head.getX(), head.getY(), head.getWidth(), head.getHeight());
    }

    public Rectangle getBodyBounds() {
        return bounds.set(leftArm.getX(), leftLeg.getY(), rightArm.getRight(), head.getTop());
    }

    public void setHappy() {
        mouthImg.getActor().setDrawable(happyMouthDrawable);
    }

    public void setSad() {
        mouthImg.getActor().setDrawable(sadMouthDrawable);
    }
}
