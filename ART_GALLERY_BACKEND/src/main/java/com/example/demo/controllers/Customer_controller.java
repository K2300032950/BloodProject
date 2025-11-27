package com.example.demo.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.Customer_details;
import com.example.demo.repos.Customer_repo;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "*")
public class Customer_controller {

    @Autowired
    private Customer_repo custoRepo;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public String signup(@RequestBody Customer_details a) {
        if(custoRepo.findByEmail(a.getEmail()).isPresent()) { // check by email
            return "Email already exists";
        }

        a.setPassword(passwordEncoder.encode(a.getPassword()));
        custoRepo.save(a);
        return "Signup successful";
    }

    @PostMapping("/login")
    public String login(@RequestBody Customer_details a) {
        Optional<Customer_details> p = custoRepo.findByEmail(a.getEmail()); // login by email

        if(p.isPresent()) {
            if(passwordEncoder.matches(a.getPassword(), p.get().getPassword())) {
                return "Login success";
            } else {
                return "Invalid password";
            }
        }

        return "User not found";
    }
}
