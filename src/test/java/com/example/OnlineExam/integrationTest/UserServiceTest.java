package com.example.OnlineExam.integrationTest;

import com.example.OnlineExam.db.PostgresqlContainer;
import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.user.AuthorityRepository;
import com.example.OnlineExam.repository.user.SchoolClassRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {
    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2");
    @Autowired
    private UserService userService;
    @Autowired
    private SchoolClassRepository schoolClassRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public  void connectionTest(){
        assertTrue(postgreSQLContainer.isCreated());
        assertTrue(postgreSQLContainer.isRunning());
    }


    @Test
    void addUserRole(){
        //given
        insertUsers();
        //when
        userService.addUserRole("test1","TEACHER");
        User user = userRepository.getUserByUsername("test1").orElseThrow();
        //then
        assertTrue(user.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_TEACHER")));
    }
    @Test
    void removeUserRole(){
        //given
        insertUsers();
        //when
        userService.removeAllUserRole("test2");
        User user = userRepository.getUserByUsername("test2").orElseThrow();

        //then
        assertFalse(user.getRoles().stream().anyMatch(role -> role.getAuthority().equals("ROLE_TEACHER")));
    }
    @Test
    void addUserWithoutValidCredentialsShouldThrowException(){
        //given
        User user = new User("","",true,"","","");
        //when
        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> userService.saveUser(user));

        assertThat(exception.getMessage()).contains("Username","Password","Name","Surname","Email");
    }
    @Test
    void addTwoUserWithSameNameShouldThrowException() {
        //given
        User user = new User("name1", "password", true, "name", "surname", "email");
        User user1 = new User("name1", "password", true, "name", "surname", "email");
        //then
        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.saveUser(user1);
            userService.saveUser(user);
        });
    }
    private void insertUsers(){
        userRepository.save(new User("test1", "password", true, "name", "surname", "email"));
        userRepository.save(new User("test2", "password", true, "name", "surname", "email"));
        userService.addUserRole("test2","TEACHER");

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("4B");
        schoolClassRepository.save(schoolClass);

        userRepository.flush();
        authorityRepository.flush();
        schoolClassRepository.flush();
    }



}
