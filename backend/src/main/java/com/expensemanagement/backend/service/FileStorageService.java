package com.expensemanagement.backend.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.expensemanagement.backend.exception.FileStorageException;
import com.expensemanagement.backend.exception.MyFileNotFoundException;


@Service
public class FileStorageService {
	
	private final Path fileStorageLocation;
    private final String baseUrl; // To construct full URLs for receipts

 // Constructor to initialize file storage location and base URL for serving files
    public FileStorageService(
            @Value("${file.upload-dir}") String uploadDir,
            @Value("${application.base-url:http://localhost:8181}") String appBaseUrl) { // Default to localhost:8181 if not set
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.baseUrl = appBaseUrl;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }
    
    /**
     * Stores a multipart file in the configured directory and returns its unique filename.
     * The unique filename will be used as the receiptUrl in the Expense entity.
     *
     * @param file The MultipartFile received from the client.
     * @return The unique filename generated for the stored file.
     * @throws FileStorageException if the file cannot be stored.
     */
    public String storeFile(MultipartFile file) {
        // Normalize file name to prevent directory traversal attacks
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // Extract file extension (e.g., .jpg, .pdf)
        String fileExtension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalFileName.length() - 1) {
            fileExtension = originalFileName.substring(dotIndex);
        }

        // Generate a unique file name using UUID
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);

        try {
            // Copy file to the target location, replacing existing if any (though UUID should prevent this)
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
         // Return the relative path or a URL that the backend can serve
            // For now, we'll return the unique filename which our controller will prepend with base URL
            return uniqueFileName; // Return only the unique filename
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }
    
    /**
     * Loads a file as a Spring Resource based on its unique filename.
     * This is used by the controller to serve the file.
     *
     * @param fileName The unique filename of the stored receipt.
     * @return A Spring Resource representing the file.
     * @throws MyFileNotFoundException if the file does not exist.
     */
    public Resource loadFileAsResource(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new MyFileNotFoundException("Filename cannot be null or empty.");
        }
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found or not readable: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found or invalid URL: " + fileName, ex);
        }
    }
    
    /**
     * Deletes a file from storage based on its unique filename.
     *
     * @param fileName The unique filename of the file to delete.
     * @return true if the file was successfully deleted, false otherwise.
     */
    public boolean deleteFile(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false; // Nothing to delete
        }
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            // Ensure we are deleting a file within our designated storage location
            if (filePath.startsWith(this.fileStorageLocation)) {
                return Files.deleteIfExists(filePath);
            } else {
                // Prevent path traversal outside the designated directory
                System.err.println("Attempted to delete file outside storage location: " + fileName);
                return false;
            }
        } catch (IOException ex) {
            // Log the exception for debugging but don't throw to avoid interrupting main flow
            System.err.println("Could not delete file " + fileName + ": " + ex.getMessage());
            return false;
        }
    }
    
    /**
     * Constructs the full URL for a receipt based on its unique filename.
     * This is what will be stored in the Expense entity's receiptUrl field.
     *
     * @param uniqueFileName The unique filename returned by storeFile().
     * @return The full public URL to access the receipt.
     */
    public String getFileUrl(String uniqueFileName) {
        if (uniqueFileName == null || uniqueFileName.trim().isEmpty()) {
            return null;
        }
        // Assuming your backend serves static content from /api/expenses/receipts/
        return baseUrl + "/api/expenses/receipts/" + uniqueFileName;
    }
    
}
