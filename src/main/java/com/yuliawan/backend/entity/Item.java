package com.yuliawan.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Setter
@Getter
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private Integer price;

    @JsonIgnore
    @OneToMany(mappedBy = "item")
    private List<Inventory> inventory = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "item")
    private List<Order> order = new ArrayList<>();

    public Integer getRemainingStock() {
        AtomicReference<Integer> stock = new AtomicReference<>(0);
        this.inventory.forEach(i -> {
            if("T".equalsIgnoreCase(i.getType())) {
                stock.set(stock.get() + i.getQty());
            } else {
                stock.set(stock.get() - i.getQty());
            }
        });
        this.order.forEach(o -> stock.set(stock.get() - o.getQty()));
        return stock.get();
    }
}
