package gui;

import models.*;
import javax.swing.*;
import java.awt.*;

public class CertificateFrame extends JFrame {
    public CertificateFrame(Certificate cert) {
        setTitle("Certificate "+cert.getCertificateId());
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setText("Certificate ID: "+cert.getCertificateId()+"\nStudent ID: "+cert.getStudentEmail()+"\nCourse ID: "+cert.getCourseId()+"\nIssued at: "+cert.getIssuedAt());
        add(new JScrollPane(ta));
        setSize(400,300);
        setLocationRelativeTo(null);
    }
}
