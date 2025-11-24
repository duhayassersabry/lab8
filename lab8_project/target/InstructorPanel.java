package SkillForgeApp;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class InstructorPanel extends JPanel {
    InstructorService instService = new InstructorService();
    Instructor currentInstructor;

    public InstructorPanel(SkillForgeApp frame, Instructor inst) {
        this.currentInstructor = inst;
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();

        // --- Tab 1: Create Course ---
        JPanel createPanel = new JPanel(new GridLayout(5, 1));
        JTextField titleF = new JTextField();
        JTextField descF = new JTextField();
        JButton createBtn = new JButton("Create Course");
        
        createBtn.addActionListener(e -> {
            instService.createCourse(titleF.getText(), descF.getText(), inst.username);
            JOptionPane.showMessageDialog(this, "Course Created! Wait for Admin Approval.");
            // Clear fields
            titleF.setText("");
            descF.setText("");
        });
        
        createPanel.add(new JLabel("Course Title:")); createPanel.add(titleF);
        createPanel.add(new JLabel("Description:")); createPanel.add(descF);
        createPanel.add(createBtn);

        // --- Tab 2: My Courses (View Courses & Lessons) ---
        JPanel myCoursesPanel = new JPanel();
        myCoursesPanel.setLayout(new BoxLayout(myCoursesPanel, BoxLayout.Y_AXIS));
        
        // Refresh button to load courses
        JButton refreshBtn = new JButton("Refresh My Courses List");
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));

        refreshBtn.addActionListener(e -> {
            listContainer.removeAll();
            List<Course> myCourses = instService.getInstructorCourses(inst.username);
            
            if(myCourses.isEmpty()) {
                listContainer.add(new JLabel("No courses created yet."));
            } else {
                for(Course c : myCourses) {
                    // Create a panel for each course
                    JPanel courseCard = new JPanel(new BorderLayout());
                    courseCard.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.BLUE), 
                        c.title + " [" + c.status + "]"
                    ));
                    
                    // Description
                    courseCard.add(new JLabel("<html><i>" + c.description + "</i></html>"), BorderLayout.NORTH);

                    // Lessons List
                    JPanel lessonsPanel = new JPanel(new GridLayout(0, 1));
                    lessonsPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
                    lessonsPanel.add(new JLabel("Lessons:"));
                    
                    if(c.lessons.isEmpty()) {
                        lessonsPanel.add(new JLabel("- No lessons yet"));
                    } else {
                        for(Lesson l : c.lessons) {
                            lessonsPanel.add(new JLabel(" • " + l.title));
                        }
                    }
                    
                    // Add Lesson Button
                    JButton addLessonBtn = new JButton("Add Lesson");
                    addLessonBtn.addActionListener(ev -> {
                        String lTitle = JOptionPane.showInputDialog("Lesson Title:");
                        String lContent = JOptionPane.showInputDialog("Lesson Content:");
                        if(lTitle != null && lContent != null) {
                            // Simplified Quiz creation for demo
                            Quiz q = new Quiz();
                            q.addQuestion("Is this simple?", "yes");
                            
                            instService.addLessonToCourse(c, lTitle, lContent, q);
                            JOptionPane.showMessageDialog(this, "Lesson Added!");
                            refreshBtn.doClick(); // Refresh UI
                        }
                    });

                    courseCard.add(lessonsPanel, BorderLayout.CENTER);
                    courseCard.add(addLessonBtn, BorderLayout.SOUTH);
                    
                    listContainer.add(courseCard);
                    listContainer.add(Box.createVerticalStrut(10)); // Spacing
                }
            }
            listContainer.revalidate();
            listContainer.repaint();
        });

        // Trigger initial load
        refreshBtn.doClick();

        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.add(refreshBtn, BorderLayout.NORTH);
        scrollWrapper.add(new JScrollPane(listContainer), BorderLayout.CENTER);

        // --- Tab 3: Insights ---
        JPanel insightsPanel = new JPanel();
        insightsPanel.add(new JLabel("Student Performance Analytics (Simulated)"));
        JProgressBar pBar = new JProgressBar(0, 100);
        pBar.setValue(75);
        pBar.setString("Avg Class Score: 75%");
        pBar.setStringPainted(true);
        insightsPanel.add(pBar);

        tabs.add("Manage Courses", createPanel);
        tabs.add("My Courses & Lessons", scrollWrapper); // New Tab
        tabs.add("Insights", insightsPanel);
        
        add(tabs, BorderLayout.CENTER);
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> frame.logout());
        add(logout, BorderLayout.SOUTH);
    }
}
