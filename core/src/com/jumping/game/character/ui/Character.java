package com.jumping.game.character.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.jumping.game.assets.AssetsManager;
import com.jumping.game.util.Values;

public class Character {
    private Rectangle bounds = new Rectangle();

    private Table characterTbl;

    private Stack character;
    private Container<Stack> leftArmContainer, rightArmContainer, leftLegContainer,
            rightLegContainer, headContainer, bodyContainer;
    private Stack leftArm, rightArm, leftLeg, rightLeg, head, body;

    private Container<Image> leftLegImg, rightLegImg, leftFootImg, rightFootImg,
            bodyImg,
            leftArmImg, rightArmImg, leftHandImg, rightHandImg,
            headImg, eyeImg, noseImg,
            leftEarImg, rightEarImg, mouthImg, hairImg;
    private Container<Image> leftLegClothing, rightLegClothing, leftFootClothing, rightFootClothing,
            bodyClothing,
            leftArmClothing, rightArmClothing,
            headClothing;

    public Character(AssetsManager assetsManager, Stage stage) {
        leftLegImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_LEG));
        leftLegClothing = getImg(assetsManager.getSpriteDrawable(Values.PIG_LEG));
        SpriteDrawable rightLegTexture = assetsManager.getSpriteDrawable(Values.PIG_LEG);
        rightLegTexture.getSprite().setFlip(true, false);
        rightLegImg = getImg(rightLegTexture);
        rightLegClothing = getImg(rightLegTexture);

        leftFootImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_FOOT));
        leftFootClothing = getImg(assetsManager.getSpriteDrawable(Values.PIG_FOOT));
        SpriteDrawable rightFootTexture = assetsManager.getSpriteDrawable(Values.PIG_FOOT);
        rightFootTexture.getSprite().setFlip(true, false);
        rightFootImg = getImg(rightFootTexture);
        rightFootClothing = getImg(rightFootTexture);

        leftArmImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_ARM));
        leftArmClothing = getImg(assetsManager.getSpriteDrawable(Values.PIG_ARM));
        SpriteDrawable rightArmTexture = assetsManager.getSpriteDrawable(Values.PIG_ARM);
        rightArmTexture.getSprite().setFlip(true, false);
        rightArmImg = getImg(rightArmTexture);
        rightArmClothing = getImg(rightArmTexture);

        leftHandImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_HAND));
        SpriteDrawable rightHandTexture = assetsManager.getSpriteDrawable(Values.PIG_HAND);
        rightHandTexture.getSprite().setFlip(true, false);
        rightHandImg = getImg(rightHandTexture);

        eyeImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_EYE));

        leftEarImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_EAR));
        SpriteDrawable rightEarTexture = assetsManager.getSpriteDrawable(Values.PIG_EAR);
        rightEarTexture.getSprite().setFlip(true, false);
        rightEarImg = getImg(rightEarTexture);

        headImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_HEAD));
        headClothing = getImg(assetsManager.getSpriteDrawable(Values.PIG_HEAD));
        bodyImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_BODY));
        bodyClothing = getImg(assetsManager.getSpriteDrawable(Values.PIG_BODY));
        noseImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_NOSE));
        mouthImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_MOUTH));
        hairImg = getImg(assetsManager.getSpriteDrawable(Values.PIG_HAIR));

        leftArm = new Stack();
        rightArm = new Stack();
        leftLeg = new Stack();
        rightLeg = new Stack();
        head = new Stack();
        body = new Stack();

        leftArmContainer = new Container<>(leftArm);
        rightArmContainer = new Container<>(rightArm);
        leftLegContainer = new Container<>(leftLeg);
        rightLegContainer = new Container<>(rightLeg);
        headContainer = new Container<>(head);
        bodyContainer = new Container<>(body);

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
        head.add(headClothing);
        head.add(eyeImg);
        head.add(mouthImg);
        head.add(noseImg);

        character = new Stack();
        character.setFillParent(true);
        character.add(leftLegContainer);
        character.add(rightLegContainer);
        character.add(leftArmContainer);
        character.add(rightArmContainer);
        character.add(bodyContainer);
        character.add(headContainer);

        characterTbl = new Table();
        characterTbl.setFillParent(true);
        characterTbl.add(new Container<>(character).fill()).padTop(120).grow();
        stage.addActor(characterTbl);

        headContainer.padBottom(650);
        leftArmContainer.padRight(500).padBottom(150);
        rightArmContainer.padLeft(500).padBottom(150);
        leftLegContainer.padRight(200).padTop(420);
        rightLegContainer.padLeft(200).padTop(420);
        rightEarImg.padBottom(350).padLeft(270);
        noseImg.padTop(80);
        mouthImg.padTop(290);
        eyeImg.padBottom(150);
        hairImg.center();
        hairImg.padBottom(400);
        leftEarImg.padBottom(350).padLeft(50);

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
}
