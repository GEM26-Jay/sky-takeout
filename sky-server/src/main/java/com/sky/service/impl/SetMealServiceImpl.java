package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.utils.LocalFileUtil;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    SetMealMapper setMealMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Autowired
    DishMapper dishMapper;

    @Override
    public void updateSetMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        boolean dishChanged = false;
        for (SetmealDish setmealDish : setmealDishList) {
            setmealDish.setSetId(setmeal.getId());
            setmealDish.setSetName(setmeal.getName());
            if (setmealDish.getId()==null){
                dishChanged = true;
            }
        }

        setMealMapper.update(setmeal);
        if (dishChanged){
            setmealDishMapper.deleteBySetId(setmeal.getId());
            setmealDishMapper.insertGroup(setmealDishList);
        }
    }

    @Override
    public PageResult getSetMealPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setMealMapper.selectPage(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateStatus(Long setMealId, int status) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(setMealId);
        setmeal.setStatus(status);
        setMealMapper.update(setmeal);
    }

    @Override
    @Transactional
    public void deleteGruopByIds(List<Long> ids) {

        // 查询套餐是否在启用中, 若启用则无法删除
        List<Setmeal> setMeals = setMealMapper.selectByIds(ids);

        StringBuilder sb = new StringBuilder();
        for (Setmeal setmeal : setMeals) {
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                sb.append("套餐\"").append(setmeal.getName()).append("\"，");
            }
        }
        if (!sb.isEmpty()){
            throw new BaseException(sb.append("正在启用中！无法删除").toString());
        }

        // 删除套餐表
        setMealMapper.deleteGroupByIds(ids);
        // 删除套餐-菜品关系表
        setmealDishMapper.deleteGroupBySetIds(ids);

    }

    @Override
    @Transactional
    public void addSetMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.insert(setmeal);

        List<SetmealDish> dishList = setmealDTO.getSetmealDishes();
        Long setMealId = setmeal.getId();
        String setName = setmeal.getName();

        for (SetmealDish dish : dishList) {
            dish.setSetId(setMealId);
            dish.setSetName(setName);
        }
        setmealDishMapper.insertGroup(dishList);

    }

    @Override
    public SetmealVO selectById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        // 查询套餐表数据
        Setmeal setmeal = setMealMapper.selectById(id);
        // 查询套餐-菜品关联表数据
        List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetId(id);

        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    @Override
    public List<Setmeal> getBySetMeal(Setmeal setmeal) {
        List<Setmeal> setMealList = setMealMapper.getBySetMealInfo(setmeal);

        return setMealList;

    }

    @Override
    public List<DishItemVO> getDishItemBySetId(Long id) {
        List<DishItemVO> dishItemVOs = setmealDishMapper.selectDishVOBySetId(id);
        return dishItemVOs;
    }


}
