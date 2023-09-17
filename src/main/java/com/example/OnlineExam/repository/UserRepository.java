package com.example.OnlineExam.repository;

import com.example.OnlineExam.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User getUserByUserId(int id);
    Optional<User> getUserByName(String name);

}
