package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.utils.LocalFileUtil;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    @Autowired
    SetmealDishMapper setMealDishMapper;

    @Transactional
    @Override
    public void addDishWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 菜品表添加数据
        dishMapper.insert(dish);
        // 获取insert语句生成的主键值
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach((df)->{
                df.setDishId(dishId);
            });
            dishFlavorMapper.insertGroup(flavors);
        }

    }

    @Override
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();

        var needUpdate = false;
        if (flavors != null && !flavors.isEmpty()) {
            for (DishFlavor flavor : flavors) {
                if (flavor.getDishId() == null) {
                    flavor.setDishId(dish.getId());
                    if (!needUpdate) needUpdate = true;
                }
            }
        }else{
            // 删除原口味
            dishFlavorMapper.deleteByDishId(dish.getId());
        }

        if (needUpdate){
            // 删除原口味
            dishFlavorMapper.deleteByDishId(dish.getId());
            // 添加新口味
            dishFlavorMapper.insertGroup(flavors);
        }

    }

    @Transactional
    @Override
    public void deleteGroup(ArrayList<Long> idArr) {
        //判断当前菜品是否能够删除--起售中
        ArrayList<Dish> dishes = dishMapper.getGroupByIds(idArr);
        StringBuilder statusSb = new StringBuilder();
        for (Dish dish : dishes) {
            if (dish.getStatus() == StatusConstant.ENABLE){
                statusSb.append(dish.getName()).append(',');
            }
        }

        if (!statusSb.isEmpty()) {
            throw new DeletionNotAllowedException(statusSb.append("正在启用中，无法删除").toString());
        }

        //判断是否被套餐关联
        List<SetmealDish> setmealDishes = setMealDishMapper.getGroupByDishIds(idArr);

        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            StringBuilder setSb = new StringBuilder();
            for (SetmealDish setDish : setmealDishes) {
                setSb.append("菜品\"").append(setDish.getDishName()).append("\"正在套餐\"").append(setDish.getSetName()).append("\"中售卖，无法删除!\r\n");
            }
            throw new DeletionNotAllowedException(setSb.toString());
        }

        //删除菜品表中的菜品数据
        dishMapper.deleteGroupByDishIds(idArr);

        //删除菜品关联的口味数据
        dishFlavorMapper.deleteGroupByDishIds(idArr);

    }

    @Override
    public DishVO getById(Long id) {
        DishVO dishVO = new DishVO();
        Dish dish = dishMapper.getById(id);
        BeanUtils.copyProperties(dish, dishVO);
        ArrayList<DishFlavor> dfs = dishFlavorMapper.selectByDishId(id);
        dishVO.setFlavors(dfs);
        return dishVO;
    }

    @Override
    public PageResult getPage(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.selectByPage(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateStatus(Long id, int status) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        dishMapper.update(dish);
    }

    @Override
    public List<Dish> getByTypeId(Long typeId) {
        List<Dish> dishes = dishMapper.getByTypeId(typeId);
        return dishes;
    }
}
