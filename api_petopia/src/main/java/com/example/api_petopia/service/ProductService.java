package com.example.api_petopia.service;

import com.example.api_petopia.entity.Product;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.ProductSimpleStatus;
import com.example.api_petopia.entity.myenum.UserStatus;
import com.example.api_petopia.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Iterable<Product> findAllForUser(){
        return productRepository.findAllByStatusEquals(ProductSimpleStatus.ACTIVE);
    }

    public Optional<Product> findById(Long id){
        return productRepository.findById(id);
    }

    public Product save(Product product){
        return productRepository.save(product);
    }


    public List<Product> searchAll(Specification<Product> specification){
        return productRepository.findAll(specification);
    }

    public Page<Product> searchAllForAdmin(Specification<Product> specification, int page, int limit){
        return productRepository.findAll(specification,PageRequest.of(page, limit,Sort.by("updatedAt").descending()));
    }
    public int totalProduct(){
        return productRepository.findAll().size();
    }

    public int totalProductByStatus(int status){
        ProductSimpleStatus status1 = ProductSimpleStatus.ACTIVE;
        if (status == 0){
            status1 = ProductSimpleStatus.DEACTIVE;
        }
        if (status == 2){
            status1 = ProductSimpleStatus.DELETED;
        }
        return productRepository.findAllByStatus(status1).size();
    }
}
