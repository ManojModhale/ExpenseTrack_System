package com.expensemanagement.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.expensemanagement.backend.model.Category;
import com.expensemanagement.backend.model.Status;

public class ExpenseDto {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private Category category;
	
	private BigDecimal amount;
	
	private LocalDate date;
	
	private Status status;
	
	private String rejectionReason; // For manager's rejection reason
	
    private String receiptUrl; // URL to the uploaded receipt
	
	private String requesterFirstName;
	
    private String requesterLastName;
    
    private String requesterUsername;	// Useful for identifying the employee

	public ExpenseDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExpenseDto(Long id, String name, String description, Category category, BigDecimal amount, LocalDate date, Status status,
		 String rejectionReason, String receiptUrl, String requesterFirstName, String requesterLastName,
			String requesterUsername) {
		super();
		this.id = id;
		this.name=name;
		this.description = description;
		this.category = category;
		this.amount = amount;
		this.date = date;
		this.status = status;
		this.rejectionReason = rejectionReason;
		this.receiptUrl = receiptUrl;
		this.requesterFirstName = requesterFirstName;
		this.requesterLastName = requesterLastName;
		this.requesterUsername = requesterUsername;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public String getReceiptUrl() {
		return receiptUrl;
	}

	public void setReceiptUrl(String receiptUrl) {
		this.receiptUrl = receiptUrl;
	}

	public String getRequesterFirstName() {
		return requesterFirstName;
	}

	public void setRequesterFirstName(String requesterFirstName) {
		this.requesterFirstName = requesterFirstName;
	}

	public String getRequesterLastName() {
		return requesterLastName;
	}

	public void setRequesterLastName(String requesterLastName) {
		this.requesterLastName = requesterLastName;
	}

	public String getRequesterUsername() {
		return requesterUsername;
	}

	public void setRequesterUsername(String requesterUsername) {
		this.requesterUsername = requesterUsername;
	}
    
}
