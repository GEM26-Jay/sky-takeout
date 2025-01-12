package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealMapper {

    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    Page<Setmeal> selectPage(SetmealPageQueryDTO setmealPageQueryDTO);

    @Select("select * from setmeal where id=#{id}")
    Setmeal selectById(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    List<Setmeal> selectByIds(List<Long> setMealIds);

    void deleteGroupByIds(List<Long> setMealIds);
}
