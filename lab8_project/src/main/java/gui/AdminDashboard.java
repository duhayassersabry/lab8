package gui;

import services.CourseService;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private Admin admin;
    private DefaultListModel<String> model = new DefaultListModel<>();
    private JList<String> list = new JList<>(model);
    private CourseService courseService = new CourseService();

    public AdminDashboard(Admin a){
        this.admin = a;
        setTitle("Admin - " + a.getEmail());
        init();
        loadPending();
    }

    private void init(){
        JButton approve = new JButton("Approve");
        JButton reject = new JButton("Reject");
        approve.addActionListener(e-> handle(true));
        reject.addActionListener(e-> handle(false));

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.add(approve); south.add(reject);
        p.add(south, BorderLayout.SOUTH);

        add(p);
        setSize(800,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void loadPending(){
        model.clear();
        List<Course> pending = courseService.pendingCourses();
        for (Course c: pending) model.addElement(c.getCourseId() + " - " + c.getTitle() + " (by " + c.getInstructorId() + ")");
    }

    private void handle(boolean app){
        String sel = list.getSelectedValue();
        if (sel==null) { JOptionPane.showMessageDialog(this,"Select a course"); return; }
        String id = sel.split(" - ")[0];
        courseService.approveCourse(id, app);
        loadPending();
        JOptionPane.showMessageDialog(this, app ? "Approved":"Rejected");
    }
}
