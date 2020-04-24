package com.carexpert.common;

import com.carexpert.entity.Item;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class CommonUtil {



    public static String getHost() {
        String HOST = getProperties().getProperty("host");
        if(StringUtils.isEmpty(HOST)){
            HOST = "47.99.60.167:11111";
        }
        return HOST;
    }

    public static Properties getProperties() {
        String filename = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "/config.properties";
        File file = new File(filename);
        Properties properties = new Properties();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (InputStream in = new FileInputStream(file)) {
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

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
            url = "http://"+getHost()+"/carexpert/document/"+item.getFilename();
        }else if(CommonType.ITEM_TYPE_VIDEO.equals(item.getType())){
            url = "http://"+getHost()+"/carexpert/video/"+item.getFilename();
        }else if (CommonType.ITEM_TYPE_IMAGE.equals(item.getType())){
            url = "http://"+getHost()+"/carexpert/image/"+item.getFilename();
        }
        return url;
    }

    public static String getFilePath(String type,String filename){
        String base = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"/static";
        String prefix = "";
        if (CommonType.ITEM_TYPE_DOCUMENT.equals(type)){
            prefix = "/document";
        }else if (CommonType.ITEM_TYPE_IMAGE.equals(type)){
            prefix = "/image";
        }else if (CommonType.ITEM_TYPE_VIDEO.equals(type)){
            prefix = "/video";
        }else if ("cover".equals(type)){
            prefix = "/cover";
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
        if (StringUtils.isEmpty(filename)) {
            return "";
        }
        return "http://"+getHost()+"/carexpert/cover/"+filename;
    }

    public static void deleteFile(Item item){
            File file = new File(getFilePath(item.getType(),item.getFilename()));
            file.deleteOnExit();
    }

    public static String getUniqueFilename(String filename){
        File file = new File(filename);
        System.out.println("file exists:"+file.exists());
        int last = filename.lastIndexOf(".");
        String prefix = filename.substring(0,last);
        String ext = filename.substring(filename.lastIndexOf("."));
        int i = 1;
        String temp = filename;
        System.out.println("prefix:"+prefix+" ext:" + ext);
        while(file.exists()){
            temp = prefix + "(" + i + ")" + ext;
            System.out.println("try:"+temp);
            file = new File(temp);
            i++;
        }
        return temp;
    }

    public static TreeNode buildDirectory(Item item){
        TreeNode vo = new TreeNode();
        vo.setTitle(item.getName());
        vo.setId(item.getId());
       // vo.setChildren(new ArrayList<TreeNode>());
        return vo;
    }

}
