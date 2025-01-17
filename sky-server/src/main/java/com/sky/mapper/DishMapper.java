package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface DishMapper {

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> selectByPage(DishPageQueryDTO dishPageQueryDTO);

    ArrayList<Dish> getGroupByIds(ArrayList<Long> idArr);

    void deleteGroupByDishIds(ArrayList<Long> idArr);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    List<Dish> getByTypeId(Long typeId);

    List<Dish> getByDishInfo(Dish dishInfo);

    List<Dish> getByIds(ArrayList<Long> dishIds);

}
