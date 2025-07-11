package com.fiap.zecomanda.repository;

import com.fiap.zecomanda.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByLogin(String login);

    Optional<Usuario> findByEmail(String email);

}
