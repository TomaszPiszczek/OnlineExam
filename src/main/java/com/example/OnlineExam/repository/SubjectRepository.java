package com.example.OnlineExam.repository;

import com.example.OnlineExam.model.subject.Subject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject,Integer> {
    Optional<Subject> getSubjectBySubjectName(String name);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_subjects u WHERE u.user_id = (SELECT user_id FROM public.user  WHERE username = :username)",nativeQuery = true)
    void deleteUserFromSubject(@Param("username") String userName);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM subject WHERE subject = :subjectName",nativeQuery = true)
    void deleteSubjectBySubjectName(String subjectName);

    boolean existsSubjectsBySubjectName(String name);

}
