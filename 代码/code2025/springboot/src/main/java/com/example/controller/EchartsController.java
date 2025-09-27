package com.example.controller;

import com.example.common.Result;
import com.example.entity.Category;
import com.example.entity.Introduction;
import com.example.entity.User;
import com.example.service.CategoryService;
import com.example.service.IntroductionService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Resource
    public CategoryService categoryService;
    @Resource
    public IntroductionService introductionService;
    @Resource
    public UserService userService;

        @GetMapping("/pie")
        public Result pie(){
            List<Map<String,Object>> list = new ArrayList<>();
            //查询所有分类信息
            List<Category> categories=categoryService.selectAll(new Category());
            //查询所有帖子信息
            List<Introduction> introductions=introductionService.selectAll(new Introduction());
            for(Category category:categories){
                long count=introductions.stream().filter(x->category.getId().equals(x.getCategoryId())).count();
                Map<String,Object> map=new HashMap<>();
                map.put("name",category.getTitle());
                map.put("value",count);
                list.add(map);
            }
            return Result.success(list);
        }
        @GetMapping("/bar")
        public Result bar(){
            Map<String,Object> resultmap=new HashMap<>();
            List<String> xList=new ArrayList<>();
            List<Long> yList=new ArrayList<>();
//            Map<String,Long> map=new HashMap<>();

            List<User> users=userService.selectAll(new User());

            List<Introduction> introductions=introductionService.selectAll(new Introduction());
            for(User user:users){
                long count =introductions.stream().filter(x->user.getId().equals(x.getUserId())).count();
                xList.add(user.getName());
                yList.add(count);
//                map.put(user.getName(),count);
            }
//            LinkedHashMap<String,Long> collectMap= map.entrySet().stream()
//                            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
//                            .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(e1,e2)->e1,LinkedHashMap::new));
//            for (String key : collectMap.keySet()){
//                xList.add(key);
//                yList.add(collectMap.get(key));
//            }
            resultmap.put("xAxis",xList);
            resultmap.put("yAxis",yList);
            return Result.success(resultmap);
        }

}
