package com.example.maratbe.secretsv2;

import android.graphics.Color;

/**
 * Created by MARATBE on 12/24/2017.
 */

public interface Constants {
    int ID_TAG_LAYOUT = 1;
    int ID_TEXT_SCROLL = 2;
    int ID_STATUS_LAYOUT = 3;
    int ID_ACTION_LAYOUT = 4;
    int ID_GO_UP_BTN = 5;
    int ID_SEND_BTN = 45;
    int ID_COMMENTS_IMG = 8;

    int ID_NUM_OF_COMMENTS_TXT = 8;
    int ID_SHARE_BTN = 9;
    int ID_PUT_LIKE_BTN = 10;
    int ID_PUT_COMMENT_BTN = 11;

    int ID_EMOJI_NUM_OF_LOL_IMG = 13;
    int ID_EMOJI_NUM_OF_HEART_IMG = 14;
    int ID_EMOJI_NUM_OF_SAD_IMG = 15;
    int ID_EMOJI_NUM_OF_ANGRY_IMG = 16;
    int ID_STAR_IMG_TOP = 17;
    int ID_RATING_TXT_TOP = 18;
    int ID_EMOJI_LOL_IMG = 19;
    int ID_EMOJI_HEART_IMG = 20;
    int ID_EMOJI_SAD_IMG = 21;
    int ID_EMOJI_ANGRY_IMG = 22;
    int ID_THEME_TITLE_TXT = 23;
    int[] ID_THEME_NAME_RADIO = new int[]{24, 25, 26, 27};
    int[] ID_THEME_NAME_BTN = new int[]{28, 29, 30, 31};
    int ID_SECRET_BTN = 32;
    int ID_THOUGHT_BTN = 33;
    int ID_PANEL_LAYOUT = 34;



    int TEXT_SIZE = 20;
    int TITLE_SIZE = 28;
    int TEXT_COLOR = Color.DKGRAY;
    int TITLE_COLOR = Color.BLUE;
    int THEME_NIGHT = 0;
    int THEME_WINTER = 1;
    int THEME_SPRING = 2;
    int THEME_SUMMER = 3;
    int THEME_AUTUMN = 4;

    int SECRET_START_VALUE = 10;
    int THOUGHT_START_VALUE = 0;
    int[] BUTTONS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
    int[] IMAGES = new int[]{20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39};
    int[] EXPEND_BUTTONS = new int[]{40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59};
    int[] PUT_LIKE_BUTTONS = new int[]{60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79};
    int[] PUT_SAD_BUTTONS = new int[]{80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 5, 99};
    int[] PUT_ANGRY_BUTTONS = new int[]{100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119};
    int[] PUT_LOL_BUTTONS = new int[]{120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139};


}
