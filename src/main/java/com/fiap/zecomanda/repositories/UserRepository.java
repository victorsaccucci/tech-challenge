package com.fiap.zecomanda.repositories;

import com.fiap.zecomanda.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
    Page<User> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = :#{#usuario.name}, " +
            "u.email = :#{#usuario.email} WHERE u.id = :id")
    Integer update(User user, Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM User WHERE id = ?1")
    Integer delete(Long id);

}
