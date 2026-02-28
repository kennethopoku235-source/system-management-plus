package domain;

public class Student {
    private String studentId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String programme;
    private int level;
    private double gpa;
    private String status;
    private String dateAdded;

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getProgramme() { return programme; }
    public void setProgramme(String programme) { this.programme = programme; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDateAdded() { return dateAdded; }
    public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }
}