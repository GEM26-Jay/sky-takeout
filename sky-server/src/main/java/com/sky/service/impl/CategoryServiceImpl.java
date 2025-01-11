package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public PageResult getPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        int total = categoryMapper.counts(categoryPageQueryDTO);
        int startPage = categoryPageQueryDTO.getPage();
        int pageSize = categoryPageQueryDTO.getPageSize();
        startPage = (startPage-1)*pageSize;
        categoryPageQueryDTO.setPage(startPage);

        ArrayList<Category> records = categoryMapper.selectByPage(categoryPageQueryDTO);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(total);
        pageResult.setRecords(records);
        return pageResult;
    }

    @Override
    public void updateStatus(long id, Integer status) {
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);
        categoryMapper.update(category);
    }

    @Override
    public void delete(long id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public void insert(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
//        category.setUpdateUser(BaseContext.getCurrentId());
        category.setStatus(1);
        categoryMapper.insert(category);
    }

    @Override
    public ArrayList<Category> selectByType(int i) {
        ArrayList<Category> arr = categoryMapper.selectByType(i);
        return arr;
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.update(category);
    }
}
