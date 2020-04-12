package com.carexpert.common;

import com.carexpert.entity.Item;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.File;

public class CommonUtil {

    public static boolean isOptionRight(String option, Integer answer) {
        Integer builder = 0;
        if (StringUtils.isEmpty(answer)){
            return false;
        }else if ("A".equals(option)) {
            builder = answer & 0x01;
        }else if ("B".equals(option)) {
            builder = answer & 0x10;
        }else if ("C".equals(option)) {
            builder = answer & 0x100;
        }else if ("D".equals(option)) {
            builder = answer & 0x1000;
        }
        return builder != 0;
    }

    public static boolean isModuleEnable(Integer permission,Integer flag){
        if(permission == null || flag == null){
            return false;
        }
        return (permission & (1 << flag)) != 0;
    }

    public static String getFileUrl(Item item){
        String url = "";
        if (CommonType.ITEM_TYPE_DOCUMENT.equals(item.getType())){
            url = "http://localhost:8080/carexpert/file/document/"+item.getFilename();
        }else if (CommonType.ITEM_TYPE_IMAGE.equals(item.getType())){
            url = "http://localhost:8080/carexpert/file/image/"+item.getFilename();
        }
        return url;
    }

    public static String getVideoUrl(String filename,int quality){
        String s = quality == CommonType.VIDEO_QUALITY_HIGH ?
                "high":(quality == CommonType.VIDEO_QUALITY_MIDDLE ? "middle" : "low");
        return "http://localhost:8080/carexpert/video/" + s + "/"+filename;
    }



    private static String getFilePath(String type,String filename,int quality){
        String base = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"/file";
        String prefix;
        if (CommonType.ITEM_TYPE_DOCUMENT.equals(type)){
            prefix = "/document";
        }else if (CommonType.ITEM_TYPE_IMAGE.equals(type)){
            prefix = "/image";
        }else {
            prefix = "/video" + (quality == 0 ? "/high"
                    : (quality == 1 ? "/middle" : "/low"));
        }
        File targetDir = new File(base,prefix);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        String path = targetDir.getAbsolutePath() +"/"+ filename;
        System.out.println("getFilePath:"+path);
        return path;
    }

    public static String getFilePath(String type,String filename){
        return getFilePath(type, filename,0);
    }

    public static String getVideoPath(String filename,int quality){
        return getFilePath(CommonType.ITEM_TYPE_VIDEO, filename,quality);
    }

    public static void deleteFile(Item item){
        if (CommonType.ITEM_TYPE_VIDEO.equals(item.getType())){
            int[] qualities = new int[]{CommonType.VIDEO_QUALITY_HIGH,
                    CommonType.VIDEO_QUALITY_MIDDLE,CommonType.VIDEO_QUALITY_LOW};
            for (int q:qualities){
                String path = getVideoPath(item.getFilename(),q);
                File file = new File(path);
                file.delete();
            }
        }else {
            File file = new File(getFilePath(item.getType(),item.getFilename()));
            file.deleteOnExit();
        }
    }
}
