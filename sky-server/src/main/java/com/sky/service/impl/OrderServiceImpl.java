package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    AddressBookMapper addressBookMapper;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional()
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        //处理业务异常（地址，购物车null）
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list == null || list.isEmpty()) {
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //向订单表插入一跳数据
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setOrderTime(LocalDateTime.now());
        order.setPayStatus(Orders.UN_PAID);
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setPhone(addressBook.getPhone());
        order.setConsignee(addressBook.getConsignee());
        order.setUserId(BaseContext.getCurrentId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(addressBook.getProvinceName()).append(";")
                .append(addressBook.getCityName()).append(";")
                .append(addressBook.getDistrictName()).append(";")
                .append(addressBook.getDetail()).append(";");
        order.setAddress(stringBuilder.toString());
        orderMapper.insert(order);

        //向订单明细表插入商品数据
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        for (ShoppingCart cart : list) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetails.add(orderDetail);
        }
        orderDetailMapper.insertGroup(orderDetails);

        //清空当前用户的购物车数据
        shoppingCartMapper.deleteAll(BaseContext.getCurrentId());

        //封装VO返回结果
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(order.getId());
        orderSubmitVO.setOrderTime(order.getOrderTime());
        orderSubmitVO.setOrderAmount(order.getAmount());
        orderSubmitVO.setOrderNumber(order.getNumber());
        return orderSubmitVO;

    }

    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        return null;
    }

    @Override
    public void paySuccess(Long orderId) {
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        orders.setPayStatus(Orders.PAID);
        orderMapper.updateStatus(orders);
    }

    @Override
    public OrderVO getOrderDetail(Long id) {
        Orders order = new Orders();
        order.setId(id);
        order = orderMapper.select(order);

        List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(id);

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetails);

        return orderVO;
    }

    @Override
    public PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Long UserId = BaseContext.getCurrentId();
        Orders order = new Orders();
        order.setUserId(UserId);
        order.setStatus(ordersPageQueryDTO.getStatus());
        Page<Orders> page = orderMapper.QueryPage(order);
        List<Orders> list = page.getResult();
        List<OrderVO> orderVOList = new ArrayList<>();

        for (Orders orders : list) {
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orders.getId());
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            orderVO.setOrderDetailList(orderDetails);
            orderVOList.add(orderVO);
        }

        return new PageResult(page.getTotal(), orderVOList);
    }

    @Override
    public void delete(Long orderId) {
        Orders order = new Orders();
        order.setId(orderId);
        order.setStatus(Orders.CANCELLED);
        order.setPayStatus(Orders.UN_PAID);
        orderMapper.updateStatus(order);

    }

    @Override
    @Transactional()
    public void repetOrder(Long orderId) {
        Orders order = new Orders();
        order.setId(orderId);
        order = orderMapper.select(order);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setCancelReason(null);
        order.setCancelTime(null);
        order.setCheckoutTime(null);
        order.setDeliveryStatus(0);
        order.setDeliveryTime(null);
        orderMapper.insert(order);

        List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderId);
        orderDetailMapper.insertGroup(orderDetails);

    }


}
