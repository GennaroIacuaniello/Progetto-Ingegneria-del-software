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

		try {

			dotenv = Dotenv.configure().load();

		} catch (DotenvException e) {
			try {

				dotenv = Dotenv.configure().directory("./backend").load();

			} catch (DotenvException innerE) {

				System.err.println("ATTENZIONE: File .env non trovato. Si spera che le variabili d'ambiente siano impostate nel sistema.");
				dotenv = Dotenv.configure().ignoreIfMissing().load();

			}
		}

		if (dotenv != null) {
			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		}
		
		SpringApplication.run(BackendApplication.class, args);
	}

}
