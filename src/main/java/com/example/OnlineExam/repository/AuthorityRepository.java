package com.example.OnlineExam.repository;

import com.example.OnlineExam.model.user.Authority;
import com.example.OnlineExam.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Integer> {
    Authority getAuthoritiesByUser(User user);
}
