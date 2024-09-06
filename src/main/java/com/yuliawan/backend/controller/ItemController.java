package com.yuliawan.backend.controller;

import com.yuliawan.backend.entity.Item;
import com.yuliawan.backend.request.ItemRequest;
import com.yuliawan.backend.service.ItemService;
import com.yuliawan.backend.util.Response;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getData(@PathVariable Integer id) {
        Optional<Item> item = itemService.findById(id);
        HttpStatus status = item.isPresent() ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(item.orElse(null));
    }
    @GetMapping("/page/{page}")
    public ResponseEntity<Object> getListing(@PathVariable Integer page) {
        if (page != null && page < 1) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Response("Page cannot be less than 1"));
        }
        Page<Item> items = itemService.findAll(page);
        HttpStatus status = items != null ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(items);
    }

    @PostMapping("/")
    public ResponseEntity<Object> save(@RequestBody ItemRequest itemRequest) {
        Item item = itemService.save(itemRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@PathVariable Integer id, @RequestBody ItemRequest itemRequest) {
        try {
            Item item = itemService.edit(id, itemRequest);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(item);
        } catch (NoResultException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Response(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        itemService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Response("Data deleted"));
    }
}
