package service;

import data.jsonDataBase;
import data.PasswordHasher;
import model.User;
import java.util.List;

public class AuthService {

    public User login(String email, String rawPassword) {
        List<User> users = jsonDataBase.loadUsers();
        String inputHash = PasswordHasher.hashPassword(rawPassword);

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                if (user.getPasswordHash().equals(inputHash)) {
                    return user; 
                }
            }
        }
        return null; 
    }

    public boolean signup(User newUser) {
        List<User> users = jsonDataBase.loadUsers();

        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(newUser.getEmail()) || 
                u.getUserId().equals(newUser.getUserId())) {
                return false; 
            }
        }

        users.add(newUser);
        jsonDataBase.saveUsers(users);
        return true;
    }
}