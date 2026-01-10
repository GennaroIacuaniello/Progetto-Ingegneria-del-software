package backend.controller;

import backend.database.dao.IssueDAO;
import backend.database.dao.ProjectDAO;
import backend.database.dao.UserDAO;
import backend.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc // Configures MockMvc for HTTP requests
@WithMockUser(username = "admin", roles = {"ADMIN", "DEVELOPER"})
class IssueControllerTests {

    static {

        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .ignoreIfMissing()
                    .load();

            dotenv.entries().forEach(entry ->
                    System.setProperty(entry.getKey(), entry.getValue())
            );

        } catch (Exception e) {
            System.err.println(".env not found: " + e.getMessage());
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IssueDAO issueDAO;

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private DataSource dataSource;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private int testIssueId;

    private UserDTO adminForTesting;

    @BeforeEach
    void setup() throws Exception {

        IssueDTO testIssue = new IssueDTO();
        testIssue.setTitle("TestIssue_Test");
        testIssue.setDescription("Temporary issue for testing.");
        testIssue.setStatus(IssueStatusDTO.TODO);
        testIssue.setType(IssueTypeDTO.BUG);
        testIssue.setPriority(1);

        testIssue.setReportDate(Date.from(Instant.now()));

        adminForTesting = new UserDTO();
        adminForTesting.setId(0);
        adminForTesting.setEmail("admin@admin.admin");
        testIssue.setReportingUser(adminForTesting);

        ProjectDTO relatedProjectTest = new ProjectDTO();
        relatedProjectTest.setName("RelatedProjectTest_Test");

        projectDAO.createProject(relatedProjectTest);

        List<ProjectDTO> projectJustCreatedForId = projectDAO.searchProjectsByName("RelatedProjectTest_Test");

        relatedProjectTest.setId(projectJustCreatedForId.get(0).getId());

        testIssue.setRelatedProject(relatedProjectTest);

        issueDAO.reportIssue(testIssue);

        List<IssueDTO> issueJustCreatedForId = issueDAO.searchIssues(testIssue, null, 0, relatedProjectTest.getId());

        if (!issueJustCreatedForId.isEmpty()) {
            testIssue.setId(issueJustCreatedForId.get(0).getId());
            this.testIssueId = testIssue.getId();
            System.out.println("Test Issue created with ID: " + this.testIssueId);
        } else {
            throw new RuntimeException("Impossible found created test issue!");
        }
    }

    @Test
    void testUpdateStatusAssignedOk() throws Exception {

        mockMvc.perform(put("/issues/" + testIssueId + "/resolver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminForTesting)))
                .andExpect(status().isOk());

        IssueController.StatusUpdateRequest request = new IssueController.StatusUpdateRequest(IssueStatusDTO.ASSIGNED);

        mockMvc.perform(put("/issues/" + testIssueId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Status update success"));
        
    }

    @Test
    void testUpdateStatusResolvedOk() throws Exception {

        mockMvc.perform(put("/issues/" + testIssueId + "/resolver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminForTesting)))
                .andExpect(status().isOk());

        IssueController.StatusUpdateRequest statusRequest = new IssueController.StatusUpdateRequest(IssueStatusDTO.RESOLVED);

        mockMvc.perform(put("/issues/" + testIssueId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Status update success"));

    }

    @Test
    void testUpdateStatusResolvedWhenNotAssigned() throws Exception {

        IssueController.StatusUpdateRequest statusRequest = new IssueController.StatusUpdateRequest(IssueStatusDTO.RESOLVED);

        mockMvc.perform(put("/issues/" + testIssueId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Database error"));
    }

    @Test
    void testUpdateStatusWithNull() throws Exception {

        IssueController.StatusUpdateRequest statusRequest = new IssueController.StatusUpdateRequest(null);

        mockMvc.perform(put("/issues/" + testIssueId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Status error: missing"));
    }

    @Test
    void testUpdateStatusOfAResolvedIssue() throws Exception {

        mockMvc.perform(put("/issues/" + testIssueId + "/resolver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminForTesting)))
                .andExpect(status().isOk());

        IssueController.StatusUpdateRequest statusRequest = new IssueController.StatusUpdateRequest(IssueStatusDTO.RESOLVED);

        mockMvc.perform(put("/issues/" + testIssueId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Status update success"));

        statusRequest = new IssueController.StatusUpdateRequest(IssueStatusDTO.ASSIGNED);

        mockMvc.perform(put("/issues/" + testIssueId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Database error"));
    }

    @Test
    void testUpdateStatusInvalidId() throws Exception {

        int invalidIssueId = -1;

        IssueController.StatusUpdateRequest request =
                new IssueController.StatusUpdateRequest(IssueStatusDTO.RESOLVED);

        mockMvc.perform(put("/issues/" + invalidIssueId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Issue not found"));

    }

    @Test
    void testAssignDeveloperEmptyEmail() throws Exception {

        UserDTO invalidResolver = new UserDTO();
        invalidResolver.setEmail("");

        mockMvc.perform(put("/issues/" + testIssueId + "/resolver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidResolver)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Assigning error: missing email"));

    }

    @Test
    void testAssignDeveloperNullEmail() throws Exception {

        UserDTO invalidResolver = new UserDTO();
        invalidResolver.setEmail(null);

        mockMvc.perform(put("/issues/" + testIssueId + "/resolver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidResolver)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Assigning error: missing email"));

    }

    @Test
    void testAssignDeveloperNotExistingEmail() throws Exception {

        UserDTO invalidResolver = new UserDTO();
        invalidResolver.setEmail("a");

        mockMvc.perform(put("/issues/" + testIssueId + "/resolver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidResolver)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with specified email not found or issue does not exist"));

    }

    @Test
    void testAssignDeveloperNotExistingIssue() throws Exception {

        int invalidIssueId = -1;

        mockMvc.perform(put("/issues/" + invalidIssueId + "/resolver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminForTesting)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with specified email not found or issue does not exist"));

    }

    @Test
    void testAssignDeveloperOk() throws Exception {

        UserDTO dev = new UserDTO();
        dev.setEmail("admin@admin.admin");

        mockMvc.perform(put("/issues/" + testIssueId + "/resolver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dev)))
                .andExpect(status().isOk());
    }

    @Test
    void testAssignGuest() throws Exception{

        UserDTO testGuest = new UserDTO();
        testGuest.setEmail("email@prova.test_test");
        testGuest.setPassword("test_test");
        testGuest.setRole(0);

        userDAO.registerNewUser(testGuest);

        try {

            mockMvc.perform(put("/issues/" + testIssueId + "/resolver")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testGuest)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.message").value("Database error"));

        } finally {

            String query = "DELETE FROM User_ WHERE email = ?";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, testGuest.getEmail());
                ps.executeUpdate();
                System.out.println("Deletion complete: Guest user eliminated.");
            }
        }

    }

    @AfterEach
    void tearDown() throws Exception {

        String query = "DELETE FROM Issue WHERE issue_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, testIssueId);
            ps.executeUpdate();
            System.out.println("Deletion complete: Issue " + testIssueId + " eliminated.");
        }
    }
}