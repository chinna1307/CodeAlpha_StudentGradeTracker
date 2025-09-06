import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Student {
    private String name;
    private double grade;

    public Student(String name, double grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public double getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Grade: " + String.format("%.2f", grade);
    }
}

public class StudentGradeTracker extends JFrame {

    private final ArrayList<Student> studentList;
    private final JTextField nameField;
    private final JTextField gradeField;
    private final JTextArea displayArea;
    private final JButton addButton;
    private final JButton showSummaryButton;

    public StudentGradeTracker() {
        studentList = new ArrayList<>();

        setTitle("Student Grade Tracker");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Marks (0-100):"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);

        addButton = new JButton("Add Student");
        styleButton(addButton);
        inputPanel.add(addButton);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        showSummaryButton = new JButton("Show Summary Report");
        styleButton(showSummaryButton);
        buttonPanel.add(showSummaryButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        displayArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        showSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSummary();
            }
        });

        displayArea.setText("Welcome! Add students to get started.\n\n--- Students List ---\n");
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        String gradeText = gradeField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double grade;
        try {
            grade = Double.parseDouble(gradeText);
            if (grade < 0 || grade > 100) {
                JOptionPane.showMessageDialog(this, "Grade must be between 0 and 100.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the grade.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student newStudent = new Student(name, grade);
        studentList.add(newStudent);

        updateStudentListDisplay();

        nameField.setText("");
        gradeField.setText("");
        nameField.requestFocus();
    }

    private void updateStudentListDisplay() {
        StringBuilder sb = new StringBuilder("--- Students List ---\n");
        if (studentList.isEmpty()) {
            sb.append("No students added yet.\n");
        } else {
            for (Student student : studentList) {
                sb.append(String.format("- %-20s Marks(0-100): %6.2f%n", student.getName(), student.getGrade()));
            }
        }
        displayArea.setText(sb.toString());
    }

    private void showSummary() {
        if (studentList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students to summarize. Please add at least one student.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double sum = 0;
        Student highestScorer = Collections.max(studentList, Comparator.comparing(Student::getGrade));
        Student lowestScorer = Collections.min(studentList, Comparator.comparing(Student::getGrade));

        for (Student student : studentList) {
            sum += student.getGrade();
        }

        double average = sum / studentList.size();

        JDialog summaryDialog = new JDialog(this, "Grade Summary Report", true);
        summaryDialog.setSize(400, 300);
        summaryDialog.setLocationRelativeTo(this);
        summaryDialog.setLayout(new BorderLayout(10, 10));

        JTextArea summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        summaryArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        StringBuilder summary = new StringBuilder("--- Grade Summary Report ---\n");
        summary.append(String.format("Total Students: %d%n", studentList.size()));
        summary.append(String.format("Average Grade: %.2f%n", average));
        summary.append(String.format("Highest Grade: %.2f (Scored by: %s)%n", highestScorer.getGrade(), highestScorer.getName()));
        summary.append(String.format("Lowest Grade: %.2f (Scored by: %s)%n", lowestScorer.getGrade(), lowestScorer.getName()));
        summary.append("\n--- All Students ---\n");
        summary.append(String.format("%-20s %-10s%n", "Name", "Grade"));
        summary.append("--------------------------------\n");
        for (Student student : studentList) {
            summary.append(String.format("%-20s %-10.2f%n", student.getName(), student.getGrade()));
        }

        summaryArea.setText(summary.toString());
        JScrollPane summaryScrollPane = new JScrollPane(summaryArea);

        JButton closeButton = new JButton("Close");
        styleButton(closeButton);
        closeButton.addActionListener(e -> summaryDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);

        summaryDialog.add(summaryScrollPane, BorderLayout.CENTER);
        summaryDialog.add(buttonPanel, BorderLayout.SOUTH);
        summaryDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentGradeTracker().setVisible(true);
            }
        });
    }
}
