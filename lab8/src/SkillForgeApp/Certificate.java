
package SkillForgeApp;
import java.util.Date;

public class Certificate {
    String studentName;
    String courseName;
    Date dateEarned;

    public Certificate(String sName, String cName) {
        this.studentName = sName;
        this.courseName = cName;
        this.dateEarned = new Date();
    }

    @Override
    public String toString() {
        return "CERTIFICATE: " + studentName + " completed " + courseName + " on " + dateEarned;
    }
}