package com.example.PocSpringBatch.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
public class EmployeController {

    @GetMapping
    public String test(){
        return "hello moetez";
    }
    @PostMapping
    public String importExcel(@RequestPart("file")MultipartFile file){
        return file.getOriginalFilename();
    }
}
