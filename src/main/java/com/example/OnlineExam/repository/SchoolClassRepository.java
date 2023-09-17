package com.example.OnlineExam.repository;

import com.example.OnlineExam.model.user.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass,Integer> {
    boolean existsByName(String name);
    Optional<SchoolClass> getSchoolClassByName(String name); // TODO: 16.09.2023 OPTIONAL + EXCEPTION HANDLING  https://stackoverflow.com/questions/43888003/return-404-for-every-null-response
}
