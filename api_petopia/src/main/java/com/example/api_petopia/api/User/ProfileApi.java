package com.example.api_petopia.api.User;


import com.example.api_petopia.entity.User;

import com.example.api_petopia.service.ProfileService;
import com.example.api_petopia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProfileApi {
    @Autowired
    ProfileService profileService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity userPanel(Principal principal, Model model) {
        Optional<User> user = userService.findByNameActive(principal.getName());

        if (user != null) {
            System.out.println("Profile" + user);
            model.addAttribute("user", user);
        } else {
            return ResponseEntity.badRequest().body("error/404");
        }

//        return "user"+user.getFullName();
        return ResponseEntity.ok(user);
    }


    //    @RequestMapping(method = RequestMethod.PUT, value = "/profile/update/{id}")
//    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User updateUser){
//        Optional<User> userOptional = profileService.findById(id);
//        if (!userOptional.isPresent()){
//            ResponseEntity.badRequest().build();
//        }
//        User existUser = userOptional.get();
//        existUser.setUpdatedAt(LocalDateTime.now());
//        existUser.setEmail(updateUser.getEmail());
//        existUser.setFullName(updateUser.getFullName());
//        existUser.setThumbnailAvt(updateUser.getThumbnailAvt());
//        existUser.setPhone(updateUser.getPhone());
//        existUser.setAddress(updateUser.getAddress());
//        return ResponseEntity.ok(profileService.save(existUser));
//    }
    @RequestMapping(method = RequestMethod.PUT, value = "/profile/update")
    public ResponseEntity<User> updateProfile(Principal principal, Model model, @RequestBody User updateUser) {
        Optional<User> userOptional = Optional.ofNullable(profileService.findByUsername(principal.getName()));

        if (userOptional != null) {
            System.out.println("Update" + userOptional);
            model.addAttribute("user", userOptional);
        }

        User existUser = userOptional.get();
        existUser.setUpdatedAt(LocalDateTime.now());
        existUser.setEmail(updateUser.getEmail());
        existUser.setFullName(updateUser.getFullName());
        existUser.setThumbnailAvt(updateUser.getThumbnailAvt());
        existUser.setPhone(updateUser.getPhone());
        existUser.setAddress(updateUser.getAddress());
        return ResponseEntity.ok(profileService.save(existUser));
    }
}
