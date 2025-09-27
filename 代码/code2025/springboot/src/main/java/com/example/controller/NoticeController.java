package com.example.controller;

import com.example.common.Result;
import com.example.entity.Notice;
import com.example.service.NoticeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    @Resource
    private NoticeService noticeService;

    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) {//@RequestBody 接受前端传来的json参数
        noticeService.add(notice);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Notice notice) {//@RequestBody 接受前端传来的json参数
        noticeService.update(notice);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {//@RequestBody 接受前端传来的路径参数
        noticeService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/list")
    public List<Notice> list() {
        return noticeService.selectAll();
    }

    @GetMapping("/selectAll")
    public Result selectALL(Notice notice) {
        List<Notice> noticeList=noticeService.selectAll(notice);
        return Result.success(noticeList);
    }
    /*
     *分页查询
     * 当前页码，每页个数
     * pageNum，pageSize，
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Notice notice) {
        PageInfo<Notice> pageInfo= noticeService.selectPage(pageNum,pageSize,notice);
        return Result.success(pageInfo);//返回的是分页的对象
    }

}
