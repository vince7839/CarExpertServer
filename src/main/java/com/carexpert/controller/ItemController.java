package com.carexpert.controller;

import com.carexpert.common.*;
import com.carexpert.dao.TagRepository;
import com.carexpert.entity.Item;
import com.carexpert.entity.Question;
import com.carexpert.entity.User;
import com.carexpert.service.ItemService;
import com.carexpert.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@RestController
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    QuestionService questionService;

    @Autowired
    TagRepository tagRepository;

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

    @RequestMapping("/file")
    public PageVO file(Integer page,Integer limit,Integer parent, String type) {
        Page<Item> result = itemService.findFile(page-1,limit,parent,type);
        PageVO vo = new PageVO();
        vo.setCount(result.getTotalElements());
        vo.setPage(page);
        vo.setData(result.getContent());
        return vo;

    }

    @RequestMapping("/item/add")
    public Result add(String name, Integer parent, Integer level) {
        System.out.println("add: parent " + parent + " name " + name);
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
    public Result upload(@NotNull Integer parent, @NotNull MultipartFile file, String op, Integer id) throws Exception {
        String filename = file.getOriginalFilename().toLowerCase().trim();
        String type = "cover".equals(op) ? "cover" : CommonType.getFileType(filename);
        System.out.println("upload:" + filename);
        if (type == null) {
            return Result.fail("不支持的文件格式:" + type);
        }
        File dest = new File(CommonUtil.getFilePath(type, filename));
        if (dest.exists()) {
            String uniqueName = CommonUtil.getUniqueFilename(dest.getAbsolutePath());
            dest = new File(uniqueName);
            filename = dest.getName();
        }
        System.out.println(dest.getAbsolutePath());
        file.transferTo(dest);
        if ("cover".equals(op)) {
            Item item = itemService.findById(id);
            if (item != null) {
                item.setCover(filename);
                itemService.save(item);
            }
        } else {
            Item item = new Item();
            item.setParent(parent);
            item.setLevel(CommonType.ITEM_LEVEL_FILE);
            item.setName(dest.getName());
            item.setFilename(filename);
            item.setType(type);
            itemService.addItem(item);
        }

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
        List<FileVO> docList = itemService.getFileList(itemId, CommonType.ITEM_TYPE_DOCUMENT);
        List<FileVO> videoList = itemService.getFileList(itemId, CommonType.ITEM_TYPE_VIDEO);
        List<FileVO> imageList = itemService.getFileList(itemId, CommonType.ITEM_TYPE_IMAGE);
        List<Question> questionList = questionService.findByParent(itemId);
        ContentVO vo = new ContentVO();
        vo.setDocument(docList);
        vo.setVideo(videoList);
        vo.setImage(imageList);
        vo.setExam(questionList);
        return vo;
    }

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

    @RequestMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable Integer id, ModelAndView mv) {
        Item item = itemService.findById(id);
        System.out.println("detail:" + item);
        if (item == null) {
            mv.setViewName("error");
        }
        String type = item.getType();
        if (CommonType.ITEM_TYPE_VIDEO.equals(type) || CommonType.ITEM_TYPE_DOCUMENT.equals(type)) {
            List<String> list = new ArrayList<>();
            if (item.getTag() != null) {
                list = Arrays.asList(item.getTag().split(","));
                System.out.println("tag:" + list);
            }
            mv.setViewName("detail");
            mv.addObject("id", id);
            mv.addObject("cover", CommonUtil.getCoverUrl(item.getCover()));
            mv.addObject("name", item.getName());
            mv.addObject("tags", list);
            mv.addObject("allTags", tagRepository.findAll());
        } else if (CommonType.ITEM_TYPE_IMAGE.equals(type)) {
            mv.setViewName("redirect:" + CommonUtil.getFileUrl(item));
        } else {
            mv.setViewName("error");
        }
        return mv;
    }

    @RequestMapping("/detail/save")
    @ResponseBody
    public Result saveDetail(Integer id, String name, String[] tags) {
        System.out.println("save detail:" + Arrays.toString(tags));
        Item item = itemService.findById(id);
        if (item == null) {
            return Result.fail("no such id");
        }
        item.setName(name);
        String tag = "";
        for (String t : tags) {
            tag = tag + t + ",";
        }
        item.setTag(tag);
        itemService.save(item);
        return Result.success(null);
    }

    @RequestMapping("/version")
    public ModelAndView version(ModelAndView mv) {
        Properties properties = CommonUtil.getProperties();
        String version = properties.getProperty("version");
        mv.addObject("version", version);
        mv.setViewName("version");
        return mv;
    }

    @RequestMapping("/publish")
    public String publish(MultipartFile file, String version) {
        System.out.println("publish:" + version);
        String targetPath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "/static/CarExpert.exe";
        File target = new File(targetPath);
        target.deleteOnExit();
        String filename = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "/config.properties";
        try (OutputStream out = new FileOutputStream(filename)) {
            file.transferTo(target);
            Properties properties = CommonUtil.getProperties();
            properties.setProperty("version", version);
            properties.store(out, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/version";
    }

    @RequestMapping("/vernum")
    @ResponseBody
    public Result vernum() {
        Properties properties = CommonUtil.getProperties();
        String num = properties.getProperty("version");
        return Result.success(num);
    }

    @RequestMapping("/module/{moduleFlag}")
    public ModelAndView home2(@PathVariable Integer moduleFlag, ModelAndView mv) {
        Integer moduleId = itemService.findByFlag(moduleFlag).getId();
        Item module = itemService.findById(moduleId);
        mv.setViewName("home2");
        mv.addObject("module", module);
        return mv;
    }

    @RequestMapping("/directory/{id}")
    public ModelAndView directory(@PathVariable Integer id,ModelAndView mv){
        mv.addObject("parent",id);
        mv.setViewName("directory2");
        return mv;
    }

    @RequestMapping("/tree/{moduleId}")
    @ResponseBody
    public Result directory(@PathVariable Integer moduleId) {
        System.out.println("get module directory:" + moduleId);
        List<Item> oneNodes = itemService.findByParent(moduleId);
        List<TreeNode> result = buildTree(moduleId);
        //  List<TreeNode> result = new ArrayList<>();

//        for (Item one : oneNodes) {
//            TreeNode topDirectory = CommonUtil.buildDirectory(one);
//            List<Item> twoNodes = itemService.findByParent(one.getId());
//            List<TreeNode> children = new ArrayList<>();
//            for (Item two : twoNodes) {
//                TreeNode twoDirectory = CommonUtil.buildDirectory(two);
//                children.add(twoDirectory);
//            }
//            topDirectory.setChildren(children);
//            result.add(topDirectory);
//        }
        return Result.success(result);
    }

    private List<TreeNode> buildTree(Integer parent) {

        List<Item> items = itemService.findByParent(parent);
        List<TreeNode> nodes = new ArrayList<>();

        for (Item item : items) {
            TreeNode node = new TreeNode();
            node.setTitle(item.getName());
            node.setId(item.getId());
            Integer level = item.getLevel();
            node.setLevel(level);
            if (level != null && level < CommonType.ITEM_LEVEL_TWO) {
                List<TreeNode> childNode = buildTree(item.getId());
                node.setChildren(childNode);
            }
            nodes.add(node);
        }
        return nodes;
    }
}
