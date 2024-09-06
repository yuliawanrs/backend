package com.yuliawan.backend.controller;

import com.google.gson.Gson;
import com.yuliawan.backend.entity.Item;
import com.yuliawan.backend.request.ItemRequest;
import com.yuliawan.backend.service.ItemService;
import jakarta.persistence.NoResultException;
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
@WebMvcTest(ItemController.class)
public class ItemControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;


    @Test
    public void getData_success() throws Exception {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Mockito.when(itemService.findById(item1.getId())).thenReturn(Optional.of(item1));

        mvc.perform(get("/item/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getId())))
                .andExpect(jsonPath("$.name", is(item1.getName())))
                .andExpect(jsonPath("$.price", is(item1.getPrice())));

    }

    @Test
    public void getData_failed() throws Exception {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Mockito.when(itemService.findById(item1.getId())).thenReturn(Optional.of(item1));

        mvc.perform(get("/item/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    public void getListing_success() throws Exception {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Pen");
        item2.setPrice(7);

        List<Item> itemList = Arrays.asList(item1, item2);

        Page<Item> items = new PageImpl<>(itemList);

        Mockito.when(itemService.findAll(1)).thenReturn(items);

        mvc.perform(get("/item/page/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name", is(item1.getName())))
                .andExpect(jsonPath("$.content[0].price", is(item1.getPrice())))
                .andExpect(jsonPath("$.content[1].name", is(item2.getName())))
                .andExpect(jsonPath("$.content[1].price", is(item2.getPrice())));
    }

    @Test
    public void getListing_failed() throws Exception {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Pen");
        item2.setPrice(7);

        List<Item> itemList = Arrays.asList(item1, item2);

        Page<Item> items = new PageImpl<>(itemList);

        Mockito.when(itemService.findAll(1)).thenReturn(items);

        mvc.perform(get("/item/page/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Page cannot be less than 1")));
    }

    @Test
    public void save_success() throws Exception {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Book");
        itemRequest.setPrice(10);

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(itemRequest.getName());
        item1.setPrice(itemRequest.getPrice());

        Gson gson = new Gson();
        String json = gson.toJson(itemRequest);

        Mockito.when(itemService.save(itemRequest)).thenReturn(item1);

        mvc.perform(post("/item/")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void edit_success() throws Exception {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Book");
        itemRequest.setPrice(10);

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(itemRequest.getName());
        item1.setPrice(itemRequest.getPrice());

        Gson gson = new Gson();
        String json = gson.toJson(itemRequest);

        Mockito.when(itemService.edit(eq(1), ArgumentMatchers.any(ItemRequest.class)))
                .thenReturn(item1);

        mvc.perform(put("/item/1")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void edit_failed() throws Exception {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Book");
        itemRequest.setPrice(10);

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(itemRequest.getName());
        item1.setPrice(itemRequest.getPrice());

        Gson gson = new Gson();
        String json = gson.toJson(itemRequest);

        Mockito.when(itemService.edit(eq(2), ArgumentMatchers.any(ItemRequest.class)))
                .thenThrow(new NoResultException("No Item with id 2"));

        mvc.perform(put("/item/2")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_success() throws Exception {
        mvc.perform(delete("/item/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
