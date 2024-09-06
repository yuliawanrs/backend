package com.yuliawan.backend.service;

import com.yuliawan.backend.entity.Inventory;
import com.yuliawan.backend.request.InventoryRequest;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface InventoryService {
    Optional<Inventory> findById(Integer id);
    Page<Inventory> findAll(Integer pageRequested);
    Inventory save(InventoryRequest inventoryRequest) throws NonUniqueResultException, NoResultException;
    Inventory edit(Integer id, InventoryRequest inventoryRequest) throws NonUniqueResultException, NoResultException;
    void delete(Integer id);
}
