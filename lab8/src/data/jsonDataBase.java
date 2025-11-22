package data;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.*; 
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import model.User.UserRole;

public class jsonDataBase {

    private static final String USERS_FILE = "users.json";
    private static final String COURSES_FILE = "courses.json";
    
    // Configure Gson with custom type adapter for the User hierarchy
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(User.class, new UserDeserializer()) // Critical for Abstract class
            .setPrettyPrinting() // Makes the JSON human-readable
            .create();

    // --- USER MANAGEMENT ---

    public static void saveUsers(List<User> users) {
        try (Writer writer = new FileWriter(USERS_FILE)) {
            gson.toJson(users, writer);
            System.out.println("Users saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);

        if (!file.exists()) return users; // Return empty list if file doesn't exist

        try (Reader reader = new FileReader(file)) {
            Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> loadedUsers = gson.fromJson(reader, userListType);
            if (loadedUsers != null) {
                users = loadedUsers;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // --- COURSE MANAGEMENT ---

    public static void saveCourses(List<Course> courses) {
        try (Writer writer = new FileWriter(COURSES_FILE)) {
            gson.toJson(courses, writer);
            System.out.println("Courses saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        File file = new File(COURSES_FILE);

        if (!file.exists()) return courses;

        try (Reader reader = new FileReader(file)) {
            Type courseListType = new TypeToken<ArrayList<Course>>(){}.getType();
            List<Course> loadedCourses = gson.fromJson(reader, courseListType);
            if (loadedCourses != null) {
                courses = loadedCourses;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courses;
    }

    // --- VALIDATION & LOOKUP UTILS ---

    /**
     * checks if a userId already exists to prevent duplicates during signup.
     */
    public static boolean isUserIdUnique(String userId) {
        List<User> users = loadUsers();
        for (User u : users) {
            if (u.getUserId().equals(userId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks if a courseId already exists.
     */
    public static boolean isCourseIdUnique(String courseId) {
        List<Course> courses = loadCourses();
        for (Course c : courses) {
            if (c.getCourseId().equals(courseId)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Helper to find a specific user by ID
     */
    public static User getUserById(String userId) {
        for (User u : loadUsers()) {
            if (u.getUserId().equals(userId)) return u;
        }
        return null;
    }

    // --- INNER CLASS: POLYMORPHIC DESERIALIZER ---
    // This is the "Magic" that tells Gson how to read abstract 'User' objects
    private static class UserDeserializer implements JsonDeserializer<User> {
        @Override
        public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
                throws JsonParseException {
            
            JsonObject jsonObject = json.getAsJsonObject();
            
            // Look at the "role" field to decide which class to create
            String roleStr = jsonObject.get("role").getAsString();
            UserRole role = UserRole.valueOf(roleStr);

            switch (role) {
                case STUDENT:
                    return context.deserialize(json, Student.class);
                case INSTRUCTOR:
                    return context.deserialize(json, Instructor.class);
                case ADMIN:
                    return context.deserialize(json, Admin.class);
                default:
                    throw new JsonParseException("Unknown role: " + role);
            }
        }
    }
}