package model;

public abstract class User {
    // 2. Fields renamed to match assignment requirements
    protected String userId;
    protected String username;
    protected String email;
    protected String passwordHash; // Renamed from 'password' to indicate hashing
    protected UserRole role;       // Changed from String to Enum
public enum UserRole {
    STUDENT,
    INSTRUCTOR,
    ADMIN
}
    // Empty constructor for JSON libraries (Gson/Jackson)
    public User() {}

    public User(String userId, String username, String email, String passwordHash, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public UserRole getRole() { return role; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}