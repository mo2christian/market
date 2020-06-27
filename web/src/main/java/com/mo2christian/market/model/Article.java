package com.mo2christian.market.model;

import com.google.cloud.datastore.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;

@Entity(name = Article.KIND)
@Data
@EqualsAndHashCode(of = {"key"})
public class Article {

    public static final String KIND = "article";

    @Id
    private Key key;

    private String name;

    private String comment;

    private boolean check = false;

}
