package com.example.OnlineExam.service;

import com.example.OnlineExam.exception.UsernameNotFoundException;
import com.example.OnlineExam.model.user.Authority;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.user.AuthorityRepository;
import com.example.OnlineExam.repository.user.SchoolClassRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

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


        if(user.getRoles().stream()
                .noneMatch(role -> role.getAuthority().equals(roleName))){

            Authority authority = new Authority();
            authority.setAuthority("ROLE_"+roleName);
            authorityRepository.save(authority);
            user.addRole(authority);
        }
    }
    @Transactional
    public void removeAllUserRole(String userName){
        User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);

        authorityRepository.deleteAuthorityByUsername(userName);
        user.setRoles(new HashSet<>());

    }


    @Transactional
    public void saveUser(User user){
        userRepository.save(user);
    }



}
