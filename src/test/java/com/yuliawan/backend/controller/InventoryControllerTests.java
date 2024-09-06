package com.yuliawan.backend.controller;

import com.google.gson.Gson;
import com.yuliawan.backend.entity.Inventory;
import com.yuliawan.backend.entity.Item;
import com.yuliawan.backend.request.InventoryRequest;
import com.yuliawan.backend.service.InventoryService;
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
@WebMvcTest(InventoryController.class)
public class InventoryControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private InventoryService inventoryService;


    @Test
    public void getData_success() throws Exception {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(10);
        inventory1.setType("T");

        Mockito.when(inventoryService.findById(inventory1.getId())).thenReturn(Optional.of(inventory1));

        mvc.perform(get("/inventory/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getId())))
                .andExpect(jsonPath("$.item.name", is(item1.getName())))
                .andExpect(jsonPath("$.item.price", is(item1.getPrice())))
                .andExpect(jsonPath("$.qty", is(inventory1.getQty())))
                .andExpect(jsonPath("$.type", is(inventory1.getType())));

    }

    @Test
    public void getData_failed() throws Exception {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(10);
        inventory1.setType("T");

        Mockito.when(inventoryService.findById(inventory1.getId())).thenReturn(Optional.of(inventory1));

        mvc.perform(get("/inventory/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    public void getListing_success() throws Exception {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(10);
        inventory1.setType("T");

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Pen");
        item2.setPrice(7);

        Inventory inventory2 = new Inventory();
        inventory2.setId(1);
        inventory2.setItem(item2);
        inventory2.setQty(10);
        inventory2.setType("T");

        List<Inventory> inventoryList = Arrays.asList(inventory1, inventory2);

        Page<Inventory> inventories = new PageImpl<>(inventoryList);

        Mockito.when(inventoryService.findAll(1)).thenReturn(inventories);

        mvc.perform(get("/inventory/page/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].item.name", is(item1.getName())))
                .andExpect(jsonPath("$.content[0].item.price", is(item1.getPrice())))
                .andExpect(jsonPath("$.content[1].item.name", is(item2.getName())))
                .andExpect(jsonPath("$.content[1].item.price", is(item2.getPrice())));
    }

    @Test
    public void getListing_failed() throws Exception {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(10);
        inventory1.setType("T");

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Pen");
        item2.setPrice(7);

        Inventory inventory2 = new Inventory();
        inventory2.setId(1);
        inventory2.setItem(item2);
        inventory2.setQty(10);
        inventory2.setType("T");

        List<Inventory> inventoryList = Arrays.asList(inventory1, inventory2);

        Page<Inventory> inventories = new PageImpl<>(inventoryList);

        Mockito.when(inventoryService.findAll(1)).thenReturn(inventories);

        mvc.perform(get("/inventory/page/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Page cannot be less than 1")));
    }

    @Test
    public void save_success() throws Exception {
        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setItemName("Book");
        inventoryRequest.setQty(10);
        inventoryRequest.setType("T");

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(inventoryRequest.getItemName());
        item1.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(inventoryRequest.getQty());
        inventory1.setType(inventoryRequest.getType());

        Gson gson = new Gson();
        String json = gson.toJson(inventoryRequest);

        Mockito.when(inventoryService.save(inventoryRequest)).thenReturn(inventory1);

        mvc.perform(post("/inventory/")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void save_failed() throws Exception {
        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setItemName("Book");
        inventoryRequest.setQty(10);
        inventoryRequest.setType("T");

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(inventoryRequest.getItemName());
        item1.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(inventoryRequest.getQty());
        inventory1.setType(inventoryRequest.getType());

        Gson gson = new Gson();
        String json = gson.toJson(inventoryRequest);

        Mockito.when(inventoryService.save(ArgumentMatchers.any(InventoryRequest.class))).thenThrow(new NonUniqueResultException("Multiple item with name " + inventoryRequest.getItemName()));

        mvc.perform(post("/inventory/")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void edit_success() throws Exception {
        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setItemName("Book");
        inventoryRequest.setQty(10);
        inventoryRequest.setType("T");

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(inventoryRequest.getItemName());
        item1.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(inventoryRequest.getQty());
        inventory1.setType(inventoryRequest.getType());

        Gson gson = new Gson();
        String json = gson.toJson(inventoryRequest);

        Mockito.when(inventoryService.edit(eq(1), ArgumentMatchers.any(InventoryRequest.class)))
                .thenReturn(inventory1);

        mvc.perform(put("/inventory/1")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void edit_failed() throws Exception {
        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setItemName("Book");
        inventoryRequest.setQty(10);
        inventoryRequest.setType("T");

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(inventoryRequest.getItemName());
        item1.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(inventoryRequest.getQty());
        inventory1.setType(inventoryRequest.getType());

        Gson gson = new Gson();
        String json = gson.toJson(inventoryRequest);

        Mockito.when(inventoryService.edit(eq(2), ArgumentMatchers.any(InventoryRequest.class)))
                .thenThrow(new NoResultException("No Item with id 2"));

        mvc.perform(put("/inventory/2")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_success() throws Exception {
        mvc.perform(delete("/inventory/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
