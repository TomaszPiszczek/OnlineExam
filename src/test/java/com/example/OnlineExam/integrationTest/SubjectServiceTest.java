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
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
public class SubjectServiceTest {
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresqlContainer.getInstance();
    @Autowired
    UserRepository userRepository;
    @Autowired
    SubjectService subjectService;
    @Autowired
    SubjectRepository subjectRepository;
    @Test
    public void addUserToSubject(){
        //given
        insertUsers();
        //when
        subjectService.addUserToSubject("test1","mathematics");
        User user = userRepository.getUserByUsername("test1").orElseThrow();
        //then
        assertTrue(user.getSubjects().stream()
                .anyMatch(subject -> subject.getSubjectName().equals("mathematics")));
    }
    @Test
    public void addNotExistingUserToSubjectThrowException(){
        //given
        insertUsers();
        //then
        assertThrows(UsernameNotFoundException.class,() -> subjectService.addUserToSubject("notExistingUser","mathematics"));
    }
    @Test
    public void addTwoUsersToSameClassShouldNotAddSecondUser(){
        //given
        insertUsers();
        //when
        subjectService.addUserToSubject("test1","mathematics");
        Subject subject = subjectRepository.getSubjectBySubjectName("mathematics").orElseThrow();
        //then
        assertEquals(1,subject.getUsers().size());
    }

    @Test
    public void removeUserFromSubject(){
        //given
        insertUsers();
        //when
        subjectService.addUserToSubject("test1","mathematics");
        subjectService.removeUserFromSubject("test1","mathematics");

        User user = userRepository.getUserByUsername("test1").orElseThrow();
        //then
        assertFalse(user.getSubjects().stream()
                .anyMatch(subject1 -> subject1.getSubjectName().equals("mathematics")));
    }
    @Test
    public void removeUserFromNotExistingSubjectThrowException(){
        //given
        insertUsers();
        //when
        subjectService.addUserToSubject("test1","mathematics");
        //then
        assertThrows(SubjectNotFoundException.class,() ->  subjectService.removeUserFromSubject("test1","NotExistingSubject"));
    }

    @Test
    public void deleteSubjectShouldRemoveEveryUserFromSubject(){
        //given
        insertUsers();

        subjectService.addUserToSubject("test1","mathematics");
        subjectService.addUserToSubject("test2","mathematics");
        subjectService.addUserToSubject("test3","Geography");
        subjectService.addUserToSubject("test4","Music");

        //when
        subjectService.deleteSubject("mathematics");

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
    @SuppressWarnings("OptionalGetWithoutIsPresent")

    @Test
    public void removeUserFromSubjectShouldNotDeleteSubject(){
        //given
        insertUsers();
        subjectService.addUserToSubject("test1","mathematics");
        //when
        subjectService.removeUserFromSubject("test1","mathematics");
        String response = subjectRepository.getSubjectBySubjectName("mathematics").get().getSubjectName();
        User user = userRepository.getUserByUsername("test1").orElseThrow();
        //then
        assertEquals("mathematics",response);
        assertFalse(user.getSubjects().stream()
                .anyMatch(subject1 -> subject1.getSubjectName().equals("mathematics")));
    }



    private void insertUsers(){
        userRepository.save(new User("test1", "password", true, "name", "surname", "email"));
        userRepository.save(new User("test2", "password", true, "name", "surname", "email"));
        userRepository.save(new User("test3", "password", true, "name", "surname", "email"));
        userRepository.save(new User("test4", "password", true, "name", "surname", "email"));


        userRepository.flush();
        subjectRepository.flush();

    }


}
