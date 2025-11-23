package data;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

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

    // ---------- Password hashing helpers ----------
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

    // ---------- Users (polymorphic) ----------
    public List<User> loadUsers() {
        try (Reader r = new FileReader(USERS_FILE)) {
            JsonElement root = JsonParser.parseReader(r);
            List<User> out = new ArrayList<>();
            if (root != null && root.isJsonArray()) {
                for (JsonElement el : root.getAsJsonArray()) {
                    JsonObject obj = el.getAsJsonObject();
                    String role = obj.has("role") ? obj.get("role").getAsString() : "Student";
                    User u;
                    if ("Instructor".equals(role)) u = gson.fromJson(obj, Instructor.class);
                    else if ("Admin".equals(role)) u = gson.fromJson(obj, Admin.class);
                    else u = gson.fromJson(obj, Student.class);

                    // MIGRATION: if password is not hashed (not 64 hex chars), hash it and mark for save
                    String pwd = u.getPassword();
                    if (pwd != null && !isHexSha256(pwd)) {
                        String hashed = sha256Hex(pwd);
                        u.setPassword(hashed);
                        out.add(u); // add now, we'll persist after loop
                    } else {
                        out.add(u);
                    }
                }
            }
            // After loading, ensure any unhashed pw were updated in file
            // (we detect by checking whether any have non-hex — but we already replaced them in the objects)
            saveUsers(out); // safe: will write hashed passwords (idempotent)
            return out;
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    private boolean isHexSha256(String s) {
        if (s == null) return false;
        return s.matches("(?i)^[0-9a-f]{64}$");
    }

    public void saveUsers(List<User> users) {
        try (Writer w = new FileWriter(USERS_FILE)) {
            gson.toJson(users, w);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void updateUser(User u) {
        List<User> users = loadUsers();
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(u.getId())) {
                users.set(i, u);
                found = true;
                break;
            }
        }
        if (!found) users.add(u);
        saveUsers(users);
    }

    // convenience finders
    public Student findStudentByEmail(String email) {
        for (User u : loadUsers()) {
            if ("Student".equals(u.getRole()) && u.getEmail().equalsIgnoreCase(email)) {
                return (Student) u;
            }
        }
        return null;
    }

    public Instructor findInstructorByEmail(String email) {
        for (User u : loadUsers()) {
            if ("Instructor".equals(u.getRole()) && u.getEmail().equalsIgnoreCase(email)) {
                return (Instructor) u;
            }
        }
        return null;
    }

    public Admin findAdminByEmail(String email) {
        for (User u : loadUsers()) {
            if ("Admin".equals(u.getRole()) && u.getEmail().equalsIgnoreCase(email)) {
                return (Admin) u;
            }
        }
        return null;
    }

    // ---------- Courses ----------
    public List<Course> loadCourses() {
        try (Reader r = new FileReader(COURSES_FILE)) {
            Type t = new TypeToken<List<Course>>() {}.getType();
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
        for (Course c : loadCourses()) if (c.getCourseId().equals(id)) return c;
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
