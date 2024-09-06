package com.yuliawan.backend.service.impl;

import com.yuliawan.backend.entity.Inventory;
import com.yuliawan.backend.entity.Item;
import com.yuliawan.backend.entity.Order;
import com.yuliawan.backend.repository.ItemRepository;
import com.yuliawan.backend.repository.OrderRepository;
import com.yuliawan.backend.request.InventoryRequest;
import com.yuliawan.backend.request.OrderRequest;
import com.yuliawan.backend.service.OrderService;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.aspectj.weaver.ast.Or;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTests {

    @Autowired
    OrderServiceImpl orderService;
    @MockBean
    OrderRepository orderRepository;
    @MockBean
    ItemRepository itemRepository;

    @Test
    public void findById_success() {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(10);
        order1.setPrice(10);

        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(order1));

        Optional<Order> inventoryChecked = orderService.findById(order1.getOrderNo());

        assertTrue(inventoryChecked.isPresent());
        assertEquals(inventoryChecked.get(), order1);
    }

    @Test
    public void findById_failed() {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(10);
        order1.setPrice(10);

        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(order1));

        Optional<Order> inventoryChecked = orderService.findById("X1");

        assertTrue(inventoryChecked.isEmpty());
    }

    @Test
    public void findAll_success() {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(10);
        order1.setPrice(10);

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Pen");
        item2.setPrice(7);

        Order order2 = new Order();
        order2.setOrderNo(2);
        order2.setItem(item1);
        order2.setQty(5);
        order2.setPrice(7);

        List<Order> orderList = Arrays.asList(order1, order2);

        Page<Order> orders = new PageImpl<>(orderList);

        Mockito.when(orderRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(orders);

        Page<Order> itemChecked = orderService.findAll(1);

        assertEquals(itemChecked, orders);
    }

    @Test
    public void findAll_failed() {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(10);
        order1.setPrice(10);

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Pen");
        item2.setPrice(7);

        Order order2 = new Order();
        order2.setOrderNo(2);
        order2.setItem(item1);
        order2.setQty(5);
        order2.setPrice(7);

        List<Order> orderList = Arrays.asList(order1, order2);

        Page<Order> orders = new PageImpl<>(orderList);

        Mockito.when(orderRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(orders);

        Page<Order> itemChecked = orderService.findAll(0);

        assertNull(itemChecked);
    }

    @Test
    public void save_success() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemName("Book");
        orderRequest.setQty(10);


        Item item1 = new Item();
        item1.setId(1);
        item1.setName(orderRequest.getItemName());
        item1.setPrice(10);

        Inventory inventory = new Inventory();
        inventory.setItem(item1);
        inventory.setQty(20);
        inventory.setType("T");

        item1.setInventory(List.of(inventory));

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(orderRequest.getQty());
        order1.setPrice(item1.getPrice());

        Mockito.when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(order1);
        Mockito.when(itemRepository.findByName(ArgumentMatchers.any(String.class))).thenReturn(List.of(item1));

        Order itemChecked = orderService.save(orderRequest);

        assertEquals(itemChecked, order1);
    }

    @Test
    public void save_failedEmpty() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemName("Book");
        orderRequest.setQty(10);

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(orderRequest.getItemName());
        item1.setPrice(10);

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(orderRequest.getQty());
        order1.setPrice(item1.getPrice());

        Mockito.when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(order1);
        Mockito.when(itemRepository.findByName(ArgumentMatchers.any(String.class))).thenReturn(new ArrayList<>());

        assertThrows(NoResultException.class, () -> orderService.save(orderRequest));
    }

    @Test
    public void save_failedDouble() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemName("Book");
        orderRequest.setQty(10);

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(orderRequest.getItemName());
        item1.setPrice(10);

        Item item2 = new Item();
        item2.setId(1);
        item2.setName(orderRequest.getItemName());
        item2.setPrice(10);

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(orderRequest.getQty());
        order1.setPrice(item1.getPrice());

        Mockito.when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(order1);
        Mockito.when(itemRepository.findByName(ArgumentMatchers.any(String.class))).thenReturn(List.of(item1, item2));

        assertThrows(NonUniqueResultException.class, () -> orderService.save(orderRequest));
    }

    @Test
    public void save_failedInsufficientStock() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemName("Book");
        orderRequest.setQty(10);

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(orderRequest.getItemName());
        item1.setPrice(10);

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(orderRequest.getQty());
        order1.setPrice(item1.getPrice());

        Mockito.when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(order1);
        Mockito.when(itemRepository.findByName(ArgumentMatchers.any(String.class))).thenReturn(List.of(item1));

        assertThrows(IllegalArgumentException.class, () -> orderService.save(orderRequest));
    }

    @Test
    public void edit_success() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemName("Book");
        orderRequest.setQty(10);


        Item item1 = new Item();
        item1.setId(1);
        item1.setName(orderRequest.getItemName());
        item1.setPrice(10);

        Inventory inventory = new Inventory();
        inventory.setItem(item1);
        inventory.setQty(20);
        inventory.setType("T");

        item1.setInventory(List.of(inventory));

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(orderRequest.getQty());
        order1.setPrice(item1.getPrice());

        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        Mockito.when(itemRepository.findByName(item1.getName())).thenReturn(List.of(item1));
        Mockito.when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(order1);

        Order itemChecked = orderService.edit("O1",orderRequest);

        assertEquals(itemChecked, order1);
    }

    @Test
    public void edit_failedNoInventoryData() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemName("Book");
        orderRequest.setQty(10);


        Item item1 = new Item();
        item1.setId(1);
        item1.setName(orderRequest.getItemName());
        item1.setPrice(10);

        Inventory inventory = new Inventory();
        inventory.setItem(item1);
        inventory.setQty(20);
        inventory.setType("T");

        item1.setInventory(List.of(inventory));

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(orderRequest.getQty());
        order1.setPrice(item1.getPrice());

        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        Mockito.when(itemRepository.findByName(item1.getName())).thenReturn(List.of(item1));
        Mockito.when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(order1);

        assertThrows(NoResultException.class, () -> orderService.edit("O2",orderRequest));
    }

    @Test
    public void edit_failedNoItemData() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemName("Book");
        orderRequest.setQty(10);


        Item item1 = new Item();
        item1.setId(1);
        item1.setName(orderRequest.getItemName());
        item1.setPrice(10);

        Inventory inventory = new Inventory();
        inventory.setItem(item1);
        inventory.setQty(20);
        inventory.setType("T");

        item1.setInventory(List.of(inventory));

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(orderRequest.getQty());
        order1.setPrice(item1.getPrice());

        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        Mockito.when(itemRepository.findByName(item1.getName())).thenReturn(new ArrayList<>());
        Mockito.when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(order1);

        assertThrows(NoResultException.class, () -> orderService.edit("O1",orderRequest));
    }

    @Test
    public void edit_failedMultipleItemData() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemName("Book");
        orderRequest.setQty(10);


        Item item1 = new Item();
        item1.setId(1);
        item1.setName(orderRequest.getItemName());
        item1.setPrice(10);

        Inventory inventory = new Inventory();
        inventory.setItem(item1);
        inventory.setQty(20);
        inventory.setType("T");

        item1.setInventory(List.of(inventory));

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(orderRequest.getQty());
        order1.setPrice(item1.getPrice());

        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        Mockito.when(itemRepository.findByName(item1.getName())).thenReturn(List.of(item1, item1));
        Mockito.when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(order1);

        assertThrows(NonUniqueResultException.class, () -> orderService.edit("O1",orderRequest));
    }

    @Test
    public void edit_failedInsufficientStock() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemName("Book");
        orderRequest.setQty(50);


        Item item1 = new Item();
        item1.setId(1);
        item1.setName(orderRequest.getItemName());
        item1.setPrice(10);

        Inventory inventory = new Inventory();
        inventory.setItem(item1);
        inventory.setQty(1);
        inventory.setType("T");

        item1.setInventory(List.of(inventory));

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(1);
        order1.setPrice(item1.getPrice());

        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        Mockito.when(itemRepository.findByName(item1.getName())).thenReturn(List.of(item1));
        Mockito.when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(order1);

        assertThrows(IllegalArgumentException.class, () -> orderService.edit("O1",orderRequest));
    }
}
