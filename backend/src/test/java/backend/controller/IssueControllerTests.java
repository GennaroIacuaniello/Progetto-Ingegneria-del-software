package backend.controller;

import backend.database.dao.IssueDAO;
import backend.database.dao.ProjectDAO;
import backend.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc // Configures MockMvc for HTTP requests
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //@BeforeAll not static
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
    private DataSource dataSource;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private int testIssueId;

    @BeforeAll
    void setup() throws Exception {

        IssueDTO testIssue = new IssueDTO();
        testIssue.setTitle("TestIssue_Test");
        testIssue.setDescription("Temporary issue for testing.");
        testIssue.setStatus(IssueStatusDTO.TODO);
        testIssue.setType(IssueTypeDTO.BUG);
        testIssue.setPriority(1);

        testIssue.setReportDate(Date.from(Instant.now()));

        UserDTO reporter = new UserDTO();
        reporter.setId(0);
        testIssue.setReportingUser(reporter);

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
    void testUpdateStatus_RealDB() throws Exception {

        IssueController.StatusUpdateRequest request = new IssueController.StatusUpdateRequest(IssueStatusDTO.ASSIGNED);

        mockMvc.perform(put("/issues/" + testIssueId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Status update success"));
    }

    @Test
    void testAssignDeveloper_RealDB() throws Exception {

        UserDTO dev = new UserDTO();
        dev.setEmail("admin@admin.admin");

        mockMvc.perform(put("/issues/" + testIssueId + "/resolver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dev)))
                .andExpect(status().isOk());
    }

    @AfterAll
    void tearDown() throws Exception {

        String sql = "DELETE FROM Issue WHERE issue_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, testIssueId);
            ps.executeUpdate();
            System.out.println("Deletion complete: Issue " + testIssueId + " eliminated.");
        }
    }
}