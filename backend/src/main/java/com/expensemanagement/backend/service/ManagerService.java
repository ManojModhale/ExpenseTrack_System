package com.expensemanagement.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.expensemanagement.backend.dto.ExpenseDto;
import com.expensemanagement.backend.model.Expense;
import com.expensemanagement.backend.model.Role;
import com.expensemanagement.backend.model.Status;
import com.expensemanagement.backend.model.User;
import com.expensemanagement.backend.repository.ExpenseRepository;

@Service
public class ManagerService {

	@Autowired
	private ExpenseService expenseService;

	@Autowired
	private ExpenseRepository expenseRepository;

	public List<Expense> getExpensesByManager(String username) {

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

	/*
	 * public List<Expense> getAllEmployeeExpenses() { // Implement logic to fetch
	 * all expenses, or expenses for employees under this manager // For now,
	 * assuming you want all. You might need to filter by 'EMPLOYEE' role // return
	 * expenseRepository.findAll(); // This fetches all expenses return
	 * expenseRepository.findByUserRole(Role.EMPLOYEE); // Assuming User entity has
	 * a 'role' field // and Expense has a User reference }
	 */
	public List<ExpenseDto> getAllEmployeeExpenses() {
		try {
			List<Expense> expenses = expenseRepository.findByUserRole(Role.EMPLOYEE);

			// Important: When converting to DTO, you should include the receiptUrl if the
			// frontend needs it.
			// Your current convertToDTO sets receiptUrl to null.
			// Let's modify convertToDTO to include the receiptUrl
			List<ExpenseDto> expenseDtos = expenses.stream().map(this::convertToDTO).collect(Collectors.toList());
			// logger.info("Successfully mapped {} employee expenses to DTOs.",
			// expenseDtos.size());
			System.out.println("Successfully mapped " + expenseDtos.size() + " employee expenses to DTOs.");
			return expenseDtos;
		} catch (Exception e) {
			// logger.error("Error retrieving all employee expenses: {}", e.getMessage(),
			// e);
			// Re-throw as a RuntimeException to be caught by the controller's error
			// handling
			throw new RuntimeException("Failed to fetch employee expenses for review: " + e.getMessage(), e);
		}
	}

	private ExpenseDto convertToDTO(Expense expense) {
		User user = expense.getUser();
		String firstName = (user != null ? user.getFirstName() : null);
		String LastName = (user != null ? user.getLastName() : null);
		String username = (user != null ? user.getUsername() : null);

		return new ExpenseDto(
				expense.getId(), 
				expense.getName(), 
				expense.getDescription(), 
				expense.getCategory(),
				expense.getAmount(), 
				expense.getDate(), 
				expense.getStatus(), 
				null, // expense.getRejectionReason()
				expense.getReceiptUrl(), 
				firstName, LastName, username
				);
	}

	public void approveExpense(Long expenseId) {
		Expense expense = expenseRepository.findById(expenseId)
				.orElseThrow(() -> new RuntimeException("Expense not found with ID: " + expenseId));
		if (expense.getStatus() == Status.APPROVED) {
			throw new IllegalStateException("Expense is already Approved");
		}
		expense.setStatus(Status.APPROVED);
		// expense.setRejectionReason(null); // Clear rejection reason if previously
		// rejected

		// Optional: Set approvedBy and approvedDate if added to Expense model
		// User manager = userRepository.findByUsername(managerUsername).orElse(null);
		// expense.setApprovedBy(managerUser); // Assuming you add 'approvedBy' User
		// field
		// expense.setApprovedDate(LocalDate.now()); // Assuming you add 'approvedDate'
		// LocalDate field

		expenseRepository.save(expense);
		// logger.info("Expense ID {} approved successfully.", expenseId);
	}

	public void rejectExpense(Long expenseId) { // , String reason
		Expense expense = expenseRepository.findById(expenseId)
				.orElseThrow(() -> new RuntimeException("Expense not found with ID: " + expenseId));
		if (expense.getStatus() == Status.REJECTED) {
			throw new IllegalStateException("Expense is already Rejected");
		}
		/*
		 * if (reason == null || reason.trim().isEmpty()) { throw new
		 * IllegalArgumentException("Rejection reason cannot be empty for rejection.");
		 * }
		 */
		// Consider adding a check if the current user is a manager or authorized.
		expense.setStatus(Status.REJECTED);
		// expense.setRejectionReason(reason.trim()); // Add a rejectionReason field to
		// your Expense entity

		// Optional: Set rejectedBy and rejectedDate if added to Expense model
		// User manager = userRepository.findByUsername(managerUsername).orElse(null);
		// expense.setRejectedBy(manager); // Assuming you add 'rejectedBy' User field
		// expense.setRejectionDate(LocalDate.now()); // Assuming you add
		// 'rejectionDate' LocalDate field

		expenseRepository.save(expense);
		// logger.info("Expense ID {} rejected successfully with reason: {}", expenseId,
		// reason);
	}
	
	// NEW: Method to load receipt resource for managers (delegates to ExpenseService)
	public Resource loadReceiptAsResource(String filename) {
		return expenseService.loadReceiptAsResource(filename);
	}
}
