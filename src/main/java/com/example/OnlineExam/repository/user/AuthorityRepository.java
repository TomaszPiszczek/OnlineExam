package com.example.OnlineExam.repository.user;

import com.example.OnlineExam.model.user.Authority;
import com.example.OnlineExam.model.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Integer> {
    Authority getAuthoritiesByUser(User user);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Authority a WHERE a.user_id = (SELECT user_id FROM public.user  WHERE username = :username)",nativeQuery = true)
    void deleteAuthorityByUsername(@Param("username") String userName);


}
