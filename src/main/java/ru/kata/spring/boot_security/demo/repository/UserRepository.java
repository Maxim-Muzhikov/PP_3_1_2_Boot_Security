package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByEmail(String email);
	
	@Query("SELECT u FROM User u JOIN FETCH u.roles WHERE :role MEMBER OF u.roles")
	List<User> findByRole(@Param("role") Role role);
	
	@Query("SELECT u FROM User u JOIN FETCH u.roles r WHERE r.name = :roleName")
	List<User> findByRoleName(@Param("roleName") String roleName);
	
}