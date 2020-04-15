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
            url = "http://localhost:8080/carexpert/document/"+item.getFilename();
        }else if(CommonType.ITEM_TYPE_VIDEO.equals(item.getType())){
            url = "http://localhost:8080/carexpert/video/"+item.getFilename();
        }else if (CommonType.ITEM_TYPE_IMAGE.equals(item.getType())){
            url = "http://localhost:8080/carexpert/image/"+item.getFilename();
        }
        return url;
    }

    public static String getFilePath(String type,String filename){
        String base = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"/static";
        String prefix;
        if (CommonType.ITEM_TYPE_DOCUMENT.equals(type)){
            prefix = "/document";
        }else if (CommonType.ITEM_TYPE_IMAGE.equals(type)){
            prefix = "/image";
        }else {
            prefix = "/video";
        }
        File targetDir = new File(base,prefix);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        String path = targetDir.getAbsolutePath() +"/"+ filename;
        System.out.println("getFilePath:"+path);
        return path;
    }

    public static String getCoverUrl(String filename){
        return "http://localhost:8080/carexpert/cover/"+filename;
    }

    public static void deleteFile(Item item){
            File file = new File(getFilePath(item.getType(),item.getFilename()));
            file.deleteOnExit();
    }
}
