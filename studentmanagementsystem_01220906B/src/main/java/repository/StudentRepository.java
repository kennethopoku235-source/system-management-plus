package repository;

import domain.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    public StudentRepository() { createTables(); }

    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                "studentId TEXT PRIMARY KEY, fullName TEXT NOT NULL, email TEXT UNIQUE, " +
                "phoneNumber TEXT, programme TEXT, level INTEGER, gpa REAL, " +
                "status TEXT, dateAdded TEXT, lastUpdatedBy TEXT)";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            stmt.execute("CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)");
            stmt.execute("INSERT OR IGNORE INTO users VALUES('admin', 'admin123')");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void saveStudent(Student s, String admin) throws SQLException {
        String sql = "INSERT INTO students VALUES (?,?,?,?,?,?,?,?,datetime('now'),?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, s.getStudentId());
            pstmt.setString(2, s.getFullName());
            pstmt.setString(3, s.getEmail());
            pstmt.setString(4, s.getPhoneNumber());
            pstmt.setString(5, s.getProgramme());
            pstmt.setInt(6, s.getLevel());
            pstmt.setDouble(7, s.getGpa());
            pstmt.setString(8, s.getStatus());
            pstmt.setString(9, admin);
            pstmt.executeUpdate();
        }
    }

    public List<Student> findByCriteria(String q, String type, String sort, double threshold) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE (studentId LIKE ? OR fullName LIKE ?)";
        if ("TOP".equals(type)) sql += " AND gpa >= 3.5";
        else if ("RISK".equals(type)) sql += " AND gpa <= " + threshold;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + q + "%");
            pstmt.setString(2, "%" + q + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Student s = new Student();
                s.setStudentId(rs.getString("studentId"));
                s.setFullName(rs.getString("fullName"));
                s.setGpa(rs.getDouble("gpa"));
                list.add(s);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean validateUser(String u, String p) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT 1 FROM users WHERE username=? AND password=?")) {
            pstmt.setString(1, u); pstmt.setString(2, p);
            return pstmt.executeQuery().next();
        } catch (SQLException e) { return false; }
    }
}