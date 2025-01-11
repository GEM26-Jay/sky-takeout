package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface SetmealDishMapper {

    ArrayList<SetmealDish> getGroupByDishIds(ArrayList<Long> idArr);
}
