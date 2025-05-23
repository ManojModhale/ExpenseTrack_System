package com.expensemanagement.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expensemanagement.backend.model.Role;
import com.expensemanagement.backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmail(String email);
	
    Optional<User> findByUsernameAndRole(String username, Role role); // Added for forgot password
    
    Optional<User> findByUsernameAndEmailAndRole(String username, String email, Role role);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    void deleteByUsername(String username);
    
}
