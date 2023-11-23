package com.example.OnlineExam.integrationTest;

import com.example.OnlineExam.exception.SchoolClassNotFoundException;
import com.example.OnlineExam.exception.UsernameNotFoundException;
import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.user.SchoolClassRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.SchoolClassService;
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

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClassServiceTest {
    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2");

    @Autowired
    private  SchoolClassService schoolClassService;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  SchoolClassRepository schoolClassRepository;

    @Test
    public  void connectionTest(){
        assertTrue(postgreSQLContainer.isCreated());
        assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    public void saveUserToClass(){
        //given
        userRepository.save(new User("test1", "password", true, "name", "surname", "email"));
        userRepository.save(new User("test2", "password", true, "name", "surname", "email"));
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("4B");
        schoolClassRepository.save(schoolClass);
        SchoolClass schoolClass1 = new SchoolClass();
        schoolClass1.setName("4A");
        schoolClassRepository.save(schoolClass1);
        //when
        schoolClassService.saveUsersToClass(Set.of("test1"),"4B");
        schoolClassService.saveUsersToClass(Set.of("test1"),"4A");
        User user = userRepository.getUserByUsername("test1").orElseThrow();
        //then
        assertEquals("4A",user.getSchoolClass().getName());

    }
    @Test
    public void removeUserFromClass() {
        //given
        userRepository.save(new User("test3", "password", true, "name", "surname", "email"));


        //when
        schoolClassService.saveUsersToClass(Set.of("test3"),"4B");
        schoolClassService.removeUserFromClass(List.of("test3"));

        User user = userRepository.getUserByUsername("test3").orElseThrow();
        //then
        assertNull(user.getSchoolClass());
    }
    @Test
    public void removeUserFromClassShouldNotDeleteClass() {
        //given
        userRepository.save(new User("test4", "password", true, "name", "surname", "email"));
        SchoolClass schoolClass1 = new SchoolClass();
        schoolClass1.setName("4C");
        schoolClassRepository.save(schoolClass1);
        //when
        schoolClassService.saveUsersToClass(Set.of("test4"),"4C");
        schoolClassService.removeUserFromClass(List.of("test4"));

        User user = userRepository.getUserByUsername("test4").orElseThrow();
        SchoolClass schoolClass = schoolClassRepository.getSchoolClassByName("4C").orElseThrow();
        //then
        assertNull(user.getSchoolClass());
        assertEquals("4C",schoolClass.getName());

    }



    @Test
    public void saveUserToNotExistingClassThrowException() {
        //given
        userRepository.save(new User("test5", "password", true, "name", "surname", "email"));

        //when
        Exception exception = assertThrows(SchoolClassNotFoundException.class,
                () -> schoolClassService.saveUsersToClass(Set.of("test5"), "NotExistingClass"));
        //then
        assertEquals("Class not found",exception.getMessage());
    }
    @Test
    public void saveNullUserToClassShouldThrowException() {
        //given
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("4D");
        schoolClassRepository.save(schoolClass);
        //when
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> schoolClassService.saveUsersToClass(Set.of("notExistingUser"), "4D"));
        //then
        assertEquals("The user with the provided username does not exist",exception.getMessage());
    }



}
