package frontend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import frontend.dto.UserDTO;
import frontend.exception.RequestError;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserController {

    private static UserController instance;
    private final ApiClient client = ApiClient.getInstance();

    private ArrayList<UserDTO> users;
    private UserDTO user;


    private UserController(){

    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public void searchDevOrAdminByEmailAndProject(String devEmail) {

        try {

            //Da utilizzare dato che non possono esserci spazi nei parametri search delle richieste HTTP
            String encodedDevEmail = URLEncoder.encode(devEmail, StandardCharsets.UTF_8);

            System.out.println("Calling URL: " + client.getBaseUrl() + "/users/developers/search?email=" + devEmail +
                    "&projectId=" + ProjectController.getInstance().getProject().getId());

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(client.getBaseUrl() + "/users/developers/search?email=" + devEmail +
                                    "&projectId=" + ProjectController.getInstance().getProject().getId()))
                    .GET();

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                this.users = client.getObjectMapper().readValue(response.body(), new TypeReference<>() {});


            } else if (response.statusCode() == 204) {

                //Nessun risultato, svuoto la cache
                this.users = new ArrayList<>();

            } else {

                // CASO ERRORE (500, 400, ecc.)
                System.err.println("Errore dal server. Codice: " + response.statusCode());
                System.err.println("Dettaglio errore: " + response.body());
                this.users = new ArrayList<>();

            }

        } catch (RequestError re) {

            System.err.println("Backend offline: " + re.getMessage());
            this.users = new ArrayList<>();

        } catch (Exception e) {

            e.printStackTrace();
            this.users = new ArrayList<>();
        }

        for(UserDTO u: users)
            System.out.println(u.getEmail());

        System.out.println("ciao");

    }

    public List<String> getUsersEmails(){

        ArrayList<String> emails = new ArrayList<>();

        for(UserDTO u: users)
            emails.add(u.getEmail());

        return emails;

    }


}
