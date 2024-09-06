package com.yuliawan.backend.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequest {
    @NotNull
    private String name;
    @NotNull
    private Integer price;
}
