package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.ArrayList;
import java.util.List;

public interface DishService {
    void addDishWithFlavor(DishDTO dishDTO);

    void updateDish(DishDTO dishDTO);

    void deleteGroup(ArrayList<Long> idArr);

    DishVO getById(Long id);

    PageResult getPage(DishPageQueryDTO dishPageQueryDTO);

    void updateStatus(Long id, int status);

    List<Dish> getByType(Long typeId);
}
