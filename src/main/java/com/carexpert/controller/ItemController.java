package com.carexpert.controller;

import com.carexpert.common.*;
import com.carexpert.entity.Item;
import com.carexpert.entity.Question;
import com.carexpert.service.ItemService;
import com.carexpert.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    QuestionService questionService;

    @RequestMapping("/home")
    public ModelAndView home(ModelAndView mv, Integer parent, String type) {
        List<Item> items = null;
        if (parent == null) {//没有parent则查询顶级目录
            parent = CommonType.NO_PARENT;
            mv.addObject("level", CommonType.ITEM_LEVEL_TOP);
            items = itemService.findByParent(parent);
        } else {
            System.out.println(itemService.findById(parent));
            Integer childLevel = itemService.getChildLevel(parent);
            mv.addObject("level", childLevel);
            if (childLevel.equals(CommonType.ITEM_LEVEL_FILE)) {
                items = StringUtils.isEmpty(type) ? null : itemService.findFile(parent, type);
            } else {
                items = itemService.findByParent(parent);
            }
        }
        mv.addObject("items", items);
        mv.addObject("parent", parent);
        mv.addObject("type", type);
        mv.setViewName("home");
        return mv;
    }

    @RequestMapping("/inherit/{parent}")
    public Result items(@PathVariable Integer parent) {
        return Result.success(itemService.findByParent(parent));
    }

    @RequestMapping("/item/add")
    public Result add(String name, Integer parent, Integer level) {
        Item item = new Item();
        item.setName(name);
        item.setLevel(level);
        item.setParent(parent);
        itemService.addItem(item);
        return Result.success(null);
    }

    @RequestMapping("/item/edit")
    public Result edit(String name, Integer id) {
        Item item = itemService.findById(id);
        if (item != null) {
            item.setName(name);
            itemService.save(item);
            return Result.success(null);
        }
        return Result.fail(500);
    }

    @RequestMapping("/item/delete")
    public Result delete(Integer id) {
        itemService.deleteRecursively(id);
        return Result.success(null);
    }

    @RequestMapping("/upload")
    public Result upload(@NotNull Integer parent, @NotNull MultipartFile file, Integer quality) throws Exception {
        String filename = file.getOriginalFilename().toLowerCase();
        String type = CommonType.getFileType(filename);
        System.out.println("upload:" + filename);
        if (type == null) {
            return Result.fail("不支持的文件格式");
        }
        File dest = new File(CommonUtil.getFilePath(type, filename));
        if (dest.exists()) {
            return Result.fail("文件已存在");
        }
        System.out.println(dest.getAbsolutePath());
        file.transferTo(dest);
        Item item = new Item();
        item.setParent(parent);
        item.setLevel(CommonType.ITEM_LEVEL_FILE);
        item.setName(dest.getName());
        item.setFilename(dest.getAbsolutePath());
        item.setType(type);
        itemService.addItem(item);
        return Result.success(null);
    }

    @RequestMapping("/node/{moduleFlag}")
    @ResponseBody
    public List<NodeVO> node(@PathVariable Integer moduleFlag) {
        System.out.println("query node:" + moduleFlag);
        List<NodeVO> list = itemService.getNodeList(moduleFlag);
        return list;
    }

    @RequestMapping("/content/{itemId}")
    @ResponseBody
    public ContentVO content(@PathVariable Integer itemId) {
        System.out.println("query content:" + itemId);
        List<FileVO> docList = itemService.getFileList(itemId,CommonType.ITEM_TYPE_DOCUMENT);
        List<FileVO> videoList = itemService.getFileList(itemId,CommonType.ITEM_TYPE_VIDEO);
        List<FileVO> imageList = itemService.getFileList(itemId,CommonType.ITEM_TYPE_IMAGE);
        List<Question> questionList = questionService.findByParent(itemId);
        ContentVO vo = new ContentVO();
        vo.setDocument(docList);
        vo.setVideo(videoList);
        vo.setImage(imageList);
        vo.setExam(questionList);
        return vo;
    }

    @RequestMapping("/video/{id}")
    public Result video(@PathVariable Integer id, HttpServletResponse response) throws Exception {

        Item item = itemService.findById(id);
        System.out.println("video" + item);
        if (item == null) {
            return Result.fail("invalid id");
        } else {
            //InputStream in = getClass().getResourceAsStream(item.getPath());
            InputStream in = new FileInputStream(item.getFilename());
            if (in == null) {
                return Result.fail("file not exist");
            }
            //response.setContentType("video/mp4");
            response.setContentType("application/octet-stream");
            //response.setContentLengthLong(new File(item.getPath()).length());
            // response.setHeader("Content-Disposition", "attachment;filename=1.mp4");
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            return Result.success(null);
        }
    }
}
