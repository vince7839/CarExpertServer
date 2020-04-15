package com.carexpert.common;

import java.util.Arrays;

public class CommonType {
    public static final int NO_PARENT = -1;
    public static final int ITEM_LEVEL_TOP = 0;
    public static final int ITEM_LEVEL_ONE = 1;
    public static final int ITEM_LEVEL_TWO = 2;
    public static final int ITEM_LEVEL_FILE = 3;
    public static final String ITEM_TYPE_DOCUMENT = "document";
    public static final String ITEM_TYPE_VIDEO = "video";
    public static final String ITEM_TYPE_IMAGE = "image";
    public static final String ITEM_TYPE_DIRECTORY = "directory";

    public static final int USER_TYPE_NORMAL = 100;
    public static final int USER_TYPE_ADMIN = 101;
    public static final int USER_TYPE_SUPER = 102;

    public static final int MODULE_FLAG_1 = 1 << 1;
    public static final int MODULE_FLAG_2 = 1 << 2;
    public static final int MODULE_FLAG_3 = 1 << 3;
    public static final int MODULE_FLAG_4 = 1 << 4;
    public static final int MODULE_FLAG_5 = 1 << 5;
    public static final int MODULE_FLAG_6 = 1 << 6;
    public static final int MODULE_FLAG_7 = 1 << 7;
    public static final int MODULE_FLAG_8 = 1 << 8;
    public static final int MODULE_FLAG_9 = 1 << 9;
    public static final int MODULE_FLAG_10 = 1 << 10;
    public static final int MODULE_FLAG_11 = 1 << 11;
    public static final int MODULE_FLAG_12 = 1 << 12;
    public static final int MODULE_FLAG_13 = 1 << 13;

    public static final int RESET_TYPE_PHONE = 0;
    public static final int RESET_TYPE_PASSWORD = 1;
//    public static final int VIDEO_QUALITY_HIGH = 0;
//    public static final int VIDEO_QUALITY_MIDDLE = 1;
//    public static final int VIDEO_QUALITY_LOW = 2;

    public static final String[] documentType = new String[]{".ppt",".pdf"};
    public static final String[] videoType = new String[]{".mp4"};
    public static final String[] imageType = new String[]{".jpg",".jpeg",".png"};

    public static String getFileType(String filename){
        if (!filename.contains(".")){
            return null;
        }
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (Arrays.asList(documentType).contains(suffix)){
            return ITEM_TYPE_DOCUMENT;
        }else if (Arrays.asList(videoType).contains(suffix)){
            return ITEM_TYPE_VIDEO;
        }else if (Arrays.asList(imageType).contains(suffix)){
            return ITEM_TYPE_IMAGE;
        }else {
            return null;
        }
    }

}
