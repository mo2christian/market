package com.mo2christian.market.common;

import com.mo2christian.market.model.ArticleDto;
import com.mo2christian.market.model.BasketDto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Mapper {

    public static final Article ARTICLE = new Article();
    public static final Basket BASKET = new Basket();

    public static class Basket{

        private DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");

        public BasketDto toDto(com.mo2christian.market.model.Basket entity){
            BasketDto dto = new BasketDto();
            dto.setId(entity.getKey().getId());
            dto.setName(dateFormat.format(entity.getCreatedDate().toDate()) + " " +dto.getId());
            return dto;
        }

    }

    public static class Article {

        public com.mo2christian.market.model.Article toEntity(ArticleDto dto){
            com.mo2christian.market.model.Article entity = new com.mo2christian.market.model.Article();
            entity.setName(dto.getName());
            entity.setComment(dto.getComment());
            entity.setCheck(dto.isCheck());
            return entity;
        }

        public ArticleDto toDto(com.mo2christian.market.model.Article entity){
            ArticleDto dto = new ArticleDto();
            dto.setComment(entity.getComment());
            dto.setName(entity.getName());
            dto.setCheck(entity.isCheck());
            dto.setId(entity.getKey().getId());
            return dto;
        }

    }

}
