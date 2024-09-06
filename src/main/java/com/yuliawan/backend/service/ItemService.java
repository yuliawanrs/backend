package com.yuliawan.backend.service;

import com.yuliawan.backend.entity.Item;
import com.yuliawan.backend.request.ItemRequest;
import jakarta.persistence.NoResultException;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ItemService  {
    Optional<Item> findById(Integer id);
    Page<Item> findAll(Integer pageRequested);
    Item save(ItemRequest itemRequest);
    Item edit(Integer id, ItemRequest itemRequest) throws NoResultException;
    void delete(Integer id);
}
