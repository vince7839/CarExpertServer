package com.carexpert.controller;

import com.carexpert.common.CommonType;
import com.carexpert.common.NodeVO;
import com.carexpert.common.Result;
import com.carexpert.entity.Item;
import com.carexpert.service.ItemService;
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
    public Result upload(@NotNull Integer parent, @NotNull MultipartFile file) throws Exception {
        String resource = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        File dir = new File(resource + "/static");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String name = file.getOriginalFilename().toLowerCase();
        String type = CommonType.getFileType(name);
        System.out.println("name:" + name);
        if (type == null) {
            System.out.println("不支持的格式");
            return Result.fail("不支持的文件格式");
        } else {
            File dest = new File(dir.getAbsolutePath(), name);
            if (dest.exists()) {
                System.out.println("文件存在");
                return Result.fail("文件已存在");
            } else {
                System.out.println(dest.getName());
                file.transferTo(dest);
                Item item = new Item();
                item.setParent(parent);
                item.setName(name);
                item.setLevel(CommonType.ITEM_LEVEL_FILE);
                item.setPath(dest.getAbsolutePath());
                item.setType(type);
                itemService.addItem(item);
            }
        }
        return Result.success(null);
    }

    @RequestMapping("/node/{top}")
    @ResponseBody
    public List<NodeVO> node(@PathVariable Integer top) {
        List<NodeVO> list = itemService.getNodeList(top);
        return list;
    }


    @RequestMapping("/video/{id}")
    public Result video(@PathVariable Integer id, HttpServletResponse response) throws Exception {

        Item item = itemService.findById(id);
        System.out.println("video"+item);
        if (item == null) {
            return Result.fail("invalid id");
        } else {
            //InputStream in = getClass().getResourceAsStream(item.getPath());
            InputStream in = new FileInputStream(item.getPath());
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
