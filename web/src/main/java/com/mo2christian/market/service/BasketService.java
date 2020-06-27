package com.mo2christian.market.service;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Key;
import com.google.common.collect.Lists;
import com.mo2christian.market.model.Article;
import com.mo2christian.market.model.Basket;
import com.mo2christian.market.repository.ArticleRepository;
import com.mo2christian.market.repository.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.core.GcpProjectIdProvider;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasketService {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private GcpProjectIdProvider provider;

    @Autowired
    private ArticleRepository articleRepository;

    private final Sort sortByCreatedDateDesc = Sort.by(Sort.Direction.DESC, "createdDate");

    public Optional<Basket> findById(long id){
        return basketRepository.findById(Key.newBuilder(provider.getProjectId(), Basket.KIND, id).build());
    }

    public List<Basket> findAll(){
        Iterable<Basket> iterable = basketRepository.findAll(sortByCreatedDateDesc);
        return Lists.newArrayList(iterable);
    }

    public Basket getLastBasket(){
        return findAll().stream()
                .findFirst()
                .get();
    }

    public void delete(Basket basket){
        basketRepository.delete(basket);
    }

    public void addArticle(Basket basket, Article article){
        basket.getArticles().add(article);
        basketRepository.save(basket);
    }

    public void deleteArticle(Article article){
        articleRepository.delete(article);
    }

    public Optional<Article> getArticleById(long basketId, long articleId){
        Key basketKey = Key.newBuilder(provider.getProjectId(), Basket.KIND, basketId).build();
        return articleRepository.findById(Key.newBuilder(basketKey, Article.KIND, articleId).build());
    }

    public void createBasket(){
        Basket basket = new Basket();
        basket.setCreatedDate(Timestamp.now());
        basketRepository.save(basket);
    }

    public void checkArticle(Article article){
        article.setCheck(!article.isCheck());
        articleRepository.save(article);
    }
}
