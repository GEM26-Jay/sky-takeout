package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void insertGroup(List<DishFlavor> flavors);

    void deleteGroupByDishIds(ArrayList<Long> idArr);

    @Select("select * from dish_flavor where dish_id=#{dishId}")
    ArrayList<DishFlavor> selectByDishId(Long dishId);

    @Delete("delete from dish_flavor where dish_id=#{id}")
    void deleteByDishId(Long id);

}
