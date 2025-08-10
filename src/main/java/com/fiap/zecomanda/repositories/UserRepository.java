package com.fiap.zecomanda.repositories;

import com.fiap.zecomanda.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    // HARD DELETE explícito para cliente apenas (retorna nº de linhas afetadas)
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id = :id")
    int hardDeleteById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = ?1, u.email = ?2, u.phoneNumber = ?3, u.login = ?4 WHERE u.id = ?5")
    Integer updateUser(String name, String email, String phoneNumber, String login, Long id);
}
