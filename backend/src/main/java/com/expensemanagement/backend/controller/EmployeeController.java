package com.expensemanagement.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.expensemanagement.backend.exception.FileStorageException;
import com.expensemanagement.backend.exception.MyFileNotFoundException;
import com.expensemanagement.backend.model.Expense;
import com.expensemanagement.backend.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	// TODO: Replace with actual Spring Security integration to get the
	// authenticated username
	private String getCurrentUsername() {
		return "testuser"; // Placeholder for demonstration
	}

	@GetMapping("/expensesByUsername/{username}")
	public ResponseEntity<List<Expense>> getExpensesByEmployee(@PathVariable String username) {
		List<Expense> expenses = employeeService.getExpensesByEmployee(username);
		/*
		 * Iterator<Expense> iterator = expenses.iterator(); while (iterator.hasNext())
		 * { System.out.println(iterator.next()); }
		 */
		return ResponseEntity.ok(expenses);
	}

	@PostMapping(value = "/addexpense/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Expense> addExpense(
			@PathVariable String username, 
			@RequestPart("expense") Expense expense, // Directly bind to Expense object
			@RequestPart(value = "receipt", required = false) MultipartFile receiptFile) { // Optional receipt file
		try {
			Expense newExpense=employeeService.addExpense(username, expense, receiptFile);
			return new ResponseEntity<>(newExpense, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			// Catch specific service-level exceptions (e.g., user not found)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (FileStorageException e) {
			// Catch file storage specific errors
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		} catch (Exception e) { // Catch any other unexpected exceptions
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/delete-expense/{username}/{id}")
	public ResponseEntity<Map<String, Object>> deleteExpense(@PathVariable String username, @PathVariable Long id) {
		boolean isDeleted = employeeService.deleteExpense(username, id);
		Map<String, Object> response = new HashMap<>();
		if (isDeleted) {
			response.put("message", "Expense deleted successfully");
			return ResponseEntity.ok(response);
		} else {
			response.put("message", "Expense not found for this Employee: " + username + " or expense ID: " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Use 404 for not found
		}
	}

	@PutMapping(value = "/update-expense/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Expense> updateExpense(
			@PathVariable String username, 
			@RequestPart("expense") Expense updatedExpense, // Directly bind to Expense object
			@RequestPart(value = "receipt", required = false) MultipartFile receiptFile ) {
		try {
			Expense updated = employeeService.updateExpense(username, updatedExpense, receiptFile);
			return ResponseEntity.ok(updated);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build(); // if expense is not found or doesn't belong to the user
		} catch (FileStorageException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		} catch (Exception e) { // Catch any other unexpected exceptions
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("/update-expense-name")
	public boolean updateExpenseName(@RequestParam Long id, @RequestParam String name) {
		return employeeService.updateExpenseName(id, name);
	}
	
	// NEW: Endpoint to serve the receipt file for employees
		@GetMapping("/receipts/{filename:.+}") // Regex to allow dot in filename
		public ResponseEntity<Resource> downloadReceipt(@PathVariable String filename) {
			Resource resource = null;
			try {
				// EmployeeService will delegate to ExpenseService which then uses FileStorageService
				resource = employeeService.loadReceiptAsResource(filename);
			} catch (MyFileNotFoundException e) { // Catch specific file not found exception
				return ResponseEntity.notFound().build();
			} catch (Exception e) {
				// Generic error for other issues during file loading
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}

			// Try to determine file's content type
			String contentType = null;
			try {
				contentType = Files.probeContentType(resource.getFile().toPath());
			} catch (IOException ex) {
				// Fallback if content type cannot be determined
				System.err.println("Could not determine file type for " + filename + ": " + ex.getMessage());
			}

			// Fallback to generic binary if type cannot be determined
			if (contentType == null) {
				contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
			}

			// Use "inline" to display in browser, "attachment" to force download
			String contentDisposition = "inline; filename=\"" + resource.getFilename() + "\"";

			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
					.body(resource);
		}
}
