package com.example.OnlineExam.repository.user;

import com.example.OnlineExam.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User getUserByUserId(int id);

    Optional<User> getUserByUsername(String name);
    @Query(value = """
                   SELECT *
                                      FROM public.user
                                      WHERE class_id IN (SELECT class_id FROM public.class WHERE public.class.class = :className);
                    
                    """,nativeQuery = true)
    Optional<Set<User>> getUsersByClassName(@Param("className") String className);

    @Query(value = """
            SELECT u.* 
            FROM public.user u 
            JOIN public.user_subjects us ON u.user_id = us.user_id 
            JOIN public.subject s ON us.subject_id = s.subject_id 
            JOIN public.class c ON u.class_id = c.class_id 
            WHERE s.subject = :subjectName 
            AND c.class = :className 
            """,nativeQuery = true)
    Optional<Set<User>> getUsersBySubjectAndClassName(@Param("className") String className,@Param("subjectName") String subjectName);







}
