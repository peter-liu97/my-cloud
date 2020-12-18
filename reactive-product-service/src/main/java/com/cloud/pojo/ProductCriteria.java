package com.cloud.pojo;

import lombok.Data;

/**
 * @Description:
 * @Author: 伯乐
 * @Date: 2020/11/27 14:49
 */
@Data
public class ProductCriteria {
  /*  private Integer id;
    private BigDecimal price;
    private String name;
    private Integer stock;*/
    private String category;

    private int page = 1;
    private int pageSize = 10;
}
