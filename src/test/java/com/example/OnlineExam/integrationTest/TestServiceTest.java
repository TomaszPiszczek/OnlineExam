package com.example.OnlineExam.integrationTest;

import com.example.OnlineExam.db.PostgresqlContainer;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.user.User;

import com.example.OnlineExam.repository.subject.SubjectRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;

@AutoConfigureMockMvc
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestServiceTest {
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresqlContainer.getInstance();
    @Autowired
    UserRepository userRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testThrowUserNotFoundExceptionWithWrongUsernameJSON() throws Exception {

        //given
        insertUsers();
        Subject subject = new Subject();
        subject.setSubjectName("math");
        subjectRepository.save(subject);

        String json = "{\"testName\":\"Test Math\",\"user\":{\"username\":\"wrong\"},\"subject\":{\"id\":1},\"questions\":[{\"question\":\"2 + 2?\",\"answers\":[{\"answer\":\"3\",\"correct\":false},{\"answer\":\"4\",\"correct\":true},{\"answer\":\"5\",\"correct\":false},{\"answer\":\"6\",\"correct\":false}]},{\"question\":\"3 - 2?\",\"answers\":[{\"answer\":\"0\",\"correct\":false},{\"answer\":\"1\",\"correct\":true},{\"answer\":\"2\",\"correct\":false},{\"answer\":\"3\",\"correct\":false}]}]}";

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/createTest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
    @Test
    public void testThrowSubjectNotFoundExceptionWithWrongSubjectIdJSON() throws Exception {
        //given
        insertUsers();
        Subject subject = new Subject();
        subject.setSubjectName("math");
        subjectRepository.save(subject);

        String json = "{\"testName\":\"Test Math\",\"user\":{\"username\":\"test\"},\"subject\":{\"id\":1421},\"questions\":[{\"question\":\"2 + 2?\",\"answers\":[{\"answer\":\"3\",\"correct\":false},{\"answer\":\"4\",\"correct\":true},{\"answer\":\"5\",\"correct\":false},{\"answer\":\"6\",\"correct\":false}]},{\"question\":\"3 - 2?\",\"answers\":[{\"answer\":\"0\",\"correct\":false},{\"answer\":\"1\",\"correct\":true},{\"answer\":\"2\",\"correct\":false},{\"answer\":\"3\",\"correct\":false}]}]}";
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/createTest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
    @Test
    public void testCreateTestWithValidJSON() throws Exception {
        //given
        insertUsers();
        Subject subject = new Subject();
        subject.setSubjectName("math");
        subjectRepository.save(subject);
        Subject subject1 = subjectRepository.getSubjectBySubjectName("math").orElseThrow();
        

        String json = "{\"testName\":\"Test Math\",\"user\":{\"username\":\"test\"},\"subject\":{\"id\":" + subject1.getId() + "},\"questions\":[{\"question\":\"2 + 2?\",\"answers\":[{\"answer\":\"3\",\"correct\":false},{\"answer\":\"4\",\"correct\":true},{\"answer\":\"5\",\"correct\":false},{\"answer\":\"6\",\"correct\":false}]},{\"question\":\"3 - 2?\",\"answers\":[{\"answer\":\"0\",\"correct\":false},{\"answer\":\"1\",\"correct\":true},{\"answer\":\"2\",\"correct\":false},{\"answer\":\"3\",\"correct\":false}]}]}";
       //then
        mockMvc.perform(MockMvcRequestBuilders.post("/createTest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void insertUsers(){
        subjectRepository.flush();

        userRepository.save(new User("test", "password", true, "name", "surname", "email"));

    }



}
