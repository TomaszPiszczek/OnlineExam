package com.example.OnlineExam.service;

import com.example.OnlineExam.exception.UsernameNotFoundException;
import com.example.OnlineExam.model.user.Authority;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.AuthorityRepository;
import com.example.OnlineExam.repository.SchoolClassRepository;
import com.example.OnlineExam.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Transactional
    public void addUserRole(String userName, String roleName) {
        User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);

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
    @Transactional
    public void removeUserRole(String userName){
        User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);

        authorityRepository.deleteAuthorityByUsername(userName);
        user.setRoles(new ArrayList<>());

    }


    @Transactional
    public void saveUser(User user){
        userRepository.save(user);
    }



}
