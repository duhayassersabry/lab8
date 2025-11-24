package services;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.awt.Desktop;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.PdfWriter;

import data.JsonDatabaseManager;
import models.*;

public class CertificateService {
    private JsonDatabaseManager db = new JsonDatabaseManager();

    // Generate certificate if not already generated
    public Certificate getOrGenerateCertificate(Student s, Course c) throws Exception {
        // Check if certificate already exists
        for (Certificate cert : s.getCertificates()) {
            if (cert.getCourseId().equals(c.getCourseId())) {
                return cert; // already exists
            }
        }

        // If not, generate new certificate
        int total = 0;
        int count = 0;
        for (Lesson L : c.getLessons()) {
            int best = 0;
            List<QuizAttempt> attempts = s.getAttempts().getOrDefault(c.getCourseId(), new ArrayList<>());
            for (QuizAttempt a : attempts)
                if (a.getLessonTitle().equals(L.getTitle()))
                    best = Math.max(best, a.getScore());
            total += best;
            count++;
        }

        int average = count == 0 ? 0 : total / count;
        String certId = "CERT-" + UUID.randomUUID().toString().substring(0, 8);
        Certificate cert = new Certificate(certId, s.getEmail(), s.getId(), c.getCourseId(), average);
        s.getCertificates().add(cert);
        db.updateUser(s);

        // Export PDF immediately
        exportCertificatePdf(cert, "certs/" + cert.getCertificateId() + ".pdf");

        return cert;
    }

    // Export certificate PDF and open
    public String exportCertificatePdf(Certificate cert, String outPath) throws Exception {
        File f = new File(outPath);
        if (!f.exists()) { // Only generate if file doesn't exist
            f.getParentFile().mkdirs();
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(f));
            doc.open();

            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 14);

            doc.add(new Paragraph("Certificate of Completion", titleFont));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Certificate ID: " + cert.getCertificateId(), normalFont));
            doc.add(new Paragraph("Student ID: " + cert.getStudentId(), normalFont));
            doc.add(new Paragraph("Student Email: " + cert.getStudentEmail(), normalFont));
            doc.add(new Paragraph("Course ID: " + cert.getCourseId(), normalFont));
            doc.add(new Paragraph("Total Marks: " + cert.getTotalMarks(), normalFont));
            doc.add(new Paragraph("Issued At: " + cert.getIssuedAt(), normalFont));

            doc.close();
        }

        // Open the PDF
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(f);
        }

        return f.getAbsolutePath();
    }
}
