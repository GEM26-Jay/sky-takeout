package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SetMealService {
    void updateSetMeal(SetmealDTO setmealDTO);

    PageResult getSetMealPage(SetmealPageQueryDTO setmealPageQueryDTO);

    void updateStatus(Long setMealId, int status);

    void deleteGruopByIds(List<Long> setMealIds);

    void addSetMeal(SetmealDTO setmealDTO);

    SetmealVO selectById(Long id);

    List<Setmeal> getBySetMeal(Setmeal setmeal);

    List<DishItemVO> getDishItemBySetId(Long id);
}
