package com.example.OnlineExam.repository.test;

import com.example.OnlineExam.model.test.StudentTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTestRepository extends JpaRepository<StudentTest,Integer> {
    @Query("SELECT st.testResult FROM StudentTest st WHERE st.test.id = :testId AND st.user.userId = :userId")
    Integer findTestScoreByTestIdAndUserId(@Param("testId") int testId, @Param("userId") int userId);


}
