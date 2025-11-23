
package SkillForgeApp;
public class Admin extends User {
    public Admin(String id, String u, String e, String p) {
        super(id, u, e, p, UserRole.ADMIN);
    }
}