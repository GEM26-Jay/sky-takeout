package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface SetmealDishMapper {

    List<SetmealDish> getGroupByDishIds(ArrayList<Long> ids);

    void insertGroup(List<SetmealDish> dishList);

    List<DishItemVO> selectDishVOBySetId(Long setId);

    void deleteBySetId(Long setId);

    void deleteGroupBySetIds(List<Long> setIds);

    @Select("select * from setmeal_dish where set_id=#{setId}")
    List<SetmealDish> selectBySetId(Long setId);

}
