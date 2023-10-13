package com.example.OnlineExam.service;

import com.example.OnlineExam.dto.user.UserDTO;
import com.example.OnlineExam.exception.SchoolClassNotFoundException;
import com.example.OnlineExam.exception.UsernameNotFoundException;
import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.user.SchoolClassRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@Service
public class SchoolClassService {
    private final UserRepository userRepository;
    private final SchoolClassRepository schoolClassRepository;

    public SchoolClassService(UserRepository userRepository, SchoolClassRepository schoolClassRepository) {
        this.userRepository = userRepository;
        this.schoolClassRepository = schoolClassRepository;
    }
    @Transactional
    public void saveUsersToClass(Set<String> users, String schoolClassName){
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
            throw new IllegalStateException("Class already exist");
        }
        schoolClassRepository.save(schoolClass);
    }
    //todo test this method
    public Set<UserDTO> getUsersFromClass(String className) {
        Set<User> users = userRepository.getUsersByClassName(className).orElseThrow(SchoolClassNotFoundException::new);
        log.warn(users.size() + "Users size");
       users.forEach(user -> log.warn(user.getUsername()));

        Set<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user.getUserId(), user.getUsername(), user.isEnabled(), user.getName(), user.getSurname()))
                .collect(Collectors.toSet());

        userDTOs.forEach(user1 -> log.warn(user1.name()));

        return userDTOs;
    }




}
