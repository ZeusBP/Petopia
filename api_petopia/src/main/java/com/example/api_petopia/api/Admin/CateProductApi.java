package com.example.api_petopia.api.Admin;

import com.example.api_petopia.entity.Category;
import com.example.api_petopia.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/api/v1/admin/categories")
public class CateProductApi {
    @Autowired
    CategoryService categoryService;
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Category>> findAll(){
        return ResponseEntity.ok(categoryService.findAll());
    }
}
