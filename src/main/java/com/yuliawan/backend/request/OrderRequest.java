package com.yuliawan.backend.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    @NotNull
    private String itemName;
    @NotNull
    private Integer qty;
}
