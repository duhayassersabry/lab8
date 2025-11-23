package gui;

import services.UserService;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JPanel mainPanel;
    private JComboBox<String> roleCombo;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton, switchButton;
    private JLabel titleLabel, errorLabel;
    private boolean isLoginMode = true;
    private JTextField nameField;
    private JPasswordField confirmPasswordField;
    private UserService userService;

    public LoginFrame() {
        setTitle("SkillForge - Login");
        userService = new UserService();
        initComponents();
        setupLayout();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        mainPanel = new JPanel(new GridBagLayout());
        
        // Common components
        roleCombo = new JComboBox<>(new String[]{"Student", "Instructor", "Admin"});
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        switchButton = new JButton("Create New Account");
        titleLabel = new JLabel("Login to SkillForge");
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        
        // Additional registration fields
        nameField = new JTextField(20);
        confirmPasswordField = new JPasswordField(20);
        
        // Set initial visibility
        updateViewForMode();
        
        // Add action listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegistration());
        switchButton.addActionListener(e -> toggleMode());
    }

    private void setupLayout() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, c);
        
        // Error label
        c.gridy++; c.gridwidth = 2;
        mainPanel.add(errorLabel, c);
        
        // Role
        c.gridy++; c.gridwidth = 1;
        mainPanel.add(new JLabel("Role:"), c);
        c.gridx = 1;
        mainPanel.add(roleCombo, c);
        
        // Name field (for registration)
        c.gridy++; c.gridx = 0;
        mainPanel.add(new JLabel("Full Name:"), c);
        c.gridx = 1;
        mainPanel.add(nameField, c);
        
        // Email
        c.gridy++; c.gridx = 0;
        mainPanel.add(new JLabel("Email:"), c);
        c.gridx = 1;
        mainPanel.add(emailField, c);
        
        // Password
        c.gridy++; c.gridx = 0;
        mainPanel.add(new JLabel("Password:"), c);
        c.gridx = 1;
        mainPanel.add(passwordField, c);
        
        // Confirm Password (for registration)
        c.gridy++; c.gridx = 0;
        mainPanel.add(new JLabel("Confirm Password:"), c);
        c.gridx = 1;
        mainPanel.add(confirmPasswordField, c);
        
        // Buttons
        c.gridy++; c.gridx = 0; c.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, c);
        
        // Switch mode button
        c.gridy++; c.gridx = 0; c.gridwidth = 2;
        mainPanel.add(switchButton, c);
        
        add(mainPanel);
    }

    private void toggleMode() {
        isLoginMode = !isLoginMode;
        updateViewForMode();
    }

    private void updateViewForMode() {
        titleLabel.setText(isLoginMode ? "Login to SkillForge" : "Create New Account");
        loginButton.setVisible(isLoginMode);
        registerButton.setVisible(!isLoginMode);
        switchButton.setText(isLoginMode ? "Create New Account" : "Back to Login");
        nameField.setVisible(!isLoginMode);
        confirmPasswordField.setVisible(!isLoginMode);
        
        // Update labels
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().equals("Full Name:") || label.getText().equals("Confirm Password:")) {
                    label.setVisible(!isLoginMode);
                }
            }
        }
        
        // Clear fields when switching modes
        if (isLoginMode) {
            nameField.setText("");
            confirmPasswordField.setText("");
        }
        errorLabel.setText(" ");
        
        pack();
        setLocationRelativeTo(null);
    }

    private void handleLogin() {
        String role = (String) roleCombo.getSelectedItem();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter both email and password");
            return;
        }
        
        try {
            User user = userService.login(email, password, role);
            if (user == null) {
                showError("Invalid email or password");
                return;
            }
            
            // Open appropriate dashboard
            SwingUtilities.invokeLater(() -> {
                if (user instanceof Admin) {
                    new AdminDashboard((Admin) user).setVisible(true);
                } else if (user instanceof Instructor) {
                    new InstructorDashboard((Instructor) user).setVisible(true);
                } else {
                    new StudentDashboard((Student) user).setVisible(true);
                }
                dispose();
            });
            
        } catch (Exception e) {
            showError("Login failed: " + e.getMessage());
        }
    }

    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();
        
        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required");
            return;
        }
        
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Please enter a valid email address");
            return;
        }
        
        if (password.length() < 8) {
            showError("Password must be at least 8 characters long");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }
        
        try {
            User newUser = null;
            switch (role) {
                case "Student":
                    newUser = userService.registerStudent(name, email, password);
                    break;
                case "Instructor":
                    newUser = userService.registerInstructor(name, email, password);
                    break;
                case "Admin":
                    // Admin registration might be restricted in production
                    newUser = userService.registerAdmin(email, password);
                    break;
            }
            
            if (newUser != null) {
                JOptionPane.showMessageDialog(this, 
                    "Registration successful! Please login with your credentials.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                toggleMode(); // Switch back to login
            } else {
                showError("Registration failed. Please try again.");
            }
        } catch (Exception e) {
            showError("Registration error: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText("<html>" + message + "</html>");
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame().setVisible(true);
        });
    }
}