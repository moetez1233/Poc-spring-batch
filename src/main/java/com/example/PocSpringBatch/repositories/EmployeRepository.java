package com.example.PocSpringBatch.repositories;

import com.example.PocSpringBatch.models.Employe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeRepository extends JpaRepository<Employe,Long> {
}
