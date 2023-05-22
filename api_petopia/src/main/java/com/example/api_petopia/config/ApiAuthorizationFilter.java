package com.example.api_petopia.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.api_petopia.util.JWTUtil;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ApiAuthorizationFilter extends OncePerRequestFilter {
    private static final String[] IGNORE_PATH = {"/api/v1/login", "/api/v1/register", "/api/v1/token/refresh"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // nếu client gửi request đến 1 trong các url được bỏ qua thì không cần xử lý
        String requestPath = request.getServletPath();
        if (Arrays.asList(IGNORE_PATH).contains(requestPath)) {
            // cho qua
            filterChain.doFilter(request, response);
            return;
        }

        // trường hợp client không có request header theo format cần thiết
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            // cho qua (không có dấu kiểm duyệt)
            filterChain.doFilter(request, response);
//            return;
        }

        //lấy token từ request header
        try {
            // remove chữ Bearer
            String token = authorizationHeader.replace("Bearer ", "").trim();

            // dịch ngược JWT
            DecodedJWT decodedJWT = JWTUtil.getDecodedJwt(token);

            // lấy thông tin username
            String username = decodedJWT.getSubject();

            String role = decodedJWT.getClaim(JWTUtil.ROLE_CLAIM_KEY).asString();

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            authorities.add(new SimpleGrantedAuthority(role));

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Map<String, String> errors = new HashMap<>();
            errors.put("message", e.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new Gson().toJson(errors));
        }
    }
}
