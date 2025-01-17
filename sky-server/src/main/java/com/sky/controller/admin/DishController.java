package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.utils.LocalFileUtil;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    DishService dishService;
    @Autowired
    RedisTemplate redisTemplate;

    @ApiOperation("添加菜品")
    @PostMapping()
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("添加菜品: {}", dishDTO);
        dishService.addDishWithFlavor(dishDTO);

        //将所有菜品缓存数据清理掉
        cleanCache("dish*");

        return Result.success();
    }

    @PutMapping()
    @ApiOperation("修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO){
        dishService.updateDish(dishDTO);
        redisTemplate.delete("dish_"+dishDTO.getId());

        //将所有菜品缓存数据清理掉
        cleanCache("dish*");

        return Result.success();
    }

    @ApiOperation("批量删除菜品")
    @DeleteMapping()
    public Result deleteDish(@RequestParam ArrayList<Long> ids){
        log.info("删除菜品: {}", ids);
        dishService.deleteGroup(ids);
        //将所有菜品缓存数据清理掉
        cleanCache("dish*");
        return Result.success();
    }

    @ApiOperation("根据Id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getDishById(@PathVariable("id") Long id){

        DishVO dishvo = dishService.getById(id);

        return Result.success(dishvo);
    }

    @ApiOperation("根据分类查询菜品")
    @GetMapping("/list")
    public Result<List<Dish>> getDishList(Long typeId){
        List<Dish> dishes = dishService.getByTypeId(typeId);
        return Result.success(dishes);
    }

    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> getDishPage(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询: {}", dishPageQueryDTO);
        PageResult page = dishService.getPage(dishPageQueryDTO);
        return Result.success(page);
    }

    @ApiOperation("菜品状态变更")
    @PostMapping("/status/{status}")
    public Result updateDishStatus(@PathVariable("status") int status, Long id){
        dishService.updateStatus(id,  status);
        //将所有菜品缓存数据清理掉
        cleanCache("dish*");

        return Result.success();
    }

    /**
     * 清理缓存数据
     * @param pattern
     */
    private void cleanCache(String pattern){
        Set key = redisTemplate.keys(pattern);
        redisTemplate.delete(key);
    }
}
