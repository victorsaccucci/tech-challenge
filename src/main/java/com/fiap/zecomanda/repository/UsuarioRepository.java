package com.fiap.zecomanda.repository;

import com.fiap.zecomanda.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByLogin(String login);

    Optional<Usuario> findByEmail(String email);

    @Query(value = "SELECT * FROM usuario where login = ?", nativeQuery = true)
    Usuario encontrarUsuarioPorLogin(String login);

    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.nome = :#{#usuario.nome}, " +
            "u.email = :#{#usuario.email} WHERE u.id = :id")
    Integer update(Usuario usuario, Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Usuario u WHERE u.id = :id")
    Integer delete(Long id);

}
