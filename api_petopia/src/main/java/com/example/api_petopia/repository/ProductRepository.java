package com.example.api_petopia.repository;

import com.example.api_petopia.entity.Product;
import com.example.api_petopia.entity.myenum.ProductSimpleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByIdAndStatus(Long id, ProductSimpleStatus status);


    Page<Product> findAll( Pageable pageable);

    Iterable<Product> findAllByStatusEquals(ProductSimpleStatus productSimpleStatus);
    List<Product> findAllByStatus(ProductSimpleStatus status);

}
