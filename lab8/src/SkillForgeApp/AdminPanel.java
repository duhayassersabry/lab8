
package SkillForgeApp;
import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {
    AdminService adminService = new AdminService();

    public AdminPanel(SkillForgeApp frame) {
        setLayout(new BorderLayout());
        add(new JLabel("ADMIN DASHBOARD - Course Approvals", SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel listPanel = new JPanel(new GridLayout(0, 1));
        
        for(Course c : adminService.getPendingCourses()) {
            JPanel row = new JPanel(new FlowLayout());
            row.add(new JLabel("PENDING: " + c.title + " by " + c.instructorName));
            
            JButton approve = new JButton("Approve");
            approve.setBackground(Color.GREEN);
            approve.addActionListener(e -> {
                adminService.approveCourse(c);
                JOptionPane.showMessageDialog(this, "Course Approved!");
                approve.setEnabled(false);
            });

            JButton reject = new JButton("Reject");
            reject.setBackground(Color.RED);
            reject.addActionListener(e -> {
                adminService.rejectCourse(c);
                JOptionPane.showMessageDialog(this, "Course Rejected");
                reject.setEnabled(false);
            });

            row.add(approve);
            row.add(reject);
            listPanel.add(row);
        }

        add(new JScrollPane(listPanel), BorderLayout.CENTER);
        
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> frame.logout());
        add(logout, BorderLayout.SOUTH);
    }
}