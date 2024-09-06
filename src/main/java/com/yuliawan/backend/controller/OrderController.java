package com.yuliawan.backend.controller;

import com.yuliawan.backend.entity.Order;
import com.yuliawan.backend.request.OrderRequest;
import com.yuliawan.backend.service.OrderService;
import com.yuliawan.backend.util.Response;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;
    @GetMapping("/{id}")
    public ResponseEntity<Object> getData(@PathVariable String id) {
        Optional<Order> order = orderService.findById(id);
        HttpStatus status = order.isPresent() ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(order.orElse(null));
    }
    @GetMapping("/page/{page}")
    public ResponseEntity<Object> getListing(@PathVariable Integer page) {
        if (page != null && page < 1) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Response("Page cannot be less than 1"));
        }
        Page<Order> orders = orderService.findAll(page);
        HttpStatus status = orders != null ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orders);
    }

    @PostMapping("/")
    public ResponseEntity<Object> save(@RequestBody OrderRequest orderRequest) {
        try {
            Order order = orderService.save(orderRequest);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(order);
        } catch (NonUniqueResultException | NoResultException | IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Response(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@PathVariable String id, @RequestBody OrderRequest orderRequest) {
        try {
            Order order = orderService.edit(id, orderRequest);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(order);
        } catch (NonUniqueResultException | NoResultException | IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Response(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable String id) {
        orderService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Response("Data deleted"));
    }


}
