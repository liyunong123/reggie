package com.itheima.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {
  @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品，同时保存对应口味数据
     * @param dishDto
     */
    @Transactional   //涉及多张表的操作，需加入事务控制，事务注解，在启动类上开启@EnableTransactionManagement
    public void saveWithFlavor(DishDto dishDto) {
        //保存彩屏的基本信息到菜品表
        this.save(dishDto);

        Long dishId = dishDto.getId();

        //彩屏口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存彩屏口味数据到菜品口味表
        dishFlavorService.saveBatch(flavors);
    }
}

