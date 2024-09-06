package com.yuliawan.backend.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryRequest {
    @NotNull
    private String itemName;
    @NotNull
    private Integer qty;
    @NotNull
    @Pattern(regexp = "^(T|F){1}\b")
    private String type;
}
