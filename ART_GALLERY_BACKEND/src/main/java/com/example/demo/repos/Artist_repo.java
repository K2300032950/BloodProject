package com.example.demo.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Artist_details;

@Repository
public interface Artist_repo extends JpaRepository<Artist_details, Long> {
    Optional<Artist_details> findByEmail(String email); 
}

