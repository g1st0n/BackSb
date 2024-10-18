package com.example.gestox.dao;

import com.example.gestox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE lower(u.firstName) = lower(:firstName) AND u.enabled = true")
    Optional<User> findByFirstName(@Param("firstName") String firstName);

    @Query("SELECT u FROM User u WHERE lower(u.address) = lower(:email) AND u.enabled = true")
    Optional<User> findByEmailAddress(@Param("email") String email);

}
