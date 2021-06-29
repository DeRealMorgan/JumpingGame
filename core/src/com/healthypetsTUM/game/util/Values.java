package com.healthypetsTUM.game.util;

import com.badlogic.gdx.graphics.Color;

public class Values {
    public static final String DATA_PATH = "dataPath",
            SHARED_PREF = "healthAppPrefs", USER_DATA = "userData";

    public static final String FIRST_START = "firstStart";

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
    public static final int MATH_ATTACHMENT_WIDTH = 200, MATH_ATTACHMENT_HEIGHT = 160;

    public static final int SPACING = 20, SPACING_SMALL = 10, EDGE_DISTANCE = 50, TOP_PADDING_UI_GAME = 100;
    public static final float FADE_OVERLAY_DUR = 0.6f;

    public static final int FONT_BORDER_WIDTH = 0,
            FONT_SIZE_SMALL = 35, FONT_SIZE = 55, FONT_SIZE_BIG = 70;
    public static final Color FONT_COLOR = /*Color.valueOf("E9E6F7")*/ Color.BLACK;

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
            SETTINGS_BTN = "settingsIcon",
            SHOWER_BTN = "showerIcon", SHOWER_BTN_DISABLED = "showerIconDisabled",
            FOOD_BTN = "foodIcon", FOOD_BTN_DISABLED = "foodIconDisabled",
            PET_BTN = "petIcon", PET_BTN_DISABLED = "petIconDisabled",
            CLOSE_BTN = "closeIcon",
            MINIGAME_BTN = "minigameIcon", MINIGAME_BTN_DISABLED = "minigameIconDisabled",
            PAUSE_BTN = "pauseIcon",
            CLOCK_ICON = "clockIcon",
            LVL_ICON = "lvlIcon",
            MATH_ICON = "mathIcon",
            OVERLAY_BACKGROUND = "overlay_back",
            BTN_UP = "btn_up", BTN_DOWN = "btn_down",
            MUSIC_ON = "musicIconOn", MUSIC_OFF = "musicIconOff",
            SOUNDS_ON = "soundsIconOn", SOUNDS_OFF = "soundsIconOff";
    public static final String FALLBACK_TEXTURE = "fallback_texture.jpg";

    public static final String SLIDER_KNOB = "slider_knob", SLIDER_BAR = "slider_bar";

    public static final String PIG_HEAD = "pig_head",
            INVISIBLE_HEAD = "pig_head_invisible",
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
    public static final String FOOD_ITEM = "_food";

    public static final int ITEM_COUNT = 9, FOOD_ITEM_COUNT = 6, WORLD_COUNT = 4,
            SHOP_ROW_AMOUNT = 3, FOOD_ROW_AMOUNT = 2, WORLD_ROW_AMOUNT = 2;
    public static final String COIN = "coin",
            SHOP_ITEM = "_shop_item",
            SHOP_ITEM_ARM = "_shop_item_arm",
            SHOP_ITEM_BACK = "shop_item_back",
            BACKGROUND = "_background.png";

    public static final String BLANK_IMG = "blank_img",
            MENU_BACK = "menu_back",
            UI_BAR_BACK = "ui_bar_back",
            WINDOW_BANNER = "window_banner",
            BOUGHT_BACK = "bought_back",
            EQUIPED_BACK = "bought_equiped",
            DISABLED_BACK = "disabled_back",
            DISABLED_IMG = "disabled_img";

    public static final String CONSENT_HEADER = "Zustimmung", CONSENT_BODY = "Diese App ist ein Projekt für die Technische Universität München. " +
            "Während dem Spielen werden anonymisiert Daten gesammelt und verarbeitet.";
    public static final String HEALTH_HEADER = "Schritte", HEALTH_BODY = "Dein Haustier möchte gerne wissen, wie viele " +
            "Schritte du täglich läufst. Dafür musst du dich jetzt mit deinem Google Konto verbinden.";
    public static final String SHOP_HEADER = "Shop", WORLDS_HEADER = "Welten", SETTINGS_HEADER = "Einstellungen",
            SHOP_FOOD_HEADER = "Nahrung", MATH_HEADER = "Aufgabe";

    public static final String TREATS_HEADER = "Item gefunden", TREATS_BODY = "Du bist gerade an diesem Item vorbei gelaufen. " +
            "Löse schnell 3 Matheaufgaben, um es einzusammeln.";

    public static final String OK = "OK", AGREE = "Ich stimme zu.";

    public static final Color OVERLAY_BACK_COLOR = Color.valueOf("B2B8FF");

    public static final String PROGRESSBAR_FRONT_GREEN = "progressbar_front_green",
            PROGRESSBAR_FRONT_RED = "progressbar_front_red",
            PROGRESSBAR_BACK = "progressbar_back",
            PROGRESSBAR_FRONT = "progressbar_front",
            PROGRESSBAR_FRONT_EMPTY = "progressbar_front_empty";

    public static final String CHECKBOX_UNCHECKED = "checkbox_unchecked",
            CHECKBOX_CHECKED = "checkbox_checked";

    public static final String LVL = "Lvl ", BOUGHT = "Gekauft";

    public static final String TREATS_WORK = "treats_worker";
    public static final String TREAT_NOTIFICATION_TITLE = "Item gefunden!";
    public static final String TREAT_NOTIFICATION_TEXT = "Finde heraus, was du gefunden hast!";
    public static final String TREAT_SUCCESS = "Item gesammelt",
            TREAT_FAIL = "Item verloren";
    public static final String TREAT_LOST = "Du findest das Item bestimmt später noch einmal!";
    public static final String TREAT_COLLECTED = "Sehr gut! Du kannst das Item jetzt im Shop finden.";
    public static final int TREAT_CHECK_TIME_DELTA = 60*15; // seconds between checks
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
    public static final long PET_DURATION = 8000;
    public static final long SOAPWATER_COOLDOWN = 700; // in ms
    public static final long FOOD_DURATION = 8000;
    public static final long CRUMB_COOLDOWN = 700;

    public static final long REFRESH_STEPS_INTERVAL = 1000*30;

    public static final float TILE_DISTANCE_Y = 600;

    public static final float CAM_INTERPOLATION_STEP = 5f;

    public static final int FPS_IN_MILLIS = 16;

    public static final int ACCELATE_X = 200;
    public static final float VELOCITY_TRESHOLD_X = 0.1f; // if detected velocityX is too mall, it should be ignored
    public static final float DESKTOP_VELOCITY_X = 2f; // velocity for desktop testing

    public static final String ADD_EXERCISE_FILE = "exercise_add.txt",
            SUB_EXERCISE_FILE = "exercise_sub.txt",
            MUL_EXERCISE_FILE = "exercise_mul.txt",
            DIV_EXERCISE_FILE = "exercise_div.txt";

    public static final String ANSWER_TEXT = "Lösung: ",
            GAME_OVER = "GAME OVER",
            PAUSE = "PAUSE",
            SCORE = "Score: ",
            MATH_SCORE = "Math Score: ",
            BACK = "ZURÜCK",
            CONTINUE = "WEITER SPIELEN",
            REPLAY = "ERNEUT SPIELEN";

    public static final String NEW_LINE = "\n";

    public static final int REQUEST_ACTIVITY_RECOG_CODE = 200, GOOGLE_FIT_REQUEST_CODE = 205;

    public static final int MAX_PET_AMOUNT = 2, MAX_PLAY_AMOUNT = 4, MAX_FOOD_AMOUNT = 4, MAX_SHOWER_AMOUNT = 2;
}
