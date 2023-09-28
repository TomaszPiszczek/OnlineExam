package com.example.OnlineExam.integrationTest;

import com.example.OnlineExam.db.PostgresqlContainer;
import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.user.AuthorityRepository;
import com.example.OnlineExam.repository.user.SchoolClassRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import com.example.OnlineExam.service.ClassService;
import com.example.OnlineExam.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
public class QuestionServiceTest {
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
    @Autowired
    ClassService classService;

   /* @Test
    public void adas(){
        insertUsers();
        classService.saveUserToClass("test1","4B");
        classService.saveUserToClass("test2","4B");

        SchoolClass schoolClass = schoolClassRepository.getSchoolClassByName("4B").orElseThrow();
        classService.getAllStudent("4B");




    }

    private void insertUsers(){
        userRepository.save(new User("test1", "password", true, "name", "surname", "email"));
        userRepository.save(new User("test2", "password", true, "name", "surname", "email"));
        userService.addUserRole("test2","ROLE_TEACHER");

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("4B");
        schoolClassRepository.save(schoolClass);

        userRepository.flush();
        authorityRepository.flush();
        schoolClassRepository.flush();
    }*/
}
