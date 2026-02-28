package service;

import domain.Student;
import repository.StudentRepository;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class StudentService {
    private StudentRepository repository = new StudentRepository();
    private static final String DATA_FOLDER = "data/";

    public boolean login(String u, String p) { return repository.validateUser(u, p); }

    public void validateForUI(Student s) throws Exception {
        if (!s.getFullName().matches("^[a-zA-Z\\s]{2,60}$")) throw new Exception("Name: 2-60 letters only.");
        if (s.getGpa() < 0 || s.getGpa() > 4.0) throw new Exception("GPA: Must be 0.0 - 4.0.");
        if (!s.getEmail().contains("@")) throw new Exception("Email: Invalid format.");
    }

    public List<Student> getFiltered(String q, String type, String sort, double threshold) {
        return repository.findByCriteria(q, type, sort, threshold);
    }

    public void registerStudent(Student s, String admin) throws Exception { repository.saveStudent(s, admin); }

    public String importFromCSV(String path, String admin) throws Exception {
        StringBuilder report = new StringBuilder("Import Result:\n");
        int success = 0, fail = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line; br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                try {
                    String[] d = line.split(",");
                    Student s = new Student();
                    s.setStudentId(d[0].trim());
                    s.setFullName(d[1].trim());
                    s.setEmail(d[2].trim());
                    s.setGpa(Double.parseDouble(d[3].trim()));
                    s.setStatus("Active");
                    repository.saveStudent(s, admin);
                    success++;
                } catch (Exception e) {
                    fail++;
                    report.append("Row Error: ").append(e.getMessage()).append("\n");
                }
            }
        }
        String finalReport = report.append("\nSuccess: ").append(success).append("\nFailed: ").append(fail).toString();
        Files.createDirectories(Paths.get(DATA_FOLDER));
        try (PrintWriter pw = new PrintWriter(DATA_FOLDER + "import_log.txt")) { pw.println(finalReport); }
        return finalReport;
    }
}