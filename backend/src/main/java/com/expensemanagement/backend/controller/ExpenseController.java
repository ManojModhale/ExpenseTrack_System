package com.expensemanagement.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.expensemanagement.backend.exception.FileStorageException;
import com.expensemanagement.backend.model.Expense;
import com.expensemanagement.backend.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExpenseController {
	@Autowired
    private ExpenseService expenseService;

    // TODO: Replace with actual Spring Security integration to get the authenticated username
    private String getCurrentUsername() {
        return "testuser"; // Placeholder for demonstration
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getUserExpenses() {
        String username = getCurrentUsername();
        List<Expense> expenses = expenseService.getExpensesByUser(username);
        return ResponseEntity.ok(expenses);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Expense> addExpense(
            @RequestPart("expense") String expenseJson, // Expense data as JSON string
            @RequestPart(value = "receipt", required = false) MultipartFile receiptFile) { // Optional receipt file
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Expense expense = objectMapper.readValue(expenseJson, Expense.class);

            Expense newExpense = expenseService.addExpense(getCurrentUsername(), expense, receiptFile);
            return new ResponseEntity<>(newExpense, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (RuntimeException e) { // Catch more specific exceptions if desired
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Expense> updateExpense(
            @PathVariable Long id,
            @RequestPart("expense") String expenseJson,
            @RequestPart(value = "receipt", required = false) MultipartFile receiptFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Expense updatedExpense = objectMapper.readValue(expenseJson, Expense.class);
            updatedExpense.setId(id); // Ensure the ID is set from the path variable

            Expense result = expenseService.updateExpense(getCurrentUsername(), updatedExpense, receiptFile);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        try {
            if (expenseService.deleteExpense(getCurrentUsername(), id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            // Handle cases where user not found or other service-level runtime errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Endpoint to serve the receipt file
    // This endpoint now specifically uses the *filename* part of the URL.
    @GetMapping("/receipts/{filename:.+}") // Regex to allow dot in filename
    public ResponseEntity<Resource> downloadReceipt(@PathVariable String filename) {
        Resource resource = null;
        try {
            resource = expenseService.loadReceiptAsResource(filename);
        } /*catch (MyFileNotFoundException e) { // Catch specific file not found exception
            return ResponseEntity.notFound().build();
        }*/ catch (Exception e) {
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
