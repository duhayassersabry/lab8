package data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class to handle password encryption using SHA-256.
 * Used during Signup (to store hash) and Login (to verify hash).
 */
public class PasswordHasher {

    /**
     * Hashing Algorithm: SHA-256
     * @param password The plain text password entered by the user.
     * @return A hexadecimal string representing the hashed password.
     */
    public static String hashPassword(String password) {
        try {
            // 1. Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 2. Add password bytes to digest
            md.update(password.getBytes());

            // 3. Get the hash's bytes
            byte[] bytes = md.digest();

            // 4. Convert bytes to Hexadecimal String
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                // Convert byte to hex (and ensure 2 digits with leading zero)
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // If SHA-256 isn't found (unlikely in standard Java), return null
            return null; 
        }
    }
}