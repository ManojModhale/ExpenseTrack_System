package com.expensemanagement.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.expensemanagement.backend.model.Expense;


@Service
public class EmployeeService {
	
	@Autowired
	private ExpenseService expenseService;
	
	public List<Expense> getExpensesByEmployee(String username) {
		
        return expenseService.getExpensesByUser(username);
    }

    public Expense addExpense(String username, Expense expense, MultipartFile receiptFile) {
    	return expenseService.addExpense(username, expense, receiptFile);
    }
    
    public boolean deleteExpense(String username, Long id) {
    	return expenseService.deleteExpense(username, id);
    }
    
    public Expense updateExpense(String username, Expense updatedExpense, MultipartFile receiptFile) {
    	return expenseService.updateExpense(username, updatedExpense, receiptFile);
    }
    
    // Delegate updateExpenseName to ExpenseService
 	public boolean updateExpenseName(Long id, String name) {
 		return expenseService.updateExpenseName(id, name);
 	}

 	// NEW: Method to load receipt resource for employees (delegates to ExpenseService)
 	public Resource loadReceiptAsResource(String filename) {
 		return expenseService.loadReceiptAsResource(filename);
 	}
}
