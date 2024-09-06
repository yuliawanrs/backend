package com.yuliawan.backend.controller;

import com.yuliawan.backend.entity.Inventory;
import com.yuliawan.backend.request.InventoryRequest;
import com.yuliawan.backend.service.InventoryService;
import com.yuliawan.backend.util.Response;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;
    @GetMapping("/{id}")
    public ResponseEntity<Object> getData(@PathVariable Integer id) {
        Optional<Inventory> inventory = inventoryService.findById(id);
        HttpStatus status = inventory.isPresent() ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(inventory.orElse(null));
    }
    @GetMapping("/page/{page}")
    public ResponseEntity<Object> getListing(@PathVariable Integer page) {
        if (page != null && page < 1) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Response("Page cannot be less than 1"));
        }
        Page<Inventory> inventories = inventoryService.findAll(page);
        HttpStatus status = inventories != null ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(inventories);
    }

    @PostMapping("/")
    public ResponseEntity<Object> save(@RequestBody InventoryRequest inventoryRequest) {
        try {
            Inventory inventory = inventoryService.save(inventoryRequest);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(inventory);
        } catch (NonUniqueResultException | NoResultException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Response(e.getMessage()));
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@PathVariable Integer id, @RequestBody InventoryRequest inventoryRequest) {
        try {
            Inventory inventory = inventoryService.edit(id, inventoryRequest);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(inventory);
        } catch (NonUniqueResultException | NoResultException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Response(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        inventoryService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Response("Data deleted"));
    }


}
