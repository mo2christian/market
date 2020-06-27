package com.mo2christian.market.controller;

import com.google.cloud.storage.Storage;
import com.google.cloud.vision.v1.*;
import com.mo2christian.market.common.AppException;
import com.mo2christian.market.common.Mapper;
import com.mo2christian.market.model.Article;
import com.mo2christian.market.model.ArticleDto;
import com.mo2christian.market.model.Basket;
import com.mo2christian.market.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MarketController {

    @Value("${app.bucket}")
    private String bucketName;

    @Autowired
    private Storage storage;

    @Autowired
    private CloudVisionTemplate visionTemplate;

    @Autowired
    private BasketService service;

    @PostMapping(value = "/rest/basket", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public String createBasket(){
        service.createBasket();
        return "OK";
    }

    @PostMapping(value = "/rest/basket/article", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public String addArticle(@RequestBody ArticleDto dto){
        Basket basket = service.getLastBasket()
                .get();
        Article article = Mapper.ARTICLE.toEntity(dto);
        service.addArticle(basket, article);
        return "OK";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("market_list") MultipartFile file, Model model){
        try{
            Resource img = new ByteArrayResource(file.getBytes());
            AnnotateImageResponse response = visionTemplate.analyzeImage(img, Feature.Type.DOCUMENT_TEXT_DETECTION);
            TextAnnotation annotation = response.getFullTextAnnotation();
            List<String> articles = new ArrayList<>();
            StringBuilder sentence = new StringBuilder();
            for (Page page: annotation.getPagesList()) {
                for (Block block : page.getBlocksList()) {
                    for (Paragraph para : block.getParagraphsList()) {
                        for (Word word: para.getWordsList()) {
                            StringBuilder wordText = new StringBuilder();
                            for (Symbol symbol: word.getSymbolsList()) {
                                wordText.append(symbol.getText());
                            }
                            sentence.append(wordText)
                                    .append(" ");
                        }
                        articles.add(sentence.toString());
                        sentence.delete(0, sentence.length());
                    }
                }
            }
            model.addAttribute("articles", articles);
        }
        catch(IOException ex){
            throw new AppException("Error while uploading file", ex);
        }

        return "list";
    }

    @GetMapping("/")
    public View index(){
        return new RedirectView("/basket");
    }

}
