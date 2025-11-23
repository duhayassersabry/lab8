package services;
import java.util.stream.Collectors;
import data.JsonDatabaseManager;
import models.*;

import java.util.List;

public class UserService {
    private JsonDatabaseManager db = new JsonDatabaseManager();

    public UserService(){}

    public User login(String email, String pass, String role) {
        if ("Instructor".equalsIgnoreCase(role)) {
            Instructor i = db.findInstructorByEmail(email);
            if (i != null && db.verifyPassword(pass, i.getPassword())) return i;
            return null;
        } else if ("Admin".equalsIgnoreCase(role)) {
            Admin a = db.findAdminByEmail(email);
            if (a != null && db.verifyPassword(pass, a.getPassword())) return a;
            return null;
        } else {
            Student s = db.findStudentByEmail(email);
            if (s != null && db.verifyPassword(pass, s.getPassword())) return s;
            return null;
        }
    }

    public Student registerStudent(String name, String email, String password) {
        // Check if email already exists
        if (findStudentByEmail(email) != null) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        
        if (email == null || email.trim().isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Please enter a valid email address");
        }
        
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        
        // Create and save new student
        String id = "S" + System.currentTimeMillis();
        String hashed = db.sha256Hex(password);
        Student student = new Student(id, name, email, hashed);
        db.updateUser(student);
        return student;
    }

    public Instructor registerInstructor(String name, String email, String password) {
        // Check if email already exists
        if (findInstructorByEmail(email) != null) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        
        if (email == null || email.trim().isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Please enter a valid email address");
        }
        
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        
        // Create and save new instructor
        String id = "I" + System.currentTimeMillis();
        String hashed = db.sha256Hex(password);
        Instructor instructor = new Instructor(id, name, email, hashed);
        db.updateUser(instructor);
        return instructor;
    }

    public Admin registerAdmin(String email, String password) {
        String id = "A" + System.currentTimeMillis();
        String hashed = db.sha256Hex(password);
        Admin a = new Admin(id, email, email, hashed);
        db.updateUser(a);
        return a;
    }

    // ... other methods ...



    public List<Student> allStudents() {
        return db.loadUsers().stream().filter(u->"Student".equals(u.getRole())).map(u->(Student)u).collect(Collectors.toList());
    }
    public List<Instructor> allInstructors() {
        return db.loadUsers().stream().filter(u->"Instructor".equals(u.getRole())).map(u->(Instructor)u).collect(Collectors.toList());
    }
    public List<Admin> allAdmins() {
        return db.loadUsers().stream().filter(u->"Admin".equals(u.getRole())).map(u->(Admin)u).collect(Collectors.toList());
    }

    public Student findStudentByEmail(String email) { 
        return db.findStudentByEmail(email); 
    }
    
    public Instructor findInstructorByEmail(String email) {
        return db.findInstructorByEmail(email);
    }
    
    public Admin findAdminByEmail(String email) {
        return db.findAdminByEmail(email);
    }
}
