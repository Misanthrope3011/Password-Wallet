package com.example.demo.repository;

import com.example.demo.entities.DataChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<DataChange, Long> {
}
