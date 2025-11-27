package com.example.demo.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Customer_details;

@Repository
public interface Customer_repo extends JpaRepository<Customer_details, Long> {
    Optional<Customer_details> findByEmail(String email); 
}
