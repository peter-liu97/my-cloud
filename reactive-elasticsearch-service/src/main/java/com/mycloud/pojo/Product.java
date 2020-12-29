package com.mycloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(indexName = "product-index")
public class Product {
    @Id
    private Long id;

    @Field(type = FieldType.Double)
    private BigDecimal price;

    private String name;

    private Integer stock;

    private String category;

    @Field(type = FieldType.Date)
    private Date createTime;
}
