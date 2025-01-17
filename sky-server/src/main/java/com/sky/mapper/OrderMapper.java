package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper {
    void insert(Orders order);

    Orders select(Orders order);

    @Update("update orders set status=#{status}, pay_status=#{payStatus} where id=#{id}")
    void updateStatus(Orders order);

    Page<Orders> QueryPage(Orders order);
}
