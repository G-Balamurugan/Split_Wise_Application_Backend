package com.example.group_services.dao.api;

import com.example.group_services.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Autowired
    List<User> findAll();
    User findByUserId(long userId);
    @Query("SELECT user FROM User user WHERE lower(replace(user.userName, ' ', '')) LIKE %:normalizedUserName%")
    List<User> findByUserNameIgnoreCaseAndNormalized(@Param("normalizedUserName") String normalizedUserName);

}
