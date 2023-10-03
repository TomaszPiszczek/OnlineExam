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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresqlContainer.getInstance();
    @Autowired
    UserService userService;
    @Autowired
    SchoolClassRepository schoolClassRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthorityRepository authorityRepository;



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
