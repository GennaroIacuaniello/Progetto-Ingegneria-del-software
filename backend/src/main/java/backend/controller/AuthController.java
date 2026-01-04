package backend.controller;

import backend.database.dao.UserDAO;
import backend.dto.UserDTO;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

@RestController
public class AuthController {

    private final UserDAO userDao;

    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;

    public AuthController(UserDAO userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) throws SQLException {

        Optional<UserDTO> userOpt = Optional.ofNullable(userDao.searchUserByMail(request.getEmail()));

        if (userOpt.isPresent()) {
            UserDTO userDto = userOpt.get();

            if (passwordEncoder.matches(request.getPassword(), userDto.getPassword())) {

                String token = Jwts.builder()
                        .setSubject(userDto.getEmail())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                        .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                        .compact();

                userDto.setPassword(null);

                return ResponseEntity.ok(new AuthResponse(token, userDto));
            }
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {

        private String email;
        private String password;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponse {

        private String token;
        private UserDTO user;

    }


    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) throws SQLException {

        if (userDao.searchUserByMail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Error: email already used!");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        UserDTO newUser = new UserDTO();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(hashedPassword);
        newUser.setRole(request.getRole());
        userDao.registerNewUser(newUser);

        return ResponseEntity.ok("Registration success!");

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {

        private String email;
        private String password;
        private int role;

    }
}