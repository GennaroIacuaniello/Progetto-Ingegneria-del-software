package frontend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import frontend.dto.UserDTO;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class UserController {

    //ESEMPIO DI CONTROLLER REST DEL FRONTEND

    /*
    private final String SERVER_URL = "http://localhost:8080/api/users";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // Logica per scaricare users (GET)
    public List<User> getUsers() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Converte il JSON ricevuto in una lista di oggetti User
            User[] userArray = mapper.readValue(response.body(), User[].class);
            return Arrays.asList(userArray);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Logica per inviare uno user (POST)
    public void registerUser(String email, String hashedPassword) {
        try {
            User userToReg = new User();
            userToReg.setEmail(email);
            userToReg.setHashedPassword(hashedPassword);

            String jsonBody = mapper.writeValueAsString(userToReg);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


}
