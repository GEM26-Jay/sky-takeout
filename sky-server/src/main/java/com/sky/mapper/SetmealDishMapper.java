package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface SetmealDishMapper {

    List<SetmealDish> getGroupByDishIds(ArrayList<Long> ids);

    void insertGroup(List<SetmealDish> dishList);

    @Select("select * from setmeal_dish where set_id=#{setId}")
    ArrayList<SetmealDish> selectBySetId(Long setId);

    void deleteBySetId(Long setId);

    void deleteGroupBySetIds(List<Long> setIds);

}
