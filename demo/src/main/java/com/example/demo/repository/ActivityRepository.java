package com.example.demo.repository;

import com.example.demo.entities.DataChange;
import com.example.demo.enumeration.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<DataChange, Long> {

    List<DataChange> findByActionTypeEntity(Action action);

}
