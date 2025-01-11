package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface CategoryMapper {

    /**
     * 统计数据数量
     * @param categoryPageQueryDTO
     * @return
     */
    int counts(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 分页查询数据
     * @param categoryPageQueryDTO
     * @return
     */
    ArrayList<Category> selectByPage(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 更新数据库
     * @param category
     */
    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    /**
     * 删除数据
     * @param id
     */
    @Delete("delete from category where id=#{id}")
    void deleteById(long id);

    /**
     * 根据分类类别进行查询
     * @param type
     * @return
     */
    ArrayList<Category> selectByType(int type);

    /**
     * 插入数据
     * @param category
     */
    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "VALUES (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(OperationType.INSERT)
    void insert(Category category);
}
