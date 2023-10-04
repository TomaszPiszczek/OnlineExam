package com.example.OnlineExam.service;

import com.example.OnlineExam.exception.SubjectNotFoundException;
import com.example.OnlineExam.exception.UsernameNotFoundException;
import com.example.OnlineExam.model.subject.Subject;
import com.example.OnlineExam.model.user.User;
import com.example.OnlineExam.repository.subject.SubjectRepository;
import com.example.OnlineExam.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SubjectService {
    UserRepository userRepository;
    SubjectRepository subjectRepository;
    EntityManager entityManager;

    public SubjectService(UserRepository userRepository, SubjectRepository subjectRepository, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public void addUserToSubject(String userName, String subjectName) {
        User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);
        Subject subject = subjectRepository.getSubjectBySubjectName(subjectName).orElse(new Subject());

        subject.setSubjectName(subjectName);

        if(!user.getSubjects().contains(subject)){
            user.addSubject(subject);
        }
    }

    @Transactional
    public void addUsersToSubject(List<String> userNames, String subjectName){
        Subject subject = subjectRepository.getSubjectBySubjectName(subjectName).orElse(new Subject());
        subject.setSubjectName(subjectName);
        for (String name:userNames
             ) {
            User user = userRepository.getUserByUsername(name).orElseThrow(UsernameNotFoundException::new);
            if(!user.getSubjects().contains(subject)){
                user.addSubject(subject);
            }
        }
    }


    @Transactional
    @SuppressWarnings("unused")
    public void removeUserFromSubject(String userName, String subjectName) {
        User user = userRepository.getUserByUsername(userName).orElseThrow(UsernameNotFoundException::new);
        Subject subject = subjectRepository.getSubjectBySubjectName(subjectName).orElseThrow(SubjectNotFoundException::new);

        user.removeSubject(subject);
    }
    @Transactional
    public void deleteSubject(String subjectName){
        Subject subject = subjectRepository.getSubjectBySubjectName(subjectName).orElseThrow(SubjectNotFoundException::new);
        Set<User> copy = new HashSet<>(subject.getUsers());
        for(User user: copy){
            user.removeSubject(subject);
            entityManager.merge(user);
        }
    }

}
