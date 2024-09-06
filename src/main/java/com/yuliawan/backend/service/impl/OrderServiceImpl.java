package com.yuliawan.backend.service.impl;

import com.yuliawan.backend.entity.Item;
import com.yuliawan.backend.entity.Order;
import com.yuliawan.backend.repository.ItemRepository;
import com.yuliawan.backend.repository.OrderRepository;
import com.yuliawan.backend.request.OrderRequest;
import com.yuliawan.backend.service.OrderService;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;

    @Override
    public Optional<Order> findById(String id) {
        Integer searchId = orderIdToId(id);
        if(searchId != null){
            return orderRepository.findById(searchId);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Page<Order> findAll(Integer pageRequested) {
        if(pageRequested > 0) {
            Pageable request = PageRequest.of(pageRequested - 1, 3);
            return orderRepository.findAll(request);
        }
        return null;
    }

    @Override
    public Order save(OrderRequest orderRequest) throws NonUniqueResultException, NoResultException, IllegalArgumentException {
        List<Item> item = itemRepository.findByName(orderRequest.getItemName());
        if(item.isEmpty()) {
            throw new NoResultException("No item with name " + orderRequest.getItemName());
        }

        if (item.size() > 1){
            throw new NonUniqueResultException("Multiple item with name " + orderRequest.getItemName());
        }

        Item itemToOrder = item.get(0);

        if(itemToOrder.getRemainingStock() < orderRequest.getQty()) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        Order order = new Order();
        order.setItem(item.get(0));
        order.setQty(orderRequest.getQty());
        order.setPrice(itemToOrder.getPrice());
        return orderRepository.save(order);
    }

    @Override
    public Order edit(String id, OrderRequest orderRequest) throws NonUniqueResultException, NoResultException, IllegalArgumentException {
        Optional<Order> order = findById(id);
        if(order.isPresent()) {
            List<Item> item = itemRepository.findByName(orderRequest.getItemName());
            if(item.isEmpty()) {
                throw new NoResultException("No item with name " + orderRequest.getItemName());
            }

            if (item.size() > 1){
                throw new NonUniqueResultException("Multiple item with name " + orderRequest.getItemName());
            }

            Item itemToOrder = item.get(0);
            Order orderEdited = order.get();

            if((itemToOrder.getRemainingStock() + orderEdited.getQty()) < orderRequest.getQty()) {
                throw new IllegalArgumentException("Insufficient stock");
            }

            orderEdited.setItem(item.get(0));
            orderEdited.setQty(orderRequest.getQty());
            orderEdited.setPrice(itemToOrder.getPrice());
            return orderRepository.save(orderEdited);
        } else {
            throw new NoResultException("No Order with id " + id);
        }
    }

    @Override
    public void delete(String id) {
        Integer searchId = orderIdToId(id);
        if(searchId != null) {
            orderRepository.deleteById(searchId);
        }
    }

    private Integer orderIdToId (String orderId) {
        if(orderId.matches("O[0-9]+")) {
            return Integer.valueOf(orderId.substring(1));
        }
        return null;
    }
}
