package model;

public class Admin extends User {

    public Admin(String userId, String username, String email, String passwordHash) {

        super(userId, username, email, passwordHash, UserRole.ADMIN);
    }

}
