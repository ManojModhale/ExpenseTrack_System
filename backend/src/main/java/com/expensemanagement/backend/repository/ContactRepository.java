package com.expensemanagement.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expensemanagement.backend.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

}
