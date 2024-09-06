package com.yuliawan.backend.service.impl;

import com.yuliawan.backend.entity.Inventory;
import com.yuliawan.backend.entity.Item;
import com.yuliawan.backend.repository.InventoryRepository;
import com.yuliawan.backend.repository.ItemRepository;
import com.yuliawan.backend.request.InventoryRequest;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InventoryServiceImplTests {

    @Autowired
    InventoryServiceImpl inventoryService;
    @MockBean
    InventoryRepository inventoryRepository;
    @MockBean
    ItemRepository itemRepository;

    @Test
    public void findById_success() {
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Book");
        item1.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(10);
        inventory1.setType("T");

        Mockito.when(inventoryRepository.findById(item1.getId())).thenReturn(Optional.of(inventory1));

        Optional<Inventory> inventoryChecked = inventoryService.findById(1);

        assertTrue(inventoryChecked.isPresent());
        assertEquals(inventoryChecked.get(), inventory1);
    }

    @Test
    public void findAll_success() {
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

        Mockito.when(inventoryRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(inventories);

        Page<Inventory> itemChecked = inventoryService.findAll(1);

        assertEquals(itemChecked, inventories);
    }

    @Test
    public void findAll_failed() {
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

        Mockito.when(inventoryRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(inventories);

        Page<Inventory> itemChecked = inventoryService.findAll(0);

        assertNull(itemChecked);
    }

    @Test
    public void save_success() {
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


        Mockito.when(inventoryRepository.save(ArgumentMatchers.any(Inventory.class))).thenReturn(inventory1);
        Mockito.when(itemRepository.findByName(ArgumentMatchers.any(String.class))).thenReturn(List.of(item1));

        Inventory itemChecked = inventoryService.save(inventoryRequest);

        assertEquals(itemChecked, inventory1);
    }

    @Test
    public void save_failedEmpty() {
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


        Mockito.when(inventoryRepository.save(ArgumentMatchers.any(Inventory.class))).thenReturn(inventory1);
        Mockito.when(itemRepository.findByName(ArgumentMatchers.any(String.class))).thenReturn(new ArrayList<>());

        assertThrows(NoResultException.class, () -> inventoryService.save(inventoryRequest));
    }

    @Test
    public void save_failedDouble() {
        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setItemName("Book");
        inventoryRequest.setQty(10);
        inventoryRequest.setType("T");

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(inventoryRequest.getItemName());
        item1.setPrice(10);

        Item item2 = new Item();
        item2.setId(1);
        item2.setName(inventoryRequest.getItemName());
        item2.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(inventoryRequest.getQty());
        inventory1.setType(inventoryRequest.getType());


        Mockito.when(inventoryRepository.save(ArgumentMatchers.any(Inventory.class))).thenReturn(inventory1);
        Mockito.when(itemRepository.findByName(ArgumentMatchers.any(String.class))).thenReturn(List.of(item1, item2));

        assertThrows(NonUniqueResultException.class, () -> inventoryService.save(inventoryRequest));
    }

    @Test
    public void edit_success() {
        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setItemName("Book");
        inventoryRequest.setQty(10);
        inventoryRequest.setType("T");

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(inventoryRequest.getItemName());
        item1.setPrice(10);

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Pen");
        item2.setPrice(10);

        Item item3 = new Item();
        item3.setId(3);
        item3.setName("Pen");
        item3.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(inventoryRequest.getQty());
        inventory1.setType(inventoryRequest.getType());

        Mockito.when(inventoryRepository.findById(inventory1.getId())).thenReturn(Optional.of(inventory1));
        Mockito.when(itemRepository.findByName(item1.getName())).thenReturn(List.of(item1));
        Mockito.when(itemRepository.findByName(item2.getName())).thenReturn(List.of(item2, item3));
        Mockito.when(itemRepository.findByName("Pencil")).thenReturn(new ArrayList<>());
        Mockito.when(inventoryRepository.save(ArgumentMatchers.any(Inventory.class))).thenReturn(inventory1);

        Inventory itemChecked = inventoryService.edit(1,inventoryRequest);

        assertEquals(itemChecked, inventory1);
    }

    @Test
    public void edit_failedNoInventoryData() {
        InventoryRequest inventoryRequest1 = new InventoryRequest();
        inventoryRequest1.setItemName("Book");
        inventoryRequest1.setQty(10);
        inventoryRequest1.setType("T");

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(inventoryRequest1.getItemName());
        item1.setPrice(10);

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Pen");
        item2.setPrice(10);

        Item item3 = new Item();
        item3.setId(3);
        item3.setName("Pen");
        item3.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(inventoryRequest1.getQty());
        inventory1.setType(inventoryRequest1.getType());

        Mockito.when(inventoryRepository.findById(inventory1.getId())).thenReturn(Optional.of(inventory1));
        Mockito.when(itemRepository.findByName(item1.getName())).thenReturn(List.of(item1));
        Mockito.when(itemRepository.findByName(item2.getName())).thenReturn(List.of(item2, item3));
        Mockito.when(itemRepository.findByName("Pencil")).thenReturn(new ArrayList<>());
        Mockito.when(inventoryRepository.save(ArgumentMatchers.any(Inventory.class))).thenReturn(inventory1);

        assertThrows(NoResultException.class, () -> inventoryService.edit(2,inventoryRequest1));
    }

    @Test
    public void edit_failedNoItemData() {
        InventoryRequest inventoryRequest1 = new InventoryRequest();
        inventoryRequest1.setItemName("Book");
        inventoryRequest1.setQty(10);
        inventoryRequest1.setType("T");

        InventoryRequest inventoryRequest2 = new InventoryRequest();
        inventoryRequest2.setItemName("Pencil");
        inventoryRequest2.setQty(10);
        inventoryRequest2.setType("T");

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(inventoryRequest1.getItemName());
        item1.setPrice(10);

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Pen");
        item2.setPrice(10);

        Item item3 = new Item();
        item3.setId(3);
        item3.setName("Pen");
        item3.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(inventoryRequest1.getQty());
        inventory1.setType(inventoryRequest1.getType());

        Mockito.when(inventoryRepository.findById(inventory1.getId())).thenReturn(Optional.of(inventory1));
        Mockito.when(itemRepository.findByName(item1.getName())).thenReturn(List.of(item1));
        Mockito.when(itemRepository.findByName(item2.getName())).thenReturn(List.of(item2, item3));
        Mockito.when(itemRepository.findByName("Pencil")).thenReturn(new ArrayList<>());
        Mockito.when(inventoryRepository.save(ArgumentMatchers.any(Inventory.class))).thenReturn(inventory1);

        assertThrows(NoResultException.class, () -> inventoryService.edit(1,inventoryRequest2));
    }

    @Test
    public void edit_failedMultipleItemData() {
        InventoryRequest inventoryRequest1 = new InventoryRequest();
        inventoryRequest1.setItemName("Book");
        inventoryRequest1.setQty(10);
        inventoryRequest1.setType("T");

        InventoryRequest inventoryRequest2 = new InventoryRequest();
        inventoryRequest2.setItemName("Pen");
        inventoryRequest2.setQty(10);
        inventoryRequest2.setType("T");

        Item item1 = new Item();
        item1.setId(1);
        item1.setName(inventoryRequest1.getItemName());
        item1.setPrice(10);

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Pen");
        item2.setPrice(10);

        Item item3 = new Item();
        item3.setId(3);
        item3.setName("Pen");
        item3.setPrice(10);

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setItem(item1);
        inventory1.setQty(inventoryRequest1.getQty());
        inventory1.setType(inventoryRequest1.getType());

        Mockito.when(inventoryRepository.findById(inventory1.getId())).thenReturn(Optional.of(inventory1));
        Mockito.when(itemRepository.findByName(item1.getName())).thenReturn(List.of(item1));
        Mockito.when(itemRepository.findByName(item2.getName())).thenReturn(List.of(item2, item3));
        Mockito.when(itemRepository.findByName("Pencil")).thenReturn(new ArrayList<>());
        Mockito.when(inventoryRepository.save(ArgumentMatchers.any(Inventory.class))).thenReturn(inventory1);

        assertThrows(NonUniqueResultException.class, () -> inventoryService.edit(1,inventoryRequest2));
    }
}
