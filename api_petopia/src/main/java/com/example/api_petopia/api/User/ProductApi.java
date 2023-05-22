package com.example.api_petopia.api.User;

import com.example.api_petopia.entity.Category;
import com.example.api_petopia.entity.Product;
import com.example.api_petopia.entity.myenum.ProductSimpleStatus;
import com.example.api_petopia.service.ProductService;
import com.example.api_petopia.spec.User.Specifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/api/v1/users/products")
public class ProductApi {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public List<Product> searchProduct(@RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "description", required = false) String description,
                                       @RequestParam(value = "qty", required = false) Integer qty,
                                       @RequestParam(value = "sold", required = false) Integer sold,
                                       @RequestParam(value = "price", required = false) BigDecimal price,
                                       @RequestParam(value = "category", required = false) Category category,
                                       @RequestParam(value = "status", required = false) ProductSimpleStatus status) {
        Specification<Product> specification = Specifications.productSpec(name, description, sold, qty, price, category, status);
        return productService.searchAll(specification);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Product>> findAll(){
        return ResponseEntity.ok(productService.findAllForUser());
    }

    @RequestMapping(method = RequestMethod.GET, path = "{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<Product> optionalProduct = productService.findById(id);
        if (!optionalProduct.isPresent()){
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(optionalProduct.get());
    }

}
