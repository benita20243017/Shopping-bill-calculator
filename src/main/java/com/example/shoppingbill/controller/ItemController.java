package com.example.shoppingbill.controller;

import com.example.shoppingbill.dto.BillRequest;
import com.example.shoppingbill.dto.BillSummary;
import com.example.shoppingbill.model.Item;
import com.example.shoppingbill.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/items")
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {
        Item savedItem = itemService.createItem(item);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @Valid @RequestBody Item itemDetails) {
        try {
            Item updatedItem = itemService.updateItem(id, itemDetails);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        try {
            itemService.deleteItem(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/bill")
    public BillSummary getBillSummary(@RequestParam(defaultValue = "0.10") BigDecimal taxRate,
                                     @RequestParam(defaultValue = "0.00") BigDecimal discountRate) {
        List<Item> items = itemService.getAllItems();

        BigDecimal subtotal = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discountAmount = subtotal.multiply(discountRate);
        BigDecimal taxableAmount = subtotal.subtract(discountAmount);
        BigDecimal taxAmount = taxableAmount.multiply(taxRate);
        BigDecimal total = taxableAmount.add(taxAmount);

        return new BillSummary(subtotal, taxAmount, discountAmount, total, items.size());
    }

    @PostMapping("/bill")
    public BillSummary calculateBill(@RequestBody BillRequest billRequest) {
        List<Item> items = itemService.getAllItems();

        BigDecimal subtotal = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxRate = billRequest.getTaxRate() == null ? BigDecimal.ZERO : billRequest.getTaxRate();
        BigDecimal discountRate = billRequest.getDiscountRate() == null ? BigDecimal.ZERO : billRequest.getDiscountRate();

        BigDecimal discountAmount = subtotal.multiply(discountRate);
        BigDecimal taxableAmount = subtotal.subtract(discountAmount);
        BigDecimal taxAmount = taxableAmount.multiply(taxRate);
        BigDecimal total = taxableAmount.add(taxAmount);

        return new BillSummary(subtotal, taxAmount, discountAmount, total, items.size());
    }
}
