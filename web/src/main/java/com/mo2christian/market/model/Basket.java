package com.mo2christian.market.model;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Descendants;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Field;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Entity(name = Basket.KIND)
@Data
@EqualsAndHashCode(of = {"key"})
public class Basket {

    public static final String KIND = "basket";

    @Id
    private Key key;

    @Field(name = "created_date")
    private Timestamp createdDate;

    @Field(name = "rank")
    private int rank = 1;

    @Descendants
    Set<Article> articles;

}
