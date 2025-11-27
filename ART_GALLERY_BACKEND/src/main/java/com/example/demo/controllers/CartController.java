package com.example.demo.controllers;


import com.example.demo.models.CartItem;
import com.example.demo.repos.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    // Add item to cart
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestParam("artName") String artName,
            @RequestParam("artDescription") String artDescription,
            @RequestParam("artistName") String artistName,
            @RequestParam("artCost") Double artCost,
            @RequestParam("artPicture") MultipartFile artPicture
    ) {
        try {
            CartItem item = new CartItem();
            item.setArtName(artName);
            item.setArtDescription(artDescription);
            item.setArtistName(artistName);
            item.setArtCost(artCost);
            item.setArtPicture(artPicture.getBytes());

            cartRepository.save(item);
            return ResponseEntity.ok("Item added to cart!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to add item: " + e.getMessage());
        }
    }

    // Get all cart items
    @GetMapping("/all")
    public List<CartItem> getCartItems() {
        return cartRepository.findAll();
    }

    // Get image by ID
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getCartItemImage(@PathVariable Long id) {
        Optional<CartItem> item = cartRepository.findById(id);
        if (item.isPresent()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + item.get().getArtName() + ".jpg\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(item.get().getArtPicture());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Remove item from cart
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Long id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return ResponseEntity.ok("Item removed from cart!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
