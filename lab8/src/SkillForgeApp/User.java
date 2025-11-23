
package SkillForgeApp;

public abstract class User {
    String id;
    String username;
    String email;
    String password;
    UserRole role;

    public User(String id, String username, String email, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
