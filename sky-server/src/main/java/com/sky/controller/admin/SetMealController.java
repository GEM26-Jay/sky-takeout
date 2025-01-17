package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetMealController {

    @Autowired
    SetMealService setMealService;

    @PutMapping()
    @ApiOperation("修改套餐")
    public Result updateSetMeal(@RequestBody SetmealDTO setmealDTO){
        setMealService.updateSetMeal(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> getSetMealPage(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult = setMealService.getSetMealPage(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("套餐状态变更")
    public Result updateSetMealStatus(@PathVariable("status") int status, Long setMealId){
        setMealService.updateStatus(setMealId, status);
        return Result.success();
    }

    @DeleteMapping()
    @ApiOperation("批量删除套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result deleteSetMeals(@RequestParam List<Long> setMealIds){
        setMealService.deleteGruopByIds(setMealIds);
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    public Result addSetMeal(@RequestBody SetmealDTO setmealDTO){
        setMealService.addSetMeal(setmealDTO);
        return Result.success();
    }

    @GetMapping("{id}")
    @ApiOperation("根据Id查询套餐")
    public Result<SetmealVO> getSetMealById(@PathVariable("id") Long id){
        SetmealVO setmealVO = setMealService.selectById(id);
        return Result.success(setmealVO);
    }

}
