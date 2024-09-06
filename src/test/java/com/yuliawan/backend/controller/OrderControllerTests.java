package com.yuliawan.backend.controller;

import com.google.gson.Gson;
import com.yuliawan.backend.entity.Item;
import com.yuliawan.backend.entity.Order;
import com.yuliawan.backend.request.OrderRequest;
import com.yuliawan.backend.service.OrderService;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void getData_success() throws Exception {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(10);
        order1.setPrice(10);

        Mockito.when(orderService.findById(order1.getOrderNo())).thenReturn(Optional.of(order1));

        mvc.perform(get("/order/O1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNo", is(order1.getOrderNo())))
                .andExpect(jsonPath("$.qty", is(order1.getQty())))
                .andExpect(jsonPath("$.price", is(order1.getPrice())))
                .andExpect(jsonPath("$.item.id", is(item1.getId())))
                .andExpect(jsonPath("$.item.name", is(item1.getName())))
                .andExpect(jsonPath("$.item.price", is(item1.getPrice())));

    }

    @Test
    public void getData_failed() throws Exception {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Order order1 = new Order();
        order1.setOrderNo(1);
        order1.setItem(item1);
        order1.setQty(10);
        order1.setPrice(10);

        Mockito.when(orderService.findById(order1.getOrderNo())).thenReturn(Optional.of(order1));

        mvc.perform(get("/order/O2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    public void getListing_success() throws Exception {
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

        Mockito.when(orderService.findAll(1)).thenReturn(orders);

        mvc.perform(get("/order/page/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].orderNo", is(order1.getOrderNo())))
                .andExpect(jsonPath("$.content[0].qty", is(order1.getQty())))
                .andExpect(jsonPath("$.content[1].orderNo", is(order2.getOrderNo())))
                .andExpect(jsonPath("$.content[1].qty", is(order2.getQty())));
    }

    @Test
    public void getListing_failed() throws Exception {
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

        Mockito.when(orderService.findAll(1)).thenReturn(orders);

        mvc.perform(get("/order/page/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Page cannot be less than 1")));
    }

    @Test
    public void save_success() throws Exception {
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

        Gson gson = new Gson();
        String json = gson.toJson(orderRequest);

        Mockito.when(orderService.save(orderRequest)).thenReturn(order1);

        mvc.perform(post("/order/")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void save_failed() throws Exception {
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

        Gson gson = new Gson();
        String json = gson.toJson(orderRequest);

        Mockito.when(orderService.save(ArgumentMatchers.any(OrderRequest.class)))
                .thenThrow(new NonUniqueResultException("Multiple item with name " + orderRequest.getItemName()));

        mvc.perform(post("/order/")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void edit_success() throws Exception {
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

        Gson gson = new Gson();
        String json = gson.toJson(orderRequest);

        Mockito.when(orderService.edit(eq("O1"), ArgumentMatchers.any(OrderRequest.class)))
                .thenReturn(order1);

        mvc.perform(put("/order/O1")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void edit_failed() throws Exception {
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

        Gson gson = new Gson();
        String json = gson.toJson(orderRequest);

        Mockito.when(orderService.edit(eq("O2"), ArgumentMatchers.any(OrderRequest.class)))
                .thenThrow(new NoResultException("No Item with id 2"));

        mvc.perform(put("/order/O2")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_success() throws Exception {
        mvc.perform(delete("/order/O1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
