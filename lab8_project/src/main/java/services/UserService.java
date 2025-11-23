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

    public Student registerStudent(String email, String password) {
        String id = "S" + System.currentTimeMillis();
        String hashed = db.sha256Hex(password); // make sha256Hex() public or provide wrapper; if not, call via db.updateUser after setting
        Student s = new Student(id, email, email.contains("@")?email:email+"@example.com", hashed);
        db.updateUser(s);
        return s;
    }

    public Instructor registerInstructor(String email, String password) {
        String id = "I" + System.currentTimeMillis();
        String hashed = db.sha256Hex(password);
        Instructor i = new Instructor(id, email, email.contains("@")?email:email+"@example.com", hashed);
        db.updateUser(i);
        return i;
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

    public Student findStudentByEmail(String email){ return db.findStudentByEmail(email); } // helper but we can implement in JsonDatabaseManager
}
