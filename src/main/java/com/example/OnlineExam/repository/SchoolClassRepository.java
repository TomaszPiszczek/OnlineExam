package com.example.OnlineExam.repository;

import com.example.OnlineExam.model.user.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass,Integer> {
    boolean existsByName(String name);
    SchoolClass getSchoolClassByName(String name);
}
