package com.example.OnlineExam.repository;

import com.example.OnlineExam.model.user.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass,Integer> {
    boolean existsByName(String name);
    Optional<SchoolClass> getSchoolClassByName(String name);


}
