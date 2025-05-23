package com.expensemanagement.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expensemanagement.backend.model.Contact;
import com.expensemanagement.backend.model.Role;
import com.expensemanagement.backend.model.User;
import com.expensemanagement.backend.repository.ContactRepository;
import com.expensemanagement.backend.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ContactRepository contactRepository;
	
	 // Registration
    @Transactional
    public User registerUser(String username, String password, String email, String firstName, String lastName, Role role) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User( username, email, firstName, lastName, role);
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }
    
    // Login
    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return user;
    }

 // Forgot Password - Step 1: Verify User
    @Transactional
    public int verifyUserForForgotPassword(String username, String email, Role role) {
    	System.out.println("inside service : "+username+" - "+email+" - "+role);
        Optional<User> userOptional = userRepository.findByUsernameAndEmailAndRole(username, email, role);
        //System.out.println(userOptional);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found. Please register.");
        }

        User user = userOptional.get();
        // Generate and store OTP (in a real app, store this in a dedicated table with expiry)
        //String otp = generateOTP();
        //Ideally, OTP should be stored in a separate table.  For simplicity, we'll store it in the user object TEMPORARILY.
        //user.setPassword(otp); // Storing OTP in password field TEMPORARILY - don't do this in production
        //userRepository.save(user);

        // Send OTP via email (use your EmailService)
        return emailService.sendOtpMail(user.getEmail(), user.getFirstName(), user.getLastName() );
    }

    // Forgot Password - Step 2: Verify OTP
    @Transactional
    public void verifyOTP(String username, String otp) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        //  In a real app, compare against the stored OTP, not the password
        if (!user.getPassword().equals(otp)) { //  Compare with stored OTP
            throw new IllegalArgumentException("Invalid OTP");
        }
        // Clear the OTP after it's used (Important for security)
        user.setPassword(passwordEncoder.encode("")); //  Clear OTP
        userRepository.save(user);

    }

    // Forgot Password - Step 3: Reset Password
    @Transactional
    public void resetPassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    //to Delete User
    @Transactional
    public void deleteUser(String username) {
    	Optional<User> userOptional=userRepository.findByUsername(username);
    	 if (userOptional.isEmpty()) {
             throw new IllegalArgumentException("User not found. Cannot Remove an User.");
         }
    	 
    	 userRepository.deleteByUsername(username);
    }
    
    public User getUserProfile(String username) {
    	 Optional<User> userOptional=userRepository.findByUsername(username);
    	 if(userOptional.isEmpty()) {
    		 return null;
    	 }else {
    		User user=userOptional.get();
    		return user;
    	 }

    	 //return userRepository.findByUsername(username)
                 //.orElseThrow(() -> new IllegalArgumentException("Invalid username"));
    }
    
    @Transactional
    public User updateUser(String username, User updatedUser) {
    	Optional<User> userOptional=userRepository.findByUsername(username);
    	if(userOptional.isEmpty()) {
    		throw new IllegalArgumentException("User not found. Cannot Updated an User.");
    	}
    	User user=userOptional.get();
    	user.setFirstName(updatedUser.getFirstName());
    	user.setLastName(updatedUser.getLastName());
    	user.setEmail(updatedUser.getEmail());
    	
    	return userRepository.save(user);
    }

    public Contact contactUs(String name, String email, String mobile, String message) {
    	Contact contact=new Contact(name, email, mobile, message);
    	return contactRepository.save(contact);
    }
}
