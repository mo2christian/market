package com.mo2christian.market.function;

import com.google.actions.api.ActionRequest;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.DialogflowApp;
import com.google.actions.api.ForIntent;
import com.google.actions.api.response.ResponseBuilder;
import com.mo2christian.market.function.model.Article;
import com.mo2christian.market.function.model.Basket;
import com.mo2christian.market.function.repository.BasketRepository;

import java.util.List;
import java.util.stream.Collectors;

public class Action extends DialogflowApp {

    private final BasketRepository repository;

    public Action(){
        repository = new BasketRepository();
    }

    @ForIntent("Default Welcome Intent")
    public ActionResponse welcome(ActionRequest request) {
        ResponseBuilder responseBuilder = getResponseBuilder(request);
        responseBuilder.add("Bienvenue sur market-list");
        return responseBuilder.build();
    }

    @ForIntent("create basket")
    public ActionResponse createBasket(ActionRequest request){
        repository.createBasket();
        ResponseBuilder responseBuilder = getResponseBuilder(request);
        responseBuilder.add("Panier crée");
        return responseBuilder.build();
    }

    @ForIntent("add article")
    public ActionResponse addArticle(ActionRequest request){
        List<String> params = (List<String>)request.getParameter("articles");
        List<Article> articles = params.stream()
                .map(s -> new Article(s))
                .collect(Collectors.toList());
        Basket basket = repository.getBasket()
                .orElseGet(() -> {
                    repository.createBasket();
                    return repository.getBasket().get();
                });
        repository.addArticle(basket, articles);
        ResponseBuilder responseBuilder = getResponseBuilder(request);
        responseBuilder.add("Articles ajoutés");
        return responseBuilder.build();
    }
}
