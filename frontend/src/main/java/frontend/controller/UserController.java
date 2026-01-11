package frontend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import frontend.config.ApiPaths;
import frontend.dto.UserDTO;
import frontend.exception.RequestError;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("java:S6548")
public class UserController {

    private static UserController instance;
    private final ApiClient client = ApiClient.getInstance();

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    private ArrayList<UserDTO> users;

    private static final String USERS_PATH = ApiPaths.USERS;
    private static final String DEVELOPER_SEARCH_PATH = "developers/search?email=";

    private UserController(){

    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public boolean searchDevOrAdminByEmailAndProject(String devEmail) {

        //Encoder to use since there cannot be spaces in search parameters of HTTP requests
        String encodedDevEmail = URLEncoder.encode(devEmail, StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(client.getBaseUrl() + USERS_PATH + DEVELOPER_SEARCH_PATH + encodedDevEmail +
                                "&projectId=" + ProjectController.getInstance().getProject().getId()))
                .GET();

        return handleHttpRequest(requestBuilder);

    }

    public boolean searchDevOrAdminByEmailAndTeam(String devEmail) {

        //Encoder to use since there cannot be spaces in search parameters of HTTP requests
        String encodedDevEmail = URLEncoder.encode(devEmail, StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(client.getBaseUrl() + USERS_PATH + DEVELOPER_SEARCH_PATH + encodedDevEmail +
                        "&teamId=" + TeamController.getInstance().getTeam().getId() ))
                .GET();

        return handleHttpRequest(requestBuilder);

    }

    public boolean searchDevOrAdminByEmail(String devEmail) {


        //Encoder to use since there cannot be spaces in search parameters of HTTP requests
        String encodedDevEmail = URLEncoder.encode(devEmail, StandardCharsets.UTF_8);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(client.getBaseUrl() + USERS_PATH + DEVELOPER_SEARCH_PATH + encodedDevEmail))
                .GET();

        return handleHttpRequest(requestBuilder);

    }

    private boolean handleHttpRequest(HttpRequest.Builder requestBuilder){

        try {

            HttpResponse<String> response = client.sendRequest(requestBuilder);

            if (response.statusCode() == 200) {

                this.users = client.getObjectMapper().readValue(response.body(), new TypeReference<>() {});
                logger.log(Level.FINE, "Search completed successfully. Number of users founded: {0}", this.users.size());

                return true;

            } else if (response.statusCode() == 204) {

                //No results, clear the cache
                this.users = new ArrayList<>();

                logger.log(Level.FINE, "Search completed successfully, BUT no users were found. ");

            } else {

                // Generic error

                String errorMsg = client.getErrorMessageFromResponse(response);

                logger.log(Level.WARNING, "Users search failed. Error: {0}", errorMsg);

                this.users = new ArrayList<>();

            }

        }  catch (RequestError re) {

            logger.log(Level.WARNING, "Backend offline: {0}", re.getMessage());
            this.users = new ArrayList<>();

        } catch (Exception e) {

            logger.log(Level.SEVERE, e.getMessage());
            this.users = new ArrayList<>();
        }

        return false;

    }

    public List<String> getUsersEmails(){

        ArrayList<String> emails = new ArrayList<>();

        for(UserDTO u: users)
            emails.add(u.getEmail());

        return emails;

    }


}
