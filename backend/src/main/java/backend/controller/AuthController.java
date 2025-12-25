package backend.controller;

import backend.database.dao.UserDAO;
import backend.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserDAO userDao;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userDao.searchUserByMail(request.getUsername());

        if (userOpt.isPresent() && userOpt.get().getHashedPassword().equals(request.getPassword())) {
            // Generazione Token JWT (usando libreria jjwt)
            String token = Jwts.builder()
                    .setSubject(request.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 giorno
                    .signWith(Keys.hmacShaKeyFor("MioSegretoSuperSicuroPerLaFirmaDelToken123!".getBytes()))
                    .compact();

            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(401).body("Credenziali non valide");
    }
}
