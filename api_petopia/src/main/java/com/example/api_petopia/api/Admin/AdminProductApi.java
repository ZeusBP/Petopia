package com.example.api_petopia.api.Admin;

import com.example.api_petopia.entity.*;
import com.example.api_petopia.entity.dto.ProductDto;
import com.example.api_petopia.entity.myenum.ProductSimpleStatus;
import com.example.api_petopia.service.CategoryService;
import com.example.api_petopia.service.ProductService;
import com.example.api_petopia.spec.Admin.Specifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/api/v1/admin/products")
public class AdminProductApi {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping( method = RequestMethod.GET)
    public Page<Product> searchProduct(@RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "description", required = false) String description,
                                       @RequestParam(value = "category", required = false) Category category,
                                       @RequestParam(value = "status", required = false) ProductSimpleStatus status,
                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
//        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        Specification<Product> specification = Specifications.productSpec(name,description,category,status,page,limit);
        return productService.searchAllForAdmin(specification,page,limit);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Product> save(@RequestBody Product product) {
        product.setSold(0);
        product.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(productService.save(product));
    }

    @RequestMapping(method = RequestMethod.GET, path = "{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Product> optionalProduct = productService.findById(id);
        if (!optionalProduct.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(optionalProduct.get());
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Optional<Product> optionalProduct = productService.findById(id);
        if (!optionalProduct.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        Product existProduct = optionalProduct.get();
        existProduct.setStatus(ProductSimpleStatus.DELETED);
        return ResponseEntity.ok(productService.save(existProduct));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/change/{id}")
    public ResponseEntity<?> changeProduct(@PathVariable Long id) {
        Optional<Product> optionalProduct = productService.findById(id);
        if (!optionalProduct.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        Product existProduct = optionalProduct.get();
        existProduct.setStatus(ProductSimpleStatus.ACTIVE);
        return ResponseEntity.ok(productService.save(existProduct));
    }


    @RequestMapping(method = RequestMethod.PUT, path = "{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody ProductDto updateProduct) {
        Optional<Product> optionalProduct = productService.findById(id);
        if (!optionalProduct.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        Product existingProduct = optionalProduct.get();
        existingProduct.setName(updateProduct.getName());
        existingProduct.setDescription(updateProduct.getDescription());
        existingProduct.setSold(updateProduct.getSold());
        existingProduct.setPrice(updateProduct.getPrice());
        existingProduct.setImage(updateProduct.getImage());
        Optional<Category> category = categoryService.findById(updateProduct.getCategory().getId());
        existingProduct.setCategory(category.get());
        existingProduct.setThumbnail(updateProduct.getThumbnail());
        existingProduct.setQty(updateProduct.getQty());
        existingProduct.setStatus(updateProduct.getStatus());
        return ResponseEntity.ok(productService.save(existingProduct));
    }
    @RequestMapping(method = RequestMethod.GET, path = "/total")
    public int totalProduct(){
        return productService.totalProduct();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/totalByStatus/{status}")
    public int totalProductByStatus(@PathVariable int status){
        return productService.totalProductByStatus(status);
    }

}
