package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.Admin_details;
import com.example.demo.models.Customer_details;
import com.example.demo.repos.Admin_repo;
import com.example.demo.repos.Customer_repo;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class Admin_controller {

    @Autowired
    private Admin_repo adminRepo;

    @Autowired
    private Customer_repo custoRepo;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ========== ADMIN SIGNUP ==========
    @PostMapping("/signup")
    public String signup(@RequestBody Admin_details a) {
        if(adminRepo.findByEmail(a.getEmail()).isPresent()) {
            return "Email already exists";
        }

        a.setPassword(passwordEncoder.encode(a.getPassword()));
        adminRepo.save(a);
        return "Signup successful";
    }

    // ========== ADMIN LOGIN ==========
    @PostMapping("/login")
    public String login(@RequestBody Admin_details a) {
        Optional<Admin_details> p = adminRepo.findByEmail(a.getEmail());

        if(p.isPresent()) {
            if(passwordEncoder.matches(a.getPassword(), p.get().getPassword())) {
                return "Login success";
            } else {
                return "Invalid password";
            }
        }
        return "User not found";
    }

    // ========== DISPLAY CUSTOMERS (only username + email) ==========
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = custoRepo.findAll()
                .stream()
                .map(c -> new CustomerDTO(c.getId(),c.getUsername(), c.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(customers);
    }

    // ========== DELETE CUSTOMER ==========
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        if(custoRepo.existsById(id)) {
            custoRepo.deleteById(id);
            return ResponseEntity.ok("Customer deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }

    // ========= DTO CLASS (only username + email) =========
    static class CustomerDTO {
    	
    	private Long id;
        private String username;
        private String email;

        public CustomerDTO(Long id,String username, String email) {
        	this.id=id;
            this.username = username;
            this.email = email;
        }

        public Long getId() {
	        return id;
	    }
        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }
    }
}
