package com.yuliawan.backend.service.impl;

import com.yuliawan.backend.entity.Item;
import com.yuliawan.backend.repository.ItemRepository;
import com.yuliawan.backend.request.ItemRequest;
import jakarta.persistence.NoResultException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemServiceImplTests {
    @Autowired
    ItemServiceImpl itemService;

    @MockBean
    ItemRepository itemRepository;

    @Test
    public void findById_success() {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Mockito.when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));

        Optional<Item> itemChecked = itemService.findById(1);

        assertTrue(itemChecked.isPresent());
        assertEquals(itemChecked.get(), item1);
    }

    @Test
    public void findAll_success() {
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

        Mockito.when(itemRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(items);

        Page<Item> itemChecked = itemService.findAll(1);

        assertEquals(itemChecked, items);
    }

    @Test
    public void findAll_failed() {
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

        Mockito.when(itemRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(items);

        Page<Item> itemChecked = itemService.findAll(0);

        assertEquals(itemChecked, null);
    }

    @Test
    public void save_success() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Book");
        itemRequest.setPrice(10);

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(itemRequest.getName());
        item1.setPrice(itemRequest.getPrice());


        Mockito.when(itemRepository.save(ArgumentMatchers.any(Item.class))).thenReturn(item1);

        Item itemChecked = itemService.save(itemRequest);

        assertEquals(itemChecked, item1);
    }

    @Test
    public void edit_success() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Book");
        itemRequest.setPrice(10);

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(itemRequest.getName());
        item1.setPrice(itemRequest.getPrice());

        Mockito.when(itemRepository.save(ArgumentMatchers.any(Item.class))).thenReturn(item1);
        Mockito.when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));

        Item itemChecked = itemService.edit(1,itemRequest);

        assertEquals(itemChecked, item1);
    }

    @Test
    public void edit_failed() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setName("Book");
        itemRequest.setPrice(10);

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(itemRequest.getName());
        item1.setPrice(itemRequest.getPrice());

        Mockito.when(itemRepository.save(ArgumentMatchers.any(Item.class))).thenReturn(item1);
        Mockito.when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));

        assertThrows(NoResultException.class,() -> itemService.edit(2, itemRequest));
    }
}
