package com.cloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @Description:
 * @Author: 伯乐
 * @Date: 2020/11/27 14:49
 */


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table("t_stock")
public class Stock {
    @Id
    private Integer id;
    private Integer stock;
    private Integer productId;
}
