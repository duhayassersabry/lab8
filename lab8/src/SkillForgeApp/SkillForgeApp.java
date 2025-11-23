
package SkillForgeApp;

import javax.swing.*;
import java.awt.*;

public class SkillForgeApp extends JFrame {
    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    public SkillForgeApp() {
        setTitle("Skill Forge System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Add panels to CardLayout with unique String IDs
        mainPanel.add(new LoginPanel(this), "LOGIN");
        mainPanel.add(new RegisterPanel(this), "REGISTER");
        
        add(mainPanel);
    }

    // Method to switch to Register Screen
    public void showRegister() {
        cardLayout.show(mainPanel, "REGISTER");
    }

    // Method to switch to Login Screen
    public void showLogin() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    // Handle successful login
    public void loginSuccess(User user) {
        if (user instanceof Admin) {
            mainPanel.add(new AdminPanel(this), "ADMIN");
            cardLayout.show(mainPanel, "ADMIN");
        } else if (user instanceof Instructor) {
            mainPanel.add(new InstructorPanel(this, (Instructor) user), "INSTRUCTOR");
            cardLayout.show(mainPanel, "INSTRUCTOR");
        } else if (user instanceof Student) {
            mainPanel.add(new StudentPanel(this, (Student) user), "STUDENT");
            cardLayout.show(mainPanel, "STUDENT");
        }
    }

    // Logout logic
    public void logout() {
        Database.currentUser = null;
        cardLayout.show(mainPanel, "LOGIN");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SkillForgeApp().setVisible(true));
    }
}