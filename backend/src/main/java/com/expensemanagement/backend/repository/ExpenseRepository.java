package com.expensemanagement.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expensemanagement.backend.model.Expense;
import com.expensemanagement.backend.model.Role;
import com.expensemanagement.backend.model.User;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	List<Expense> findByUser(User user);
	
	List<Expense> findByUserId(Long userId); // Assuming User ID links to expenses
	
    Optional<Expense> findByIdAndUserId(Long id, Long userId); // To find an expense by ID belonging to a specific user
    
    Expense findByIdAndUser(Long id, User employee);

	List<Expense> findByUserRole(Role role); // to find an expense by User with Role
}
