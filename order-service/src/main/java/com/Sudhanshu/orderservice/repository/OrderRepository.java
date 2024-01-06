package com.Sudhanshu.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.Sudhanshu.orderservice.model.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {
}