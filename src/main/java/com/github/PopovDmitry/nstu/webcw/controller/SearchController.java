package com.github.PopovDmitry.nstu.webcw.controller;

import com.github.PopovDmitry.nstu.webcw.model.Article;
import com.github.PopovDmitry.nstu.webcw.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class SearchController {

    private final Logger logger = LoggerFactory.getLogger(SearchController.class);

    private final ArticleService articleService;

    @Autowired
    public SearchController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/search/")
    public String searchArticles(@RequestParam("query") String query) {
        Optional<List<Article>> articles = articleService.searchArticles(query);
        if(articles.isPresent()) {
            for (int i = 0; i < articles.get().size(); i++) {
                System.out.println(articles.get().get(i).getTitle());
            }
        }
        return "";
    }

}
