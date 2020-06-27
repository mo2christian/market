package com.mo2christian.market.function.repository;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import com.mo2christian.market.function.model.Article;
import com.mo2christian.market.function.model.Basket;

import java.util.List;
import java.util.Optional;

public class BasketRepository {

    private Datastore datastore;

    public BasketRepository(){
        datastore = DatastoreOptions.getDefaultInstance().getService();
    }

    public void createBasket(){
        IncompleteKey taskKey = datastore.newKeyFactory()
                .setKind(Basket.Field.KIND)
                .newKey();
        FullEntity<IncompleteKey> basket = Entity.newBuilder()
                .set(Basket.Field.CREATED_DATE, Timestamp.now())
                .setKey(taskKey)
                .build();
        datastore.put(basket);
    }

    public Optional<Basket> getBasket(){
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(Basket.Field.KIND)
                .setOrderBy(StructuredQuery.OrderBy.desc(Basket.Field.CREATED_DATE))
                .setLimit(1)
                .build();
        QueryResults<Entity> result = datastore.run(query);
        Optional<Basket> basket;
        if (result.hasNext()){
            Entity entity = result.next();
            Basket b = new Basket(entity.getKey());
            basket = Optional.of(b);
        }
        else
            basket = Optional.empty();
        return basket;
    }

    public void addArticle(Basket basket, List<Article> articles){
        Batch batch = datastore.newBatch();
        articles.stream()
                .map(a -> toEntity(basket.getKey(), a))
                .forEach(e -> batch.add(e));
        batch.submit();
    }

    private FullEntity toEntity(Key parent, Article article){
        IncompleteKey key = Key.newBuilder(parent, Article.Field.KIND)
                .build();
        FullEntity<IncompleteKey> entity = Entity.newBuilder()
                .setKey(key)
                .set(Article.Field.NAME, article.getName())
                .set(Article.Field.COMMENT, article.getComment())
                .set(Article.Field.CHECK, false)
                .build();
        return entity;
    }

}
