package backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe di configurazione della sicurezza di Spring Security.
 * <p>
 * Definisce i componenti fondamentali per l'autenticazione e l'autorizzazione,
 * disabilitando meccanismi non necessari per API stateless (come CSRF) e
 * integrando il filtro JWT personalizzato.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class Security {

    /**
     * Filtro JWT iniettato per essere inserito nella catena di sicurezza.
     */
    private final JWTRequestFilter jwtRequestFilter;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     * * @param jwtRequestFilter Il filtro personalizzato per i token JWT.
     */
    public Security(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Configura la catena dei filtri di sicurezza (Security Filter Chain).
     * <p>
     * Imposta le regole principali:
     *
     * <ul>
     * <li>Disabilita CSRF (non necessario per JWT/Stateless).</li>
     * <li>Autorizza liberamente gli endpoint di autenticazione (/auth/**).</li>
     * <li>Richiede autenticazione per tutte le altre richieste.</li>
     * <li>Imposta la gestione della sessione su STATELESS.</li>
     * <li>Inserisce il {@code JWTRequestFilter} prima del filtro di autenticazione standard.</li>
     * </ul>
     *
     * @param http L'oggetto per la configurazione della sicurezza HTTP.
     * @return La catena di filtri costruita.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                // Disables CSRF because we use tokens, not browser sessions
                .csrf(AbstractHttpConfigurer::disable)

                // Manage sessions: STATELESS (No cookie JSESSIONID)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Login and registration public
                        .anyRequest().authenticated()               // others require a token
                )

                // Adding filter JWT before standard user/pass filter
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Definisce l'algoritmo di hashing per le password.
     * <p>
     * Utilizza BCrypt, standard sicuro per la memorizzazione delle credenziali.
     * </p>
     * * @return L'istanza dell'encoder BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}