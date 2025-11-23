package services;

import data.JsonDatabaseManager;
import models.*;

import java.util.UUID;

public class CertificateService {
    private JsonDatabaseManager db = new JsonDatabaseManager();

    public CertificateService(){}

    // Generate certificate storing only email + minimal metadata
    public Certificate generateCertificate(Student s, Course c) {
        // certificate id: random UUID
        String certId = "CERT-" + UUID.randomUUID().toString().substring(0,8);
        Certificate cert = new Certificate(certId, s.getEmail(), c.getCourseId());
        // add to student's certificates
        s.getCertificates().add(cert);
        db.updateUser(s); // persist
        return cert;
    }
}
