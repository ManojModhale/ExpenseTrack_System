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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.expensemanagement.backend.dto.ExpenseDto;
import com.expensemanagement.backend.exception.FileStorageException;
import com.expensemanagement.backend.exception.MyFileNotFoundException;
import com.expensemanagement.backend.model.Expense;
import com.expensemanagement.backend.service.ManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "http://localhost:3000")
public class ManagerController {

	@Autowired
	private ManagerService managerService;
	
	// TODO: Replace with actual Spring Security integration to get the authenticated username
	private String getCurrentUsername() {
		return "testuser"; // Placeholder for demonstration, replace with actual user context
	}

	@GetMapping("/expensesByUsername/{username}")
	public ResponseEntity<List<Expense>> getExpensesByManager(@PathVariable String username) {
		List<Expense> expenses = managerService.getExpensesByManager(username);
		/*
		 * Iterator<Expense> iterator = expenses.iterator(); while (iterator.hasNext())
		 * { System.out.println(iterator.next()); }
		 */
		return ResponseEntity.ok(expenses);
	}

	// CHANGED: To handle MultipartFile for receipt upload
	@PostMapping(value = "/addexpense/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Expense> addExpense(@PathVariable String username,
			@RequestPart("expense") Expense expense, // Directly bind to Expense object
			@RequestPart(value = "receipt", required = false) MultipartFile receiptFile) { // Optional receipt file
		try {
			// No need for ObjectMapper.readValue() here, Spring handles it!
			Expense newExpense = managerService.addExpense(username, expense, receiptFile); // Pass the file
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
		boolean isDeleted = managerService.deleteExpense(username, id);
		Map<String, Object> response = new HashMap<>();
		if (isDeleted) {
			response.put("message", "Expense deleted successfully");
			return ResponseEntity.ok(response);
		} else {
			response.put("message", "Expense not found for this Manager: " + username + " or expense ID: " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Use 404 for not found
		}
	}

	// CHANGED: To handle MultipartFile for receipt upload
	@PutMapping(value = "/update-expense/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Expense> updateExpense(
			@PathVariable String username, 
			@RequestPart("expense") Expense updatedExpense, // Directly bind to Expense object
			@RequestPart(value = "receipt", required = false) MultipartFile receiptFile) { // Optional new receipt file
		try {
			// No need for ObjectMapper.readValue() here, Spring handles it!
			Expense result = managerService.updateExpense(username, updatedExpense, receiptFile); // Pass the file
			return ResponseEntity.ok(result);
		} catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // if expense is not found or doesn't belong to the user
        } catch (FileStorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) { // Catch any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
	}

	// Endpoint to get all employee expenses (for manager review)
	@GetMapping("/expenses/allEmployee")
	public ResponseEntity<?> getAllEmployeeExpenses() {
		try {
			List<ExpenseDto> expenses = managerService.getAllEmployeeExpenses();
			return ResponseEntity.ok(expenses);
		} catch (RuntimeException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error fetching all employee expenses: " + e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred while fetching employee expenses.");
		}
	}

	@PutMapping("/expense/{expenseId}/approve")
	public ResponseEntity<?> approveExpense(@PathVariable Long expenseId) {
		try {
			managerService.approveExpense(expenseId);
			return ResponseEntity.ok("Expense approved successfully.");
		} catch (RuntimeException e) { // Catch specific exceptions like ResourceNotFoundException
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error approving expense: " + e.getMessage());
		}
	}

	@PutMapping("/expense/{expenseId}/reject")
	public ResponseEntity<?> rejectExpense(@PathVariable Long expenseId, @RequestBody Map<String, String> payload) {// ,
		try {
			String reason = payload.get("reason"); // Expect a JSON body like { "reason": "..." }
			managerService.rejectExpense(expenseId);// , reason
			return ResponseEntity.ok("Expense rejected successfully.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (RuntimeException e) { // Catch specific exceptions like ResourceNotFoundException
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error rejecting expense: " + e.getMessage());
		}
	}
	
	// NEW: Endpoint for managers to view employee expense receipts
		// This mirrors the one in ExpenseController
		@GetMapping("/receipts/{filename:.+}") // Regex to allow dot in filename
		public ResponseEntity<Resource> downloadReceipt(@PathVariable String filename) {
			Resource resource = null;
			try {
				// ManagerService will delegate to ExpenseService which then uses FileStorageService
				resource = managerService.loadReceiptAsResource(filename);
			} catch (MyFileNotFoundException e) {
				return ResponseEntity.notFound().build();
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}

			String contentType = null;
			try {
				contentType = Files.probeContentType(resource.getFile().toPath());
			} catch (IOException ex) {
				System.err.println("Could not determine file type for " + filename + ": " + ex.getMessage());
			}

			if (contentType == null) {
				contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
			}

			String contentDisposition = "inline; filename=\"" + resource.getFilename() + "\"";

			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
					.body(resource);
		}

}
