package gui;

import services.UserService;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JComboBox<String> combo;
    private JTextField tfEmail;
    private JPasswordField pf;
    private JButton btnLogin, btnRegister;
    private UserService userService = new UserService();

    public LoginFrame() {
        setTitle("SkillForge - Login");
        init();
    }

    private void init() {
        combo = new JComboBox<>(new String[] {"Student","Instructor","Admin"});
        tfEmail = new JTextField(20);
        pf = new JPasswordField(20);
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");

        btnLogin.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> doRegister());

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6); c.gridx=0; c.gridy=0; p.add(new JLabel("Role"), c);
        c.gridx=1; p.add(combo,c);
        c.gridx=0; c.gridy=1; p.add(new JLabel("Email"),c);
        c.gridx=1; p.add(tfEmail,c);
        c.gridx=0; c.gridy=2; p.add(new JLabel("Password"),c);
        c.gridx=1; p.add(pf,c);
        c.gridx=0; c.gridy=3; p.add(btnLogin,c);
        c.gridx=1; p.add(btnRegister,c);

        add(p);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void doLogin() {
        String role = (String) combo.getSelectedItem();
        String email = tfEmail.getText().trim();
        String pass = new String(pf.getPassword());
        User u = userService.login(email, pass, role);
        if (u == null) { JOptionPane.showMessageDialog(this,"Invalid credentials."); return; }

        // open appropriate dashboard
        if (u instanceof Admin) {
            SwingUtilities.invokeLater(() -> { new AdminDashboard((Admin)u).setVisible(true); });
        } else if (u instanceof Instructor) {
            SwingUtilities.invokeLater(() -> { new InstructorDashboard((Instructor)u).setVisible(true); });
        } else {
            SwingUtilities.invokeLater(() -> { new StudentDashboard((Student)u).setVisible(true); });
        }
        dispose();
    }

    private void doRegister() {
        String role = (String) combo.getSelectedItem();
        String email = tfEmail.getText().trim();
        String pass = new String(pf.getPassword());
        if (role.equals("Student")) userService.registerStudent(email, pass);
        else if (role.equals("Instructor")) userService.registerInstructor(email, pass);
        else userService.registerAdmin(email, pass);
        JOptionPane.showMessageDialog(this,"Registered. Now login.");
    }
}
