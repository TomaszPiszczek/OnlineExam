package com.example.OnlineExam.repositoryTest;

import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.ClassRepository;
import com.example.OnlineExam.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ClassRepository classRepository;
    @Test
    public void saveUser(){

        SchoolClass schoolClass = classRepository.getSchoolClassByName("4B");
        User user = new User("name","password",true,"name","surname","email");
        user.setSchoolClass(schoolClass);

        userRepository.save(user);

        assertThat(user.getUserId()).isGreaterThan(0);

    }
}
