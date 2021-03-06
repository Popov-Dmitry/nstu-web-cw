package com.github.PopovDmitry.nstu.webcw.controller;

import com.github.PopovDmitry.nstu.webcw.dto.ArticleDTO;
import com.github.PopovDmitry.nstu.webcw.model.Article;
import com.github.PopovDmitry.nstu.webcw.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/news")
public class ArticleController {

    private final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) { this.articleService = articleService; }

    @GetMapping
    public String getNews() {
        logger.info("getNews");
        return "views/index";
    }

    @GetMapping("/{id}")
    public String getArticle(@PathVariable long id, Model model) {
        logger.info("getArticle {}", id);

        if(articleService.getArticle(id).isEmpty()) {
            logger.info("Article with id {} is not found", id);
            return "redirect:/news";
        }

        Article article = articleService.getArticle(id).get();
        article.setViews(article.getViews() + 1);
        articleService.updateArticle(article);
        model.addAttribute("article", article);
        model.addAttribute("pageTitle", article.getTitle());
        return "views/showArticle";
    }

    @PreAuthorize("hasAnyAuthority('articles:write')")
    @PostMapping()
    public String addArticle(@RequestBody @Valid ArticleDTO articleDTO,
                             BindingResult bindingResult,
                             HttpServletRequest httpServletRequest) {
        logger.info("addArticle");
        if(bindingResult.hasErrors()) {
            logger.info("Article has errors");
            return "views/newArticle";
        }

        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        articleService.saveArticle(article, httpServletRequest);
        return "redirect:/news";
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable long id) {
        logger.info("deleteArticle {}", id);
        return "";
    }

    @GetMapping("/new")
    public String newArticle(Model model) {
        logger.info("newArticle");
        model.addAttribute("article", new Article());
        return "views/newArticle";
    }

}
