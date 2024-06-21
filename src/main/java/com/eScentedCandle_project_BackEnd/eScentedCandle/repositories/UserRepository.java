package com.eScentedCandle_project_BackEnd.eScentedCandle.repositories;

import com.eScentedCandle_project_BackEnd.eScentedCandle.models.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u where u.email =  :email AND u.active = true ")
    Optional<User> findByEmail(String email);


    @Query("SELECT COUNT(u) FROM User u WHERE u.email = :email AND u.active = true")
    long countByEmail(String email);

    Long countAllByCreatedAtBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT u FROM User u WHERE (:keyword IS NULL OR u.fullName LIKE %:keyword%) OR (:id IS NULL OR u.id = :id)")
    Page<User> searchUsers(String keyword, Long id, PageRequest pageRequest);

    Page<User> findById(Long id, Pageable unPaged);


    List<User> findByFullNameContainingIgnoreCase(String keyword);


}
