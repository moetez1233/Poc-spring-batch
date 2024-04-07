package com.example.PocSpringBatch.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table
@Data
public class Employe {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String country;
    private Integer telephone;

}
