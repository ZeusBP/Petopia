package com.example.api_petopia.spec.Admin;

import com.example.api_petopia.entity.*;
import com.example.api_petopia.entity.myenum.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Specifications {
    public static Specification<BlogPet> blogSpec(String name, String age, String breed, String sex, String description, String address, CatePet catePet, TypePet typePet, User user, BlogPetStatus status, int page, int limit){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null &&  !(name.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("name"),"%"+name+"%"));
            if (age != null &&  !(age.isEmpty()))
                predicates.add(criteriaBuilder.equal(root.get("age"),age));
            if (breed != null &&  !(breed.isEmpty()))
                predicates.add(criteriaBuilder.equal(root.get("breed"),breed));
            if (sex != null &&  !(sex.isEmpty()))
                predicates.add(criteriaBuilder.equal(root.get("sex"),sex));
            if (description != null &&  !(description.isEmpty()))
                predicates.add(criteriaBuilder.equal(root.get("description"),description));
            if (address != null &&  !(address.isEmpty()))
                predicates.add(criteriaBuilder.equal(root.get("address"),address));
            if (catePet != null)
                predicates.add(criteriaBuilder.equal(root.get("catePet"),catePet));
            if (typePet != null)
                predicates.add(criteriaBuilder.equal(root.get("typePet"),typePet));
            if (user != null)
                predicates.add(criteriaBuilder.equal(root.get("user"),user));
            if (status != null)
                predicates.add(criteriaBuilder.equal(root.get("status"),status));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
    public static Specification<Product> productSpec(String name, String description, Category category, ProductSimpleStatus status, int page, int limit) {
        return ((root, query, criteriaBuilder) -> {
            Sort sort1 = Sort.by(Sort.Direction.DESC,"id");
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !(name.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            if (description != null && !(description.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("description"), "%" + description + "%"));
            if (category != null )
                predicates.add(criteriaBuilder.equal(root.get("category"), category ));
            if (status != null )
                predicates.add(criteriaBuilder.equal(root.get("status"), status ));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        });
    }
    public static Specification<Location> locationSpec(String nameLocation, String address, String phone, String email, String description, TypeLocation typeLocation,User user, LocationStatus status, int page, int limit){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (nameLocation != null &&  !(nameLocation.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("nameLocation"),"%"+nameLocation+"%"));
            if (address != null &&  !(address.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("address"),"%"+address+"%"));
            if (phone != null &&  !(phone.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("phone"),"%"+phone+"%"));
            if (email != null &&  !(email.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("email"),"%"+email+"%"));
            if (description != null &&  !(description.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("description"),"%"+description+"%"));
            if (typeLocation != null)
                predicates.add(criteriaBuilder.equal(root.get("typeLocation"),typeLocation));
            if (user != null)
                predicates.add(criteriaBuilder.equal(root.get("user"),user));
            if (status != null)
                predicates.add(criteriaBuilder.equal(root.get("status"),status));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
    public static Specification<User> userSpec(String username,Role role, String fullName, String email, String phone, String address, UserStatus status, int page, int limit){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (username != null &&  !(username.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("username"),"%"+username+"%"));
            if (role != null)
                predicates.add(criteriaBuilder.equal(root.get("role"),role));
            if (fullName != null &&  !(fullName.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("fullName"),"%"+fullName+"%"));
            if (email != null &&  !(email.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("email"), "%" +email +"%"));
            if (phone != null &&  !(phone.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("phone"),"%" +phone +"%"));
            if (address != null &&  !(address.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("address"),"%"+address+"%"));
            if (status != null)
                predicates.add(criteriaBuilder.equal(root.get("status"),status));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
    public static Specification<Order> OrderSpec(String name,String address, OrderStatus status, int page, int limit){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null &&  !(name.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("name"),"%"+name+"%"));
            if (address != null &&  !(address.isEmpty()))
                predicates.add(criteriaBuilder.like(root.get("address"),"%"+address+"%"));
            if (status != null)
                predicates.add(criteriaBuilder.equal(root.get("status"),status));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
    public static Specification<OrderDetail> OrderDetailSpec(Product product, Order order, int page, int limit){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (product != null)
                predicates.add(criteriaBuilder.equal(root.get("product"),product));
            if (order != null)
                predicates.add(criteriaBuilder.equal(root.get("order"),order));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

}
