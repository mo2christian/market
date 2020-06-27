package com.mo2christian.market.repository;

import com.google.cloud.datastore.Key;
import com.mo2christian.market.model.Article;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

public interface ArticleRepository extends DatastoreRepository<Article, Key> {
}
