
package SkillForgeApp;
import java.util.*;

public class Instructor extends User {
    List<String> createdCourseIds = new ArrayList<>();

    public Instructor(String id, String u, String e, String p) {
        super(id, u, e, p, UserRole.INSTRUCTOR);
    }
}