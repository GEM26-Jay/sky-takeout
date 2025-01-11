package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController()
@Api(tags = "菜品分类相关接口")
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PutMapping()
    @ApiOperation("修改分类")
    public Result update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> getPage(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分类分页查询: {}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.getPage(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("分类状态修改")
    public Result updateStatus(@PathVariable Integer status, int id){
        categoryService.updateStatus(id, status);
        return Result.success();
    }

    @PostMapping()
    @ApiOperation("增加分类")
    public Result insert(@RequestBody CategoryDTO categoryDTO){
        categoryDTO.setId(null);
        categoryService.insert(categoryDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除分类")
    public Result delete(int id){
        categoryService.delete(id);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("按类别查询分类")
    public Result<List> list(int type){
        ArrayList<Category> arr = categoryService.selectByType(type);
        return Result.success(arr);
    }
}
