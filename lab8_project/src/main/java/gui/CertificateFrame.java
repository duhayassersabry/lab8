package gui;

import javax.swing.*;
import java.awt.*;
import models.Student;
import models.Course;
import models.Certificate;
import services.CertificateService;

public class CertificateFrame extends JFrame {
    private Student student;
    private Course course;

    public CertificateFrame(Student student, Course course) {
        this.student = student;
        this.course = course;

        setTitle("Certificate Viewer");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 14));
        ta.setText(buildCertificateText());

        JButton viewBtn = new JButton("View Certificate PDF");
        viewBtn.addActionListener(e -> {
            try {
                CertificateService service = new CertificateService();
                Certificate cert = service.getOrGenerateCertificate(student, course);
                ta.setText(buildCertificateText()); // update text area in case generated
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage());
            }
        });

        setLayout(new BorderLayout(10,10));
        add(new JScrollPane(ta), BorderLayout.CENTER);
        add(viewBtn, BorderLayout.SOUTH);
    }

    // Build the text representation for JTextArea
    private String buildCertificateText() {
        for (Certificate cert : student.getCertificates()) {
            if (cert.getCourseId().equals(course.getCourseId())) {
                return "Certificate ID: " + cert.getCertificateId() +
                        "\nStudent ID: " + cert.getStudentId() +
                        "\nStudent Email: " + cert.getStudentEmail() +
                        "\nCourse ID: " + cert.getCourseId() +
                        "\nMarks: " + cert.getTotalMarks() +
                        "\nIssued At: " + cert.getIssuedAt();
            }
        }
        return "Certificate not generated yet.\nClick 'View Certificate PDF' to generate it.";
    }
}
