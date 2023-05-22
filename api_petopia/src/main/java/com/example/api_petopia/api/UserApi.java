package com.example.api_petopia.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.api_petopia.entity.BlogPet;
import com.example.api_petopia.entity.Role;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.dto.CredentialDto;
import com.example.api_petopia.entity.dto.LoginDto;
import com.example.api_petopia.entity.dto.UserDto;
import com.example.api_petopia.service.UserService;
import com.example.api_petopia.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserApi {
    @Autowired
    UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        // co the tien hanh validate
        if (userDto.getPassword().length() < 8) {
            return ResponseEntity.badRequest().body("password too short");
        }
        if (!userDto.getPassword().equals(userDto.getRePass())) {
            return ResponseEntity.badRequest().body("password not match");
        }
        User user = userService.register(userDto);
        if (user == null) {
            return new ResponseEntity<>("Server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        if (loginDto.getUsername() == null) {
            return ResponseEntity.badRequest().body("username missing");
        }
        if (loginDto.getPassword() == null) {
            return ResponseEntity.badRequest().body("password missing");
        }
        UserDetails userDetail = userService.loadUserByUsername(loginDto.getUsername());
        if (userDetail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user not found");
        }
        if (userService.matchPassword(loginDto.getPassword(), userDetail.getPassword())) {
            CredentialDto credentialDto = userService.generateCredential(userDetail, request);
            return ResponseEntity.status(HttpStatus.OK).body(credentialDto);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login fails");
    }


    @RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("require token in header");
        }
        try {
            String token = authorizationHeader.replace("Bearer", "").trim();
            DecodedJWT decodedJWT = JWTUtil.getDecodedJwt(token);
            String username = decodedJWT.getSubject();
            //load user in the token
            User user = userService.findByNameActive(username).get();
            if (user == null) {
                return ResponseEntity.badRequest().body("Wrong token: Username not exist");
            }
            //now return new token
            //generate tokens
            String role = decodedJWT.getClaim(JWTUtil.ROLE_CLAIM_KEY).asString();

//            authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
            String accessToken = JWTUtil.generateToken(
                    user.getUsername(),
                    role,
                    request.getRequestURL().toString(),
                    JWTUtil.ONE_DAY * 7);
            String refreshToken = JWTUtil.generateToken(
                    user.getUsername(),
                    null,
                    request.getRequestURL().toString(),
                    JWTUtil.ONE_DAY * 14);
            CredentialDto credential = new CredentialDto(accessToken, refreshToken);
            return ResponseEntity.ok(credential);
        } catch (Exception ex) {
            //show error
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "users/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(optionalUser.get());
    }
}
