package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import com.example.common.Result;
import com.example.exception.CustomerException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/*
处理文件
 */
@RestController
@RequestMapping("/files")
public class FileController {
    /*
      文件上传
      下载路径：http://localhost:9999/files/download/banner背景.jpg
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        //找到文件的位置
        String filePath = System.getProperty("user.dir") + "/files/";
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        byte[] bytes = file.getBytes();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        FileUtil.writeBytes(bytes, filePath + fileName);
        String url = "http://localhost:9999/files/download/" + fileName;
        return Result.success(url);
    }

    /*
      文件下载
      下载路径：http://localhost:9999/files/download/banner背景.jpg
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws Exception {
        String filePath = System.getProperty("user.dir") + "/files/";//C:\Users\xuzhihao\Desktop\毕设项目\代码\code2025\files
        String realPath = filePath + fileName;
        boolean exist = FileUtil.exist(realPath);
        if (!exist) {
            throw new CustomerException("文件不存在");
        }
        byte[] bytes = FileUtil.readBytes(realPath);

        ServletOutputStream os = response.getOutputStream();
        os.write(bytes);
        os.flush();
        os.close();
    }
    /**
      * wang-editor编辑器文件上传接口
      */
    @PostMapping("/wang/upload")
    public Map<String,Object> wangEditorUpload(MultipartFile file) {
        String flag = System.currentTimeMillis() + "";
        String fileName = file.getOriginalFilename();
        try {
            String filePath = System.getProperty("user.dir") + "/files/";
            FileUtil.writeBytes(file. getBytes(),filePath + flag+"-"+fileName);
            System.out.println(fileName + "--上传成功");
            Thread.sleep (1L);
        } catch (Exception e) {
            System.err.println(fileName+"--文件上传失败");
        }
            String http = "http://Localhost:9999/files/download/";
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("errno", 0);
            resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", http + flag + "-" + fileName)));
            return resMap;
    }
    
    /**
     * 上传背景图片
     */
    @PostMapping("/background/upload")
    public Result uploadBackground(@RequestParam("background") MultipartFile file) throws Exception {
        //找到文件的位置
        String filePath = System.getProperty("user.dir") + "/files/backgrounds/";
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        byte[] bytes = file.getBytes();
        String fileName = "bg_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        FileUtil.writeBytes(bytes, filePath + fileName);
        String url = "http://localhost:9999/files/download/backgrounds/" + fileName;
        
        Map<String, Object> data = new HashMap<>();
        data.put("url", url);
        data.put("fileName", fileName);
        
        return Result.success(data);
    }
}
