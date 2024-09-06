package com.yuliawan.backend.service;

import com.yuliawan.backend.entity.Order;
import com.yuliawan.backend.request.OrderRequest;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface OrderService {
    Optional<Order> findById(String id);
    Page<Order> findAll(Integer pageRequested);
    Order save(OrderRequest orderRequest) throws NonUniqueResultException, NoResultException, IllegalArgumentException;
    Order edit(String id, OrderRequest orderRequest) throws NonUniqueResultException, NoResultException, IllegalArgumentException;
    void delete(String id);
}
