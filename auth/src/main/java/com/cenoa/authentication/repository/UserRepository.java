package com.cenoa.authentication.repository;

import com.cenoa.authentication.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);
  boolean existsUserByEmail(String email);

  @Modifying
  @Transactional
  @Query(value = "UPDATE _user SET balance = balance + ?1 WHERE id = ?2", nativeQuery = true)
  void deposit(double amount, int id);

  @Modifying
  @Transactional
  @Query(value = "UPDATE _user SET balance = balance - ?1 WHERE id = ?2", nativeQuery = true)
  void withdraw(double amount, int id);

}
