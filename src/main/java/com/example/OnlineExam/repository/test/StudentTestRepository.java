package com.example.OnlineExam.repository.test;

import com.example.OnlineExam.model.test.StudentTest;
import com.example.OnlineExam.model.test.Test;
import com.example.OnlineExam.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTestRepository extends JpaRepository<StudentTest,Integer> {
    @Query("SELECT st.testResult FROM StudentTest st WHERE st.test.id = :testId AND st.user.userId = :userId")
    Integer findTestScoreByTestIdAndUserId(@Param("testId") int testId, @Param("userId") int userId);

    @Query("SELECT st.finished FROM StudentTest st WHERE st.test.id = :testId AND st.user.userId = :userId")
    Boolean isFinishedTestByTestIdAndUserId(@Param("testId") int testId, @Param("userId") int userId);
    @Query(value = "SELECT st FROM StudentTest st WHERE st.user.userId = :userId AND st.test.id = :testId")
    StudentTest findStudentTestByTestAndUser(Integer userId, Integer testId);




}
