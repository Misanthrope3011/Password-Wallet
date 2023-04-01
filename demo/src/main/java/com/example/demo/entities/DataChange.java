package com.example.demo.entities;

import com.example.demo.enumeration.Action;
import com.example.demo.enumeration.FunctionDESC;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "data_change")
public class DataChange {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private Long auditMU;
    private LocalDateTime auditMD;
    private String prevValue;
    private String currValue;

    @Enumerated(EnumType.STRING)
    private Action actionTypeEntity;

    @Enumerated(EnumType.STRING)
    private FunctionDESC functionDESC;
    private String tableName;


}