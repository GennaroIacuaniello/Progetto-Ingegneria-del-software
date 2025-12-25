package backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class Security {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disabilita CSRF perché usiamo token, non sessioni browser
                .csrf(csrf -> csrf.disable())

                // Gestione sessione: STATELESS (Niente cookie JSESSIONID)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Regole di autorizzazione
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll() // Login pubblico
                        .anyRequest().authenticated()               // Il resto richiede token
                )

                // Aggiungiamo il filtro JWT (che creeremo sotto) PRIMA del filtro user/pass standard
                .addFilterBefore(new JWTRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}