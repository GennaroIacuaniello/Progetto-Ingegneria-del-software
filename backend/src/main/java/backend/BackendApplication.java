package backend;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {

		Dotenv dotenv = null;

		// STRATEGIA ROBUSTA DI CARICAMENTO .ENV
		try {
			// TENTATIVO 1: Cerca nella "Working Directory" attuale.
			// (Funziona se lanci il jar da terminale dentro /backend o se l'IDE è configurato su /backend)
			dotenv = Dotenv.configure().load();
		} catch (DotenvException e) {
			try {
				// TENTATIVO 2: Se fallisce, prova a cercare esplicitamente nella cartella "./backend"
				// (Funziona se l'IDE lancia il progetto dalla root "Progetto-Ingegneria...")
				dotenv = Dotenv.configure().directory("./backend").load();
			} catch (DotenvException innerE) {
				// TENTATIVO 3: Se non lo trova da nessuna parte, non crashare subito.
				// Magari le variabili sono settate nel sistema operativo (Docker/Cloud).
				System.err.println("ATTENZIONE: File .env non trovato. Si spera che le variabili d'ambiente siano impostate nel sistema.");
				dotenv = Dotenv.configure().ignoreIfMissing().load();
			}
		}

		// Se abbiamo trovato qualcosa, iniettiamo le variabili in Spring
		if (dotenv != null) {
			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		}
		SpringApplication.run(BackendApplication.class, args);
	}

}
