package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 套餐菜品关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //套餐id
    private Long setId;

    //套餐名字
    private String setName;

    //菜品id
    private Long dishId;

    //菜品名称
    private String dishName;

    //菜品原价
    private BigDecimal price;

    //份数
    private Integer copies;
}
