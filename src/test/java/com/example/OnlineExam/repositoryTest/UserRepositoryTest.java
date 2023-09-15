package com.example.OnlineExam.repositoryTest;

import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.SchoolClassRepository;
import com.example.OnlineExam.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SchoolClassRepository schoolClassRepository;
    @Test
    public void saveUser(){

        SchoolClass schoolClass = schoolClassRepository.getSchoolClassByName("4B");
        schoolClassRepository.save(schoolClass);

        User user = new User("name", "password", true, "name", "surname", "email");
        user.setSchoolClass(schoolClass);

        userRepository.save(user);


        User savedUser = userRepository.findById(user.getUserId()).orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("name");
        assertThat(savedUser.getSchoolClass()).isEqualTo(schoolClass);
    }


}

