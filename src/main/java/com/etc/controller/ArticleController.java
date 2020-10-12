package com.etc.controller;

import com.etc.entity.Article;
import com.etc.feigninters.UserFeignClient;
import com.etc.service.ArticleService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;

import java.util.Map;
import java.util.function.ObjIntConsumer;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    ArticleService articleService;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private UserFeignClient userFeignClient;
    @RequestMapping("/get/{articleId}")
    public Article getArticle(@PathVariable Integer articleId){
        return articleService.getById(articleId);
    }
    @HystrixCommand(fallbackMethod = "getDefaultUser")
    @RequestMapping("/getuser/{uid}")
    public Map<String,Object> getUser(@PathVariable Integer uid){
        Map<String,Object> map=restTemplate.getForObject("http://localhost:8762/user/get/"+uid,Map.class);
        return map;
    }
    private Map<String,Object> getDefaultUser(Integer uid){
        Map<String,Object> map=new HashMap<>();
        map.put("uname","-1");
        map.put("password","-1");
        return  map;
    }

    @GetMapping("/getdetail/{articleId}")
    public Map<String,Object> getArticleDetail(@PathVariable Integer articleId){
        Article a=articleService.getById(articleId);
        Map<String, Object> map=userFeignClient.getUserById(a.getAuthorId());
        map.put("articleTitle",a.getArticleTitle());
        map.put("articleId",a.getArticleId());
        map.put("articleContent",a.getArticleContent());
        map.put("articleDt",a.getArticleDt());
        return map;
    }

}
