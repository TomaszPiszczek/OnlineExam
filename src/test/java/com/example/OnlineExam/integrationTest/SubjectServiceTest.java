package com.example.OnlineExam.integrationTest;

import com.example.OnlineExam.db.PostgresqlContainer;
import com.example.OnlineExam.exception.SubjectNotFoundException;
import com.example.OnlineExam.exception.UsernameNotFoundException;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.subject.SubjectRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.SubjectService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubjectServiceTest {
    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SubjectRepository subjectRepository;
    @Test
    public  void connectionTest(){
        assertTrue(postgreSQLContainer.isCreated());
        assertTrue(postgreSQLContainer.isRunning());
    }
    @Test
    public void addUserToSubject(){
        //given
        userRepository.save(new User("test1", "password", true, "name", "surname", "email"));
        //when
        subjectService.addUserToSubject("test1","subject1");
        User user = userRepository.getUserByUsername("test1").orElseThrow();
        //then
        assertTrue(user.getSubjects().stream()
                .anyMatch(subject -> subject.getSubjectName().equals("subject1")));
    }
    @Test
    public void addNotExistingUserToSubjectThrowException(){

        //then
        assertThrows(UsernameNotFoundException.class,() -> subjectService.addUserToSubject("notExistingUser","mathematics"));
    }
    @Test
    public void addTwoSameUsersToSameClassShouldNotAddSecondUser(){
        //given
        userRepository.save(new User("test1", "password", true, "name", "surname", "email"));

        //when
        subjectService.addUserToSubject("test1","subject");
        subjectService.addUserToSubject("test1","subject");
        Subject subject = subjectRepository.getSubjectBySubjectName("subject").orElseThrow();
        //then
        assertEquals(1,subject.getUsers().size());
    }

    @Test
    public void removeUserFromSubject(){
        //given
        userRepository.save(new User("test1", "password", true, "name", "surname", "email"));

        //when
        subjectService.addUserToSubject("test1","subject");
        subjectService.removeUserFromSubject("test1","subject");

        User user = userRepository.getUserByUsername("test1").orElseThrow();
        //then
        assertFalse(user.getSubjects().stream()
                .anyMatch(subject1 -> subject1.getSubjectName().equals("subject")));
    }
    @Test
    public void removeUserFromNotExistingSubjectThrowException(){
        userRepository.save(new User("test1", "password", true, "name", "surname", "email"));

        //given
        subjectService.addUserToSubject("test1","subject");
        //when
        subjectService.addUserToSubject("test1","subject");
        //then
        assertThrows(SubjectNotFoundException.class,() ->  subjectService.removeUserFromSubject("test1","NotExistingSubject"));
    }

    @Test
    public void deleteSubjectShouldRemoveEveryUserFromSubject(){
        //given
        userRepository.save(new User("test1", "password", true, "name", "surname", "email"));
        userRepository.save(new User("test2", "password", true, "name", "surname", "email"));
        userRepository.save(new User("test3", "password", true, "name", "surname", "email"));
        userRepository.save(new User("test4", "password", true, "name", "surname", "email"));


        subjectService.addUserToSubject("test1","subject");
        subjectService.addUserToSubject("test2","subject");
        subjectService.addUserToSubject("test3","subject1");
        subjectService.addUserToSubject("test4","subject2");

        //when
        subjectService.deleteSubject("subject");

        //then
        User user = userRepository.getUserByUsername("test1").orElseThrow();
        User user1 = userRepository.getUserByUsername("test2").orElseThrow();
        User user2 = userRepository.getUserByUsername("test3").orElseThrow();
        User user3 = userRepository.getUserByUsername("test4").orElseThrow();

        assertEquals(0,user.getSubjects().size());
        assertEquals(0,user1.getSubjects().size());
        assertEquals(1,user2.getSubjects().size());
        assertEquals(1,user3.getSubjects().size());
    }
    @Test
    public void addMultipleUsersToSubject(){
        //given
        userRepository.save(new User("test1", "password", true, "name", "surname", "email"));
        userRepository.save(new User("test2", "password", true, "name", "surname", "email"));
        userRepository.save(new User("test3", "password", true, "name", "surname", "email"));
        userRepository.save(new User("test4", "password", true, "name", "surname", "email"));

        Set<String> users = new HashSet<>();
        users.add("test1");
        users.add("test2");
        users.add("test3");
        users.add("test4");
        //when
        subjectService.addUsersToSubject(users,"subject");
        Subject subject = subjectRepository.getSubjectBySubjectName("subject").orElseThrow();
        //
        assertEquals(4,subject.getUsers().size());
    }
    @SuppressWarnings("OptionalGetWithoutIsPresent")

    @Test
    public void removeUserFromSubjectShouldNotDeleteSubject(){
        //given
        userRepository.save(new User("test1", "password", true, "name", "surname", "email"));

        subjectService.addUserToSubject("test1","subject");
        //when
        subjectService.removeUserFromSubject("test1","subject");
        String response = subjectRepository.getSubjectBySubjectName("subject").get().getSubjectName();
        User user = userRepository.getUserByUsername("test1").orElseThrow();
        //then
        assertEquals("subject",response);
        assertFalse(user.getSubjects().stream()
                .anyMatch(subject1 -> subject1.getSubjectName().equals("subject")));
    }


    @Test
    public void createSubjectWithEmptyNameThrowException(){
        //given
        Subject subject = new Subject();
        //when
        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> subjectRepository.save(subject));
        //then
        assertThat(exception.getMessage()).contains("Subject name cannot be blank");

    }







}
