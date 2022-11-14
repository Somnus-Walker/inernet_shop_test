package com.example.spring_internet_shop.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "roles")
@Entity
@Data
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    private String roleName;


}