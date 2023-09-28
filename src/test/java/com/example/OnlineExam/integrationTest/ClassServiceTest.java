package com.example.OnlineExam.integrationTest;

import com.example.OnlineExam.db.PostgresqlContainer;
import com.example.OnlineExam.exception.SchoolClassNotFoundException;
import com.example.OnlineExam.exception.UsernameNotFoundException;
import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.user.SchoolClassRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.ClassService;
import jakarta.transaction.Transactional;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ClassServiceTest {
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresqlContainer.getInstance();

    @Autowired
    ClassService classService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SchoolClassRepository schoolClassRepository;

    @Test
    public void saveUserToClass(){
        //given
        insertUsers();
        //when
        classService.saveUserToClass("test1","4B");
        classService.saveUserToClass("test1","4A");
        User user = userRepository.getUserByUsername("test1").orElseThrow();
        //then
        assertEquals("4A",user.getSchoolClass().getName());
    }
    @Test
    public void removeUserFromClass() {
        //given
        insertUsers();
        //when
        classService.saveUserToClass("test1","4B");
        classService.removeUserFromClass("test1");

        User user = userRepository.getUserByUsername("test1").orElseThrow();
        //then
        assertNull(user.getSchoolClass());
    }
    @Test
    public void removeUserFromClassShouldNotDeleteClass() {
        //given
        insertUsers();
        //when
        classService.saveUserToClass("test1","4B");
        classService.removeUserFromClass("test1");

        User user = userRepository.getUserByUsername("test1").orElseThrow();
        SchoolClass schoolClass = schoolClassRepository.getSchoolClassByName("4B").orElseThrow();
        //then
        assertNull(user.getSchoolClass());
        assertEquals("4B",schoolClass.getName());

    }



    @Test
    public void saveUserToNotExistingClassThrowException() {
        //given
        insertUsers();
        //when
        Exception exception = assertThrows(SchoolClassNotFoundException.class,
                () -> classService.saveUserToClass("test1", "NotExistingClass"));
        //then
        assertEquals("Class not found",exception.getMessage());
    }
    @Test
    public void saveNullUserToClassShouldThrowException() {
        //given
        insertUsers();
        //when
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> classService.saveUserToClass("notExistingUser", "4B"));
        //then
        assertEquals("The user with the provided username does not exist",exception.getMessage());
    }
    private void insertUsers(){
        userRepository.save(new User("test1", "password", true, "name", "surname", "email"));
        userRepository.save(new User("test2", "password", true, "name", "surname", "email"));
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("4B");
        schoolClassRepository.save(schoolClass);

        SchoolClass schoolClass1 = new SchoolClass();
        schoolClass1.setName("4A");
        schoolClassRepository.save(schoolClass1);

        userRepository.flush();
    }


}
