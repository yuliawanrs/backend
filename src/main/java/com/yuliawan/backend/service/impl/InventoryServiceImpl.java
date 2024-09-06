package com.yuliawan.backend.service.impl;

import com.yuliawan.backend.entity.Inventory;
import com.yuliawan.backend.entity.Item;
import com.yuliawan.backend.repository.InventoryRepository;
import com.yuliawan.backend.repository.ItemRepository;
import com.yuliawan.backend.request.InventoryRequest;
import com.yuliawan.backend.service.InventoryService;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    ItemRepository itemRepository;

    @Override
    public Optional<Inventory> findById(Integer id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public Page<Inventory> findAll(Integer pageRequested) {
        if(pageRequested > 0) {
            Pageable request = PageRequest.of(pageRequested - 1, 3);
            return inventoryRepository.findAll(request);
        }
        return null;
    }

    @Override
    public Inventory save(InventoryRequest inventoryRequest) throws NonUniqueResultException, NoResultException {
        List<Item> item = itemRepository.findByName(inventoryRequest.getItemName());
        if(item.isEmpty()) {
            throw new NoResultException("No item with name " + inventoryRequest.getItemName());
        }

        if (item.size() > 1){
            throw new NonUniqueResultException("Multiple item with name " + inventoryRequest.getItemName());
        }

        Inventory inventory = new Inventory();
        inventory.setItem(item.get(0));
        inventory.setQty(inventoryRequest.getQty());
        inventory.setType(inventoryRequest.getType());
        return inventoryRepository.save(inventory);

    }

    @Override
    public Inventory edit(Integer id, InventoryRequest inventoryRequest) throws NonUniqueResultException, NoResultException {
        Optional<Inventory> inventory = findById(id);
        if(inventory.isPresent()) {
            List<Item> item = itemRepository.findByName(inventoryRequest.getItemName());
            if(item.isEmpty()) {
                throw new NoResultException("No item with name " + inventoryRequest.getItemName());
            }

            if (item.size() > 1){
                throw new NonUniqueResultException("Multiple item with name " + inventoryRequest.getItemName());
            }

            Inventory inventoryEdited = inventory.get();
            inventoryEdited.setItem(item.get(0));
            inventoryEdited.setQty(inventoryRequest.getQty());
            inventoryEdited.setType(inventoryRequest.getType());
            return inventoryRepository.save(inventoryEdited);
        } else {
            throw new NoResultException("No Inventory with id " + id);
        }
    }

    @Override
    public void delete(Integer id) {
        inventoryRepository.deleteById(id);
    }
}
