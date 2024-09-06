package com.yuliawan.backend.service.impl;

import com.yuliawan.backend.entity.Item;
import com.yuliawan.backend.repository.ItemRepository;
import com.yuliawan.backend.request.ItemRequest;
import com.yuliawan.backend.service.ItemService;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemRepository itemRepository;

    @Override
    public Optional<Item> findById(Integer id) {
        return itemRepository.findById(id);
    }

    @Override
    public Page<Item> findAll(Integer pageRequested) {
        if(pageRequested > 0) {
            Pageable request = PageRequest.of(pageRequested - 1, 3);
            return itemRepository.findAll(request);
        }
        return null;
    }

    @Override
    public Item save(ItemRequest itemRequest) {
        Item item = new Item();
        item.setName(itemRequest.getName());
        item.setPrice(itemRequest.getPrice());
        return itemRepository.save(item);
    }

    @Override
    public Item edit(Integer id, ItemRequest itemRequest) throws NoResultException {
        Optional<Item> item = findById(id);
        if(item.isPresent()) {
            Item itemEdited = item.get();
            itemEdited.setName(itemRequest.getName());
            itemEdited.setPrice(itemRequest.getPrice());
            return itemRepository.save(itemEdited);
        } else {
            throw new NoResultException("No Item with id " + id);
        }
    }

    @Override
    public void delete(Integer id) {
        itemRepository.deleteById(id);
    }
}
