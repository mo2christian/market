package com.mo2christian.market.controller;

import com.mo2christian.market.common.Mapper;
import com.mo2christian.market.model.Article;
import com.mo2christian.market.model.ArticleDto;
import com.mo2christian.market.model.Basket;
import com.mo2christian.market.model.BasketDto;
import com.mo2christian.market.service.BasketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller()
public class BasketController {

    @Autowired
    private BasketService service;

    public BasketController() {
    }

    @GetMapping("/basket")
    public String list(Model model){
        log.info("Afficher tous les paniers");
        List<BasketDto> baskets = service.findAll()
                .stream()
                .map(Mapper.BASKET::toDto)
                .collect(Collectors.toList());
        model.addAttribute("baskets", baskets);
        return "basket";
    }

    @GetMapping("/basket/{id}")
    public String viewBasket(@PathVariable("id") long id, Model model) {
        log.info("Information du panier {}", id);
        Basket basket = service.findById(id)
                .get();
        List<ArticleDto> articles = basket.getArticles()
                .stream()
                .map(Mapper.ARTICLE::toDto)
                .collect(Collectors.toList());
        model.addAttribute("articles", articles);
        model.addAttribute("basket", Mapper.BASKET.toDto(basket));
        return "article";
    }

    @GetMapping("/basket/{bId}/delete")
    public View deleteBasket(@PathVariable("bId") long basketId){
        log.info("Supprimer le panier {}", basketId);
        Basket basket = service.findById(basketId).get();
        service.delete(basket);
        return new RedirectView("/basket");
    }

    @PostMapping("/basket/{id}/article")
    public View addArticle(@PathVariable("id") long id, @ModelAttribute ArticleDto articleDto){
        log.info("Ajouter l'article {} au panier {}", articleDto, id);
        Article article = Mapper.ARTICLE.toEntity(articleDto);
        Basket basket = service.findById(id).get();
        service.addArticle(basket, article);
        return new RedirectView(String.format("/basket/%d", id));
    }

    @GetMapping("/basket/{bId}/article/{aId}/check")
    public View checkArticle(@PathVariable("bId") long basketId, @PathVariable("aId") long articleId){
        log.info("Changer le statut de l'article {} dans le panier {}", articleId, basketId);
        Article article = service.getArticleById(basketId, articleId).get();
        service.checkArticle(article);
        return new RedirectView(String.format("/basket/%d", basketId));
    }

    @GetMapping("/basket/{bId}/article/{aId}/delete")
    public View deleteArticle(@PathVariable("bId") long basketId, @PathVariable("aId") long articleId){
        log.info("Supprimer l'article {} au panier {}", articleId, basketId);
        Article article = service.getArticleById(basketId, articleId).get();
        service.deleteArticle(article);
        return new RedirectView(String.format("/basket/%d", basketId));
    }

    @PostMapping("/basket")
    public View create(){
        log.info("Creer un panier");
        service.createBasket();
        return new RedirectView("/basket");
    }

}
