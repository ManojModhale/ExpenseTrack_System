package com.expensemanagement.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.expensemanagement.backend.model.Expense;
import com.expensemanagement.backend.model.Status;
import com.expensemanagement.backend.model.User;
import com.expensemanagement.backend.repository.ExpenseRepository;
import com.expensemanagement.backend.repository.UserRepository;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FileStorageService fileStorageService;

	
	public List<Expense> getExpensesByUser(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("Invalid username"));

		return expenseRepository.findByUser(user);
	}

	public Expense addExpense(String username, Expense expense, MultipartFile receiptFile) {
		if (expense.getDate() == null) {
			expense.setDate(LocalDate.now()); // Set the current date as the expense date
		}
		expense.setStatus(Status.PENDING);

		Optional<User> userOptional = userRepository.findByUsername(username);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			expense.setUser(user);

			// Handle file upload and store the URL
			if (receiptFile != null && !receiptFile.isEmpty()) {
				String uniqueFileName = fileStorageService.storeFile(receiptFile);
				// Store the full URL to the file in the database
				expense.setReceiptUrl(fileStorageService.getFileUrl(uniqueFileName));
			} else {
				expense.setReceiptUrl(null); // No receipt provided
			}

			return expenseRepository.save(expense);
		}
		throw new RuntimeException("User not found with Username: " + username);
	}

	public boolean deleteExpense(String username, Long id) {
		Optional<User> userOptional = userRepository.findByUsername(username);
		if (userOptional.isEmpty()) {
			return false;
		}
		User user = userOptional.get();
		Expense expense = expenseRepository.findByIdAndUser(id, user);
		if (expense == null) {
			return false;
		}

		// Delete the associated file using its unique filename extracted from the URL
		if (expense.getReceiptUrl() != null && !expense.getReceiptUrl().isEmpty()) {
			// Extract the unique filename from the full URL for deletion
			String fileNameToDel = extractFileNameFromUrl(expense.getReceiptUrl());
			fileStorageService.deleteFile(fileNameToDel);
		}

		expenseRepository.delete(expense);
		return true;
	}

	public Expense updateExpense(String username, Expense updatedExpense, MultipartFile receiptFile) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("Invalid username"));

		Expense expense = expenseRepository.findByIdAndUser(updatedExpense.getId(), user);
		if (expense == null) {
			throw new IllegalArgumentException("Expense not found for this employee: " + updatedExpense.getId());
		}
		expense.setName(updatedExpense.getName());
		expense.setDescription(updatedExpense.getDescription());
		expense.setAmount(updatedExpense.getAmount());
		expense.setCategory(updatedExpense.getCategory());
		expense.setDate(updatedExpense.getDate());
		expense.setStatus(Status.PENDING); // Reset status on update

		// Handle receipt file update logic
		if (receiptFile != null && !receiptFile.isEmpty()) {
			// If a new file is uploaded, delete the old one first (if exists)
			if (expense.getReceiptUrl() != null && !expense.getReceiptUrl().isEmpty()) {
				String fileNameToDel = extractFileNameFromUrl(expense.getReceiptUrl());
				fileStorageService.deleteFile(fileNameToDel);
			}
			String newUniqueFileName = fileStorageService.storeFile(receiptFile);
			expense.setReceiptUrl(fileStorageService.getFileUrl(newUniqueFileName));
		}
		// ELSE: If no new file is provided (receiptFile is null or empty), keep the
		// existing receiptUrl.
		// If frontend has a "remove receipt" feature, it would explicitly send a flag,
		// and you'd set existingExpense.setReceiptUrl(null); here.

		return expenseRepository.save(expense);
	}

	public boolean updateExpenseName(Long id, String name) {
		Optional<Expense> expenseOptional = expenseRepository.findById(id);
		if (expenseOptional.isEmpty()) {
			return false;
		}
		Expense expense = expenseOptional.get();
		expense.setName(name);
		expenseRepository.save(expense);
		return true;
	}

	public Optional<Expense> getExpenseByIdAndUser(Long expenseId, String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("Invalid username"));
		return Optional.ofNullable(expenseRepository.findByIdAndUser(expenseId, user));
	}

	// This method is now only for the *serving* part of the controller,
	// it loads the file resource based on the *filename* from the URL.
	public Resource loadReceiptAsResource(String filename) {
		return fileStorageService.loadFileAsResource(filename);
	}

	// Helper method to extract the unique filename from the full URL
	// This is crucial for deleting files correctly.
	private String extractFileNameFromUrl(String fullUrl) {
		if (fullUrl == null || fullUrl.isEmpty()) {
			return null;
		}
		// Assuming URL format is {baseUrl}/api/expenses/receipts/{filename}
		int lastSlashIndex = fullUrl.lastIndexOf('/');
		if (lastSlashIndex != -1 && lastSlashIndex < fullUrl.length() - 1) {
			return fullUrl.substring(lastSlashIndex + 1);
		}
		return fullUrl; // Fallback: if no slash found, treat whole thing as filename (unlikely for
						// proper URLs)
	}
}
