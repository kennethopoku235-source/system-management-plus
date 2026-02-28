package service;

public class ValidationService {

    /**
     * Validates student name: 2-60 characters, letters and spaces only.
     * Must be static to be called as ValidationService.isValidName()
     */
    public static boolean isValidName(String name) {
        if (name == null) return false;
        // Regex: Only letters and spaces, length 2 to 60
        return name.matches("^[a-zA-Z\\s]{2,60}$");
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidGPA(double gpa) {
        return gpa >= 0.0 && gpa <= 4.0;
    }

    public static boolean isValidLevel(int level) {
        // Project requires specific levels
        return level == 100 || level == 200 || level == 300 || level == 400 ||
                level == 500 || level == 600 || level == 700;
    }
}