package com.healthypetsTUM.game.character.ui;

public enum ClothPiece {
    LEGS, BODY, HEAD, SHOES;

    public static ClothPiece bodyPart(int item) { // todo adjust
        if(item <= 6) return HEAD;
        if(item <= 8) return LEGS;
        return SHOES;
    }
}
