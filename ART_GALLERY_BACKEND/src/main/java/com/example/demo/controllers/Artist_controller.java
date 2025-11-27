package com.example.demo.controllers;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.Artist_details;
import com.example.demo.repos.Artist_repo;

@RestController
@RequestMapping("/artist")
@CrossOrigin(origins = "*")

public class Artist_controller {

    @Autowired
    private Artist_repo artRepo;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public String signup(@RequestBody Artist_details a) {
        if(artRepo.findByEmail(a.getEmail()).isPresent()) { // check by email
            return "Email already exists";
        }

        a.setPassword(passwordEncoder.encode(a.getPassword()));
        artRepo.save(a);
        return "Signup successful";
    }

    @PostMapping("/login")
    public String login(@RequestBody Artist_details a) {
        Optional<Artist_details> p = artRepo.findByEmail(a.getEmail()); // login by email

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

