package com.yuliawan.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderNo;

    @ManyToOne
    @JoinColumn(name="ITEM_ID")
    @JsonIgnoreProperties({"inventory", "order"})
    private Item item;

    @Column(name = "QTY")
    private Integer qty;

    @Column(name = "PRICE")
    private Integer price;

    public String getOrderNo() {
        return "O".concat(orderNo.toString());
    }
}
