package com.example.controller;

import com.example.common.Result;
import com.example.entity.Category;
import com.example.service.CategoryService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    CategoryService categoryService;

    @PostMapping("/add")
    public Result add(@RequestBody Category category) {//@RequestBody 接受前端传来的json参数
        categoryService.add(category);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Category category) {//@RequestBody 接受前端传来的json参数
        categoryService.update(category);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {//@RequestBody 接受前端传来的路径参数
        categoryService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectALL(Category category) {
        List<Category> categoryList=categoryService.selectAll(category);
        return Result.success(categoryList);
    }
    /*
     *分页查询
     * 当前页码，每页个数
     * pageNum，pageSize，
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Category category) {
        PageInfo<Category> pageInfo= categoryService.selectPage(pageNum,pageSize,category);
        return Result.success(pageInfo);//返回的是分页的对象
    }

}
