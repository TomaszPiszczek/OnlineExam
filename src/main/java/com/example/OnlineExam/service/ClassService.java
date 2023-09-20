package com.example.OnlineExam.service;

import com.example.OnlineExam.exception.SchoolClassNotFoundException;
import com.example.OnlineExam.exception.UsernameNotFoundException;
import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.SchoolClassRepository;
import com.example.OnlineExam.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ClassService {
    UserRepository userRepository;
    SchoolClassRepository schoolClassRepository;

    public ClassService(UserRepository userRepository, SchoolClassRepository schoolClassRepository) {
        this.userRepository = userRepository;
        this.schoolClassRepository = schoolClassRepository;
    }
    @Transactional
    public void saveUserToClass(String userName,String schoolClassName){
        User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);

        SchoolClass schoolClass1 =  schoolClassRepository.getSchoolClassByName(schoolClassName).orElseThrow(SchoolClassNotFoundException::new);

        user.setSchoolClass(schoolClass1);
    }
    @Transactional
    public void removeUserFromClass(String userName){
        User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);

        user.setSchoolClass(null);
    }


}
