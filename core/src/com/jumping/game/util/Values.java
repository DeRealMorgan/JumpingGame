package com.jumping.game.util;

import com.badlogic.gdx.graphics.Color;

public class Values {
    public static final String DATA_PATH = "dataPath",
            SHARED_PREF = "healthAppPrefs";

    public static final int MIN_WORLD_WIDTH = 1000, MAX_WORLD_WIDTH = 1000,
            MIN_WORLD_HEIGHT = 2000, MAX_WORLD_HEIGHT = 100000;
    public static final int CHARACTER_WORLD_WIDTH = 800, CHARACTER_WORLD_HEIGHT = 1400;

    public static final int WORLD_WIDTH = MIN_WORLD_WIDTH,
            WORLD_HEIGHT = MIN_WORLD_HEIGHT;

    public static final int SPACE_BELOW_TILE_Y = WORLD_HEIGHT/20; // how much of world below player tile is on screen

    public static final int MATH_TIME = 30000; // how much time for giving an answer to a math exercise
    public static final int SLOW_UPDATE = 3000; // for how long game will be slowed down after math exercise
    public static final int EXERCISE_PADDING = 50, PADDING_BIG = 50, PADDING = 30, PADDING_SMALL = 15;

    public static final int TILE_WIDTH_NORMAL = 250,
            TILE_HEIGHT_NORMAL = 30;
    public static final int PLAYER_WIDTH = 150,
            PLAYER_HEIGHT = 200;
    public static final int MATH_ATTACHMENT_WIDTH = 150, MATH_ATTACHMENT_HEIGHT = 110;

    public static final int SPACING = 20, SPACING_SMALL = 10, EDGE_DISTANCE = 50, TOP_PADDING_UI_GAME = 100;
    public static final float FADE_OVERLAY_DUR = 1f;

    public static final int FONT_BORDER_WIDTH = 2,
            FONT_SIZE_SMALL = 30, FONT_SIZE = 50, FONT_SIZE_BIG = 80, FONT_SIZE_HUGE = 120;
    public static final Color FONT_COLOR = Color.valueOf("fafafa");

    public static final String PREFERENCES = "APP_PREFS";
    public static final String NOTIFY_CHANNEL_ID = "basic_notification_channel";
    public static final CharSequence NOTIFY_CHANNEL_NAME = "Items Benachrichtigung";

    public static final String ATLAS_NAME = "Game.atlas";
    public static final String PLAYER_SPRITE = "player",
            TILE_SPRITE = "tile",
            MATH_SPRITE = "math",
            CURSOR = "cursor";
    public static final String SHOP_BTN = "shopIcon",
            ACHIEVEMENTS_BTN = "achievementsIcon",
            WORLDS_BTN = "worldsIcon",
            LEADERBOARD_BTN = "worldsIcon",
            SHOWER_BTN = "showerIcon",
            FOOD_BTN = "foodIcon",
            PET_BTN = "petIcon",
            CLOSE_BTN = "closeIcon",
            MINIGAME_BTN = "minigameIcon",
            PAUSE_BTN = "pauseIcon",
            CLOCK_ICON = "clockIcon",
            LVL_ICON = "lvlIcon",
            MATH_ICON = "mathIcon",
            OVERLAY_BACKGROUND = "overlay_back",
            BTN_UP = "btn_up",
            BTN_DOWN = "btn_down";
    public static final String FALLBACK_TEXTURE = "fallback_texture.jpg";
    public static final String EXERCISE_BACKGROUND = "exercise_back";

    public static final String PIG_HEAD = "pig_head",
            PIG_HAIR = "pig_hair",
            PIG_EAR = "pig_ear",
            PIG_EYE = "pig_eyes",
            PIG_MOUTH = "pig_mouth",
            PIG_NOSE = "pig_nose",
            PIG_BODY = "pig_body",
            PIG_ARM = "pig_arm",
            PIG_HAND = "pig_hand",
            PIG_LEG = "pig_leg",
            PIG_FOOT = "pig_foot";

    public static final String HAND = "hand_pet", SHOWER = "shower_head",
            HEARTS = "hearts", SOAP = "soap", SOAP_BUBBLE = "soap_bubble",
            WATER_DROPS = "water_drops", CRUMBS = "crumbs";
    public static final String FOOD_BACON = "food_bacon",
            FOOD_BANANA = "food_banana", FOOD_APPLE = "food_apple";

    public static final int ITEM_COUNT = 9,
            SHOP_ROW_AMOUNT = 3;
    public static final String COIN = "coin",
            SHOP_ITEM = "_shop_item",
            SHOP_ITEM_BACK = "shop_item_back",
            BACKGROUND = "_background.png";

    public static final String BLANK_IMG = "blank_img",
            MENU_BACK = "menu_back",
            WINDOW_BANNER = "window_banner",
            BOUGHT_BACK = "bought_back",
            DISABLED_BACK = "disabled_back";

    public static final String CONSENT_HEADER = "Zustimmung", CONSENT_BODY = "Diese App ist ein Projekt für die Techniche Universität München.\n" +
            "Während dem Spielen werden anonymisiert Daten gesammelt und verarbeitet.";
    public static final String SHOP_HEADER = "Shop";

    public static final String OK = "OK", AGREE = "Ich stimme zu.";

    public static final Color OVERLAY_BACK_COLOR = Color.valueOf("B2B8FF");

    public static final String PROGRESSBAR_FRONT_GREEN = "progressbar_front_green",
            PROGRESSBAR_FRONT_RED = "progressbar_front_red",
            PROGRESSBAR_BACK = "progressbar_back",
            PROGRESSBAR_FRONT = "progressbar_front";

    public static final String CHECKBOX_UNCHECKED = "checkbox_unchecked",
            CHECKBOX_CHECKED = "checkbox_checked";

    public static final String LVL = "Lvl ", BOUGHT = "Gekauft";

    public static final String TREATS_WORK = "treats_worker";
    public static final String TREAT_NOTIFICATION_TITLE = "Item found!";
    public static final String TREAT_NOTIFICATION_TEXT = "Tap me and see what you just found!";
    public static final int TREAT_CHECK_TIME_DELTA = 5; // minutes between checks
    public static final int TREAT_START = 5000;
    public static final int TREAT_INTERVAL = 2500;

    public static final String STEP_COUNT = "step_count", STEP_COUNT_TIME = "step_count_timestamp";

    public static final String STEPS_PROGRESS1 = " von ", STEPS_PROGRESS2 = " Schritten";
    public static final int MAX_STEPS = 10000;

    public static final float BTN_SIZE = CHARACTER_WORLD_WIDTH/8, BTN_SIZE_SMALL = 50;

    public static final float GRAVITY = -1200;

    public static final float PLAYER_COLLISION_DELTA = 50;
    public static final float PLAYER_JUMP_VELOCITY_Y = 1200;

    public static final long HEART_COOLDOWN = 500; // heart cooldown in ms
    public static final long PET_DURATION = 10000;
    public static final long SOAPWATER_COOLDOWN = 700; // in ms
    public static final long FOOD_DURATION = 10000;
    public static final long CRUMB_COOLDOWN = 700;

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

    public static final String ANSWER_TEXT = "Answer: ",
            GAME_OVER = "GAME OVER",
            PAUSE = "PAUSE",
            SCORE = "Score: ",
            MATH_SCORE = "Math Score: ",
            BACK = "BACK",
            CONTINUE = "CONTINUE",
            REPLAY = "PLAY AGAIN";

    public static final String NEW_LINE = "\n";

    public static final int REQUEST_ACTIVITY_RECOG_CODE = 200;
}
