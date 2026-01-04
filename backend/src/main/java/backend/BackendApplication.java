package backend;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class BackendApplication {

	private static final Logger logger = LoggerFactory.getLogger(BackendApplication.class);

	public static void main(String[] args) {

		Dotenv dotenv;

		try {

			dotenv = Dotenv.configure().load();

		} catch (DotenvException e) {
			try {

				dotenv = Dotenv.configure().directory("./backend").load();

			} catch (DotenvException innerE) {

				logger.warn("Attention: File .env not found. I'll try with system environment variables.");
				dotenv = Dotenv.configure().ignoreIfMissing().load();

			}
		}

		if (dotenv != null) {
			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		}
		
		SpringApplication.run(BackendApplication.class, args);
	}

}
