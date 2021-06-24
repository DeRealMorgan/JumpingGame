package com.healthypetsTUM.game.character.ui;

public enum ClothPiece {
    LEGS, BODY, HEAD, SHOES;

    public static ClothPiece bodyPart(int item) { // todo adjust
        if(item <= 3) return HEAD;
        if(item <= 5) return BODY;
        if(item <= 8) return LEGS;
        return SHOES;
    }
}
