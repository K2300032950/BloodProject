package com.example.demo.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Admin_details;

@Repository
public interface Admin_repo extends JpaRepository<Admin_details, Long> {
    Optional<Admin_details> findByEmail(String email); // use email instead of username
}
