package com.example.api_petopia.api.Admin;

import com.example.api_petopia.entity.Role;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.UserStatus;
import com.example.api_petopia.service.UserService;
import com.example.api_petopia.spec.Admin.Specifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UsersApi {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Page<User> SearchLocation(@RequestParam(value = "username", required = false) String username,
                                     @RequestParam(value = "role", required = false) Role role,
                                     @RequestParam(value = "fullName", required = false) String fullName,
                                     @RequestParam(value = "email", required = false) String email,
                                     @RequestParam(value = "phone", required = false) String phone,
                                     @RequestParam(value = "address", required = false) String address,
                                     @RequestParam(value = "status", required = false) UserStatus status,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "limit", defaultValue = "10") int limit

    ) {
        Specification<User> specification = Specifications.userSpec(username,role, fullName, email, phone, address, status, page, limit);
        return userService.searchAll(specification,page,limit);
    }

    @RequestMapping(method = RequestMethod.GET, path = "user/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(optionalUser.get());
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/user/delete/{id}")
    public ResponseEntity<User> delete(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        User existUser = userOptional.get();
        existUser.setStatus(UserStatus.DELETED);
        return ResponseEntity.ok(userService.save(existUser));
    }
    @RequestMapping(method = RequestMethod.PUT, path = "/user/block/{id}")
    public ResponseEntity<User> block(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        User existUser = userOptional.get();
        existUser.setStatus(UserStatus.BLOCKED);
        return ResponseEntity.ok(userService.save(existUser));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/user/change/{id}")
    public ResponseEntity<User> change(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        User existUser = userOptional.get();
        existUser.setStatus(UserStatus.ACTIVE);
        return ResponseEntity.ok(userService.save(existUser));
    }
    @RequestMapping(method = RequestMethod.GET, path = "/user/total")
    public int totalUser(){
        return userService.totalUser();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/user/totalByStatus/{status}")
    public int totalUserByStatus(@PathVariable int status){
        return userService.totalUserByStatus(status);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/user/totalByRole/{role}")
    public int totalUserByRole(@PathVariable Integer role){
        return userService.totalUserByRole(role);
    }
}
