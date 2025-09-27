package com.example.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.User;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    @PostMapping("/add")
    public Result add(@RequestBody User user) {//@RequestBody 接受前端传来的json参数
        userService.add(user);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody User user) {//@RequestBody 接受前端传来的json参数
        userService.update(user);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {//@RequestBody 接受前端传来的路径参数
        userService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<User> list) {//@RequestBody 接受前端传来的JSON数组
        userService.deleteBatch(list);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectALL(User user) {
        List<User> userList=userService.selectAll(user);
        return Result.success(userList);
    }
    /*
     *分页查询
     * 当前页码，每页个数
     * pageNum，pageSize，
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             User user) {
        PageInfo<User> pageInfo= userService.selectPage(pageNum,pageSize,user);
        return Result.success(pageInfo);//返回的是分页的对象
    }

    /*
    *  ids: 1,2,3
    * */
    @GetMapping("/export")
    public void exportData(User user ,HttpServletResponse response) throws Exception {
        String ids = user.getIds();
        if(StrUtil.isNotBlank(ids)){
            String[] idsArr = ids.split(",");
            user.setIdsArr(idsArr);
        }
        //数据的导出
        //1.拿到所有数据吧
        List<User> list=userService.selectAll(user);
       //2.构建对象
        ExcelWriter writer=ExcelUtil.getWriter(true);
        //3. set 中文表头
        writer.addHeaderAlias("username","账号");

        writer.addHeaderAlias("name","名称");
        writer.addHeaderAlias("phone","电话");
        writer.addHeaderAlias("email","邮箱");
        writer.setOnlyAlias(true);
         //4.写出数据到writer
        writer.write(list);
        //5.是指输出文件以及输出流的头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("管理员信息",StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");
        //6.写出到输出流，并关闭writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
        os.close();

    }
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception {
       // 1.拿到输入流
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        reader.addHeaderAlias("账号","username");
        reader.addHeaderAlias("名称","name");
        reader.addHeaderAlias("电话","phone");
        reader.addHeaderAlias("邮箱","email");

        List<User> list = reader.readAll(User.class);

        for (User user : list) {
            userService.add(user);
        }
        return Result.success();
    }

}
