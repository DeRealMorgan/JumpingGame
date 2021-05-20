package com.jumping.game.util;

public class Values {
    public static final int MIN_WORLD_WIDTH = 1000, MAX_WORLD_WIDTH = 1000,
            MIN_WORLD_HEIGHT = 2000, MAX_WORLD_HEIGHT = 100000;

    public static final int WORLD_WIDTH = MIN_WORLD_WIDTH,
            WORLD_HEIGHT = MIN_WORLD_HEIGHT;

    public static final int SPACE_BELOW_TILE_Y = WORLD_HEIGHT/20; // how much of world below player tile is on screen

    public static final int EXERCISE_PADDING = 10;

    public static final int TILE_WIDTH_NORMAL = 250,
            TILE_HEIGHT_NORMAL = 30;
    public static final int PLAYER_WIDTH = 150,
            PLAYER_HEIGHT = 200;
    public static final int MATH_ATTACHMENT_WIDTH = 150, MATH_ATTACHMENT_HEIGHT = 110;

    public static final int FONT_BORDER_WIDTH = 2,
            FONT_SIZE = 30;

    public static final String ATLAS_NAME = "game.atlas";
    public static final String TEXTURE_PATH = "game_textures";
    public static final String PLAYER_SPRITE = "player",
            TILE_SPRITE = "tile",
            MATH_SPRITE = "math";
    public static final String FALLBACK_TEXTURE = "fallback_texture.jpg";
    public static final String EXERCISE_BACKGROUND = "exercise_background";

    public static final float GRAVITY = -1200;

    public static final float PLAYER_COLLISION_DELTA = 30;
    public static final float PLAYER_JUMP_VELOCITY_Y = 1200;

    public static final float TILE_DISTANCE_Y = 400;

    public static final float CAM_INTERPOLATION_STEP = 5f;

    public static final int FPS_IN_MILLIS = 16;

    public static final int ACCELATE_X = 200;
    public static final float VELOCITY_TRESHOLD_X = 0.1f; // if detected velocityX is too mall, it should be ignored
    public static final float DESKTOP_VELOCITY_X = 2f; // velocity for desktop testing

    public static final String ADD_EXERCISE_FILE = "exercise_add.txt",
            SUB_EXERCISE_FILE = "exercise_sub.txt",
            MUL_EXERCISE_FILE = "exercise_mul.txt",
            DIV_EXERCISE_FILE = "exercise_div.txt";

    public static final String NEW_LINE = "\n";
}
