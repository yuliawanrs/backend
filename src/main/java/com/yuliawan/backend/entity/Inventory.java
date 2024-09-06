package com.yuliawan.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="ITEM_ID")
    @JsonIgnoreProperties({"inventory", "order"})
    private Item item;

    @Column(name = "QTY")
    private Integer qty;

    @Column(name = "TYPE")
    private String type;
}
