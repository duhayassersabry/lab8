
package SkillForgeApp;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.UUID;

public class RegisterPanel extends JPanel {
    
    public RegisterPanel(SkillForgeApp frame) {
        setLayout(new GridLayout(6, 1, 10, 10));
        setBorder(new EmptyBorder(40, 150, 40, 150));

        // Form Fields
        JTextField userField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();
        
        // Role Selection - Updated to include Admin
        String[] roles = {"Student", "Instructor", "Admin"};
        JComboBox<String> roleBox = new JComboBox<>(roles);

        JButton registerBtn = new JButton("Create Account");
        JButton backBtn = new JButton("Back to Login");

        // Register Logic
        registerBtn.addActionListener(e -> {
            String username = userField.getText();
            String email = emailField.getText();
            String pass = new String(passField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            // Basic Validation
            if(username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            // Create ID
            String id = UUID.randomUUID().toString().substring(0, 5);
            User newUser;

            // Create specific user type
            if("Student".equals(role)) {
                newUser = new Student(id, username, email, pass);
            } else if ("Instructor".equals(role)) {
                newUser = new Instructor(id, username, email, pass);
            } else {
                // Handle Admin creation
                newUser = new Admin(id, username, email, pass);
            }

            // Save to Database
            Database.users.add(newUser);
            
            JOptionPane.showMessageDialog(this, "Registration Successful! Please Login.");
            frame.showLogin(); // Go back to login
        });

        // Back Logic
        backBtn.addActionListener(e -> frame.showLogin());

        // Add Components
        add(new JLabel("Username:")); add(userField);
        add(new JLabel("Email:")); add(emailField);
        add(new JLabel("Password:")); add(passField);
        add(new JLabel("Role:")); add(roleBox);
        add(registerBtn);
        add(backBtn);
    }
}