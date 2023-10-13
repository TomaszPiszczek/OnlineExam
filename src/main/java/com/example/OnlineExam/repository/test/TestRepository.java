package com.example.OnlineExam.repository.test;

import com.example.OnlineExam.model.test.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository
public interface TestRepository extends JpaRepository<Test,Integer> {
    Optional<Test> getTestById(int testId);
    //Optional<List<Test>> getTestsByUser(User user);
    @Query(value = """
            SELECT t.*
            FROM public.test t
            INNER JOIN public.student_test st ON t.test_id = st.test_id
            INNER JOIN public."user" u ON st.user_id = u.user_id
            WHERE u.username = :userName""",nativeQuery = true)
    Optional<List<Test>> getTestsByUserName(String userName);

    Optional<Test> getTestByTestName(String testName);

    Optional<List<Test>> getTestsByTestCreator(String testCreator);



}
