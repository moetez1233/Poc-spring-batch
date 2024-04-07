package com.example.PocSpringBatch.config.employeConfig;

import com.example.PocSpringBatch.models.Employe;
import org.springframework.batch.item.ItemProcessor;

public class EmployeProcessor implements ItemProcessor<Employe,Employe> {
    @Override
    public Employe process(Employe item) throws Exception {
        System.out.println(item);
        return item;
    }
}
