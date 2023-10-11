package com.example.OnlineExam.service;

import com.example.OnlineExam.exception.SchoolClassNotFoundException;
import com.example.OnlineExam.exception.UsernameNotFoundException;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.user.SchoolClassRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {
    UserRepository userRepository;
    SchoolClassRepository schoolClassRepository;

    public ClassService(UserRepository userRepository, SchoolClassRepository schoolClassRepository) {
        this.userRepository = userRepository;
        this.schoolClassRepository = schoolClassRepository;
    }
    @Transactional
    public void saveUsersToClass(List<String> users, String schoolClassName){
        SchoolClass schoolClass1 =  schoolClassRepository.getSchoolClassByName(schoolClassName).orElseThrow(SchoolClassNotFoundException::new);
        for (String userName : users
             ) {
            User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);
            user.setSchoolClass(schoolClass1);
        }


    }
    @Transactional
    public void removeUserFromClass(List<String> users){
        for (String userName : users
        ) {
            User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);
            user.setSchoolClass(null);
        }
    }

    @Transactional
    public void createClass(SchoolClass schoolClass){
        if(schoolClassRepository.existsByName(schoolClass.getName())){
            throw new IllegalStateException("Subject already exist");
        }
        schoolClassRepository.save(schoolClass);
    }



}
