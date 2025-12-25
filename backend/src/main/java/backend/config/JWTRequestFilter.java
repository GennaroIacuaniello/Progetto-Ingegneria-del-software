package backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.io.IOException;
import java.util.ArrayList;

public class JWTRequestFilter extends OncePerRequestFilter {

    // Deve essere la stessa chiave usata nel Login!
    private static final String SECRET = "MioSegretoSuperSicuroPerLaFirmaDelToken123!";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 1. Controlla se c'è l'header "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Rimuove "Bearer "
            try {
                // 2. Valida il token ed estrae lo username
                username = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody()
                        .getSubject();
            } catch (Exception e) {
                System.out.println("Token non valido o scaduto");
            }
        }

        // 3. Se abbiamo lo username e nessuno è ancora autenticato nel contesto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Creiamo un oggetto di autenticazione Spring (lista ruoli vuota per semplicità)
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username, null, new ArrayList<>());

            // Impostiamo l'utente nel contesto di sicurezza
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        chain.doFilter(request, response);
    }
}
