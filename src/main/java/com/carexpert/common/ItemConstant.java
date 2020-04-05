package com.carexpert.common;

import java.util.Arrays;

public class ItemConstant {
    public static final int NO_PARENT = -1;
    public static final int ITEM_LEVEL_TOP = 0;
    public static final int ITEM_LEVEL_ONE = 1;
    public static final int ITEM_LEVEL_TWO = 2;
    public static final int ITEM_LEVEL_FILE = 3;
    public static final String ITEM_TYPE_DOCUMENT = "document";
    public static final String ITEM_TYPE_VIDEO = "video";
    public static final String ITEM_TYPE_IMAGE = "image";
    public static final String ITEM_TYPE_DIRECTORY = "directory";

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
