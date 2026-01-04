package backend.controller;

import backend.database.dao.UserDAO;
import backend.dto.UserDTO;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

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

    public static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest() {
            //Empty constructor needed for jackson
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private String token;
        private UserDTO user;

        public AuthResponse(String token, UserDTO user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public UserDTO getUser() { return user; }
        public void setUser(UserDTO user) { this.user = user; }
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
        userDao.resisterNewUser(newUser);

        return ResponseEntity.ok("Registration success!");

    }

    public static class RegisterRequest {
        private String email;
        private String password;
        private int role;

        public RegisterRequest() {
            //Empty constructor needed for jackson
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }
    }
}