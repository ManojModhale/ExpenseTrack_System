package com.expensemanagement.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensemanagement.backend.model.Contact;
import com.expensemanagement.backend.model.Role;
import com.expensemanagement.backend.model.User;
import com.expensemanagement.backend.service.UserService;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	@Autowired
	private UserService userService;

	// Registration
	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, Object> request) {
		System.out.println(request.toString());
		try {
			String username = (String) request.get("username");
			String password = (String) request.get("password");
			String email = (String) request.get("email");
			String firstName = (String) request.get("firstName");
			String lastName = (String) request.get("lastName");
			String roleStr = (String) request.get("role");
			Role role = Role.valueOf(roleStr); // Convert string to Role enum
			System.out
					.println(username + "-" + password + "=" + email + "=" + firstName + "-" + lastName + "=" + email);

			User user = userService.registerUser(username, password, email, firstName, lastName, role);
			System.out.println(user);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "User registered successfully");
			response.put("user", user);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (IllegalArgumentException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	// Login
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, Object> request) {
		System.out.println(request.toString());
		try {
			String username = (String) request.get("username");
			String password = (String) request.get("password");
			User user = userService.loginUser(username, password);
			System.out.println("After Login");
			//System.out.println(user);
			user.sanitize();
			
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Login successful");
			response.put("user", user);
			// In a real application, you would generate and return a JWT token here.
			// String token = "dummy_token"; // Replace with actual JWT generation
			// response.put("token", token);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}

	// Forgot Password - Step 1: Verify User
	@PostMapping("/verify-forgotpass-user")
	public ResponseEntity<Map<String, Object>> verifyUserForForgotPassword(@RequestBody Map<String, Object> request) {
		try {
			String username = (String) request.get("username");
			String email = (String) request.get("email");
			String roleStr = (String) request.get("role");
			Role role = Role.valueOf(roleStr);
			System.out.println("inside controller : "+username+" - "+email+" - "+role);
			int otp = userService.verifyUserForForgotPassword(username, email, role);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "User verified. OTP sent to email.");
			response.put("otp", otp);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", e.getMessage());
			if (e.getMessage().contains("register")) {
				errorResponse.put("redirect", true); // Add a redirect flag
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // Or 404
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	// Forgot Password - Step 3: Reset Password
	@PostMapping("/reset-password")
	public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody Map<String, Object> request) {
		try {
			String username = (String) request.get("username");
			String newPassword = (String) request.get("newPassword");
			userService.resetPassword(username, newPassword);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Password reset successfully. Please login with your new password.");
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	@DeleteMapping("/remove-user")
    public ResponseEntity<Map<String, Object>> removeUser(@RequestBody Map<String, Object> request) {
    	try {
    		String username = (String) request.get("username"); 		
    		userService.deleteUser(username);
    		 //Map<String, Object> response = new HashMap<>();
    		 //response.put("message", "User Permanently Removed.");
    		 //return ResponseEntity.ok(response);
            return ResponseEntity.noContent().build();
    	} catch(IllegalArgumentException e) {
    		Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    	}
    }
	
	@GetMapping("/get-user-profile/{username}")
	public ResponseEntity<?> getUserProfile(@PathVariable String username) {
		try {
			User user=userService.getUserProfile(username);
			if(user == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
			}
			user.sanitize();
			return ResponseEntity.ok(user);
		}catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user profile: " + e.getMessage());
        }
	}
	
	@PutMapping("/update-user/{username}")
	public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User updatedUser) {
		try {
        	User updated=userService.updateUser(username, updatedUser);
        	return ResponseEntity.ok(updated);
    	} catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
	}
	
	@PostMapping("/contact-us")
	public ResponseEntity<Map<String, Object>> contactus(@RequestBody Map<String, Object> request){
		try {
			String name= (String) request.get("name");
			String email= (String) request.get("email");
			String mobile= (String) request.get("mobile");
			String message= (String) request.get("message");
			System.out.println(name+"-"+email+"-"+mobile+"-"+message);
			Contact contact= userService.contactUs(name, email, mobile, message);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Contact Message Stored successfully");
			response.put("contact", contact);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (IllegalArgumentException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}
}
