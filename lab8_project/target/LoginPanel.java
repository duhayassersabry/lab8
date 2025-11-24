
package SkillForgeApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends JPanel {
    public LoginPanel(SkillForgeApp frame) {
        setLayout(new GridLayout(5, 1, 10, 10)); // Adjusted rows to 5
        setBorder(new EmptyBorder(50, 200, 50, 200));
        
        // Pre-filled for easier testing
        JTextField emailField = new JTextField("student@test.com"); 
        JPasswordField passField = new JPasswordField("123");
        
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register New Account"); // New Button

        // Login Logic
        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String pass = new String(passField.getPassword());
            
            boolean found = false;
            for(User u : Database.users) {
                if(u.email.equals(email) && u.password.equals(pass)) {
                    Database.currentUser = u;
                    frame.loginSuccess(u);
                    found = true;
                    break;
                }
            }
            if(!found) JOptionPane.showMessageDialog(this, "Invalid credentials");
        });

        // Navigation to Register Screen
        registerBtn.addActionListener(e -> frame.showRegister());

        add(new JLabel("Skill Forge Login", SwingConstants.CENTER));
        add(emailField);
        add(passField);
        add(loginBtn);
        add(registerBtn); // Add the register button to the panel
    }
}