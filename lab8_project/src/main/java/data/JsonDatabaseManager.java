package data;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;

public class JsonDatabaseManager {
    private static final String USERS_FILE = "users.json";
    private static final String COURSES_FILE = "courses.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonDatabaseManager() {
        ensureFilesExist();
    }

    private void ensureFilesExist() {
        try {
            Path root = Paths.get(".");
            if (!Files.exists(root.resolve(USERS_FILE))) Files.writeString(root.resolve(USERS_FILE), "[]");
            if (!Files.exists(root.resolve(COURSES_FILE))) Files.writeString(root.resolve(COURSES_FILE), "[]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // hashing the password in the file
    public String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean verifyPassword(String plain, String hashed) {
        if (plain == null || hashed == null) return false;
        return sha256Hex(plain).equalsIgnoreCase(hashed);
    }

    //users
    public List<User> loadUsers() {
        try (Reader r = new FileReader(USERS_FILE)) {
            Type t = new TypeToken<List<JsonObject>>(){}.getType();
            List<JsonObject> list = gson.fromJson(r, t);
            List<User> users = new ArrayList<>();
            if (list == null) list = new ArrayList<>();
            boolean changed = false;
            for (JsonObject obj : list) {
                String role = obj.get("role").getAsString();
                User u = null;
                switch (role) {
                    case "Student":
                        u = gson.fromJson(obj, Student.class);
                        break;
                    case "Instructor":
                        u = gson.fromJson(obj, Instructor.class);
                        break;
                    case "Admin":
                        u = gson.fromJson(obj, Admin.class);
                        break;
                }
                if (u != null) {
                    String pwd = u.getPassword();
                    if (pwd != null && !pwd.matches("(?i)^[0-9a-f]{64}$")) {
                        u.setPassword(sha256Hex(pwd));
                        changed = true;
                    }
                    users.add(u);
                }
            }
            if (changed) saveUsers(users);
            return users;
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveUsers(List<User> users) {
        try (Writer w = new FileWriter(USERS_FILE)) {
            gson.toJson(users, w);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void updateUser(User u) {
        List<User> us = loadUsers();
        boolean found = false;
        for (int i = 0; i < us.size(); i++) {
            if (us.get(i).getId().equals(u.getId())) {
                us.set(i, u);
                found = true;
                break;
            }
        }
        if (!found) us.add(u);
        saveUsers(us);
    }

    public User findUserByEmail(String email) {
        for (User u : loadUsers()) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    public Student findStudentByEmail(String email) {
        User u = findUserByEmail(email);
        if (u instanceof Student) return (Student) u;
        return null;
    }

    public Instructor findInstructorByEmail(String email) {
        User u = findUserByEmail(email);
        if (u instanceof Instructor) return (Instructor) u;
        return null;
    }

    public Admin findAdminByEmail(String email) {
        User u = findUserByEmail(email);
        if (u instanceof Admin) return (Admin) u;
        return null;
    }

   //Courses
    public List<Course> loadCourses() {
        try (Reader r = new FileReader(COURSES_FILE)) {
            Type t = new TypeToken<List<Course>>(){}.getType();
            List<Course> list = gson.fromJson(r, t);
            return list == null ? new ArrayList<>() : list;
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveCourses(List<Course> courses) {
        try (Writer w = new FileWriter(COURSES_FILE)) {
            gson.toJson(courses, w);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Course findCourseById(String id) {
        for (Course c : loadCourses()) {
            if (c.getCourseId().equals(id)) return c;
        }
        return null;
    }

    public void updateCourse(Course c) {
        List<Course> cs = loadCourses();
        boolean found = false;
        for (int i = 0; i < cs.size(); i++) {
            if (cs.get(i).getCourseId().equals(c.getCourseId())) {
                cs.set(i, c);
                found = true;
                break;
            }
        }
        if (!found) cs.add(c);
        saveCourses(cs);
    }
}
