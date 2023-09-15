package com.example.OnlineExam.service;

import com.example.OnlineExam.model.user.Authority;
import com.example.OnlineExam.model.user.SchoolClass;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.AuthorityRepository;
import com.example.OnlineExam.repository.SchoolClassRepository;
import com.example.OnlineExam.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class UserService {
    UserRepository userRepository;
    SchoolClassRepository schoolClassRepository;
    AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, SchoolClassRepository schoolClassRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.authorityRepository = authorityRepository;
    }

    public void addUserRole(User user, String roleName) {
        List<Authority> userRoles = user.getRoles();

        if(userRoles.stream()
                .noneMatch(role -> role.getAuthority().equals(roleName))){

            Authority authority = new Authority();
            authority.setAuthority(roleName);
            authority.setUser(user);
            authorityRepository.save(authority);
            userRoles.add(authority);
            user.setRoles(userRoles);
        }

    }
    public void saveUserToClass(User user, SchoolClass schoolClass){
        try{
            schoolClassRepository.getSchoolClassByName(schoolClass.getName());
        }catch (NullPointerException ex){
            throw new NullPointerException("Class not found");
        }

        user.setSchoolClass(schoolClass);
    }
}
