
import java.util.ArrayList;
import java.util.Scanner;

class Student {

    private int studentId;
    private String name;
    private ArrayList<Double> grades;

    public Student(int studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Double> getGrades() {
        return grades;
    }

    public void addGrade(double marks) {
        grades.add(marks);
    }

    public double calculateAverage() {
        double total = 0;

        for (double marks : grades) {
            total += marks;
        }

        return total / grades.size();
    }

    public double getHighestGrade() {
        double highest = grades.get(0);

        for (double marks : grades) {
            if (marks > highest) {
                highest = marks;
            }
        }

        return highest;
    }

    public double getLowestGrade() {
        double lowest = grades.get(0);

        for (double marks : grades) {
            if (marks < lowest) {
                lowest = marks;
            }
        }

        return lowest;
    }

    public void displayStudentDetails() {

        System.out.println("\n-----------------------------------");
        System.out.println("Student ID: " + studentId);
        System.out.println("Student Name: " + name);
        System.out.println("Subject Marks: " + grades);

        System.out.printf("Average Marks: %.2f%n", calculateAverage());
        System.out.printf("Highest Marks: %.2f%n", getHighestGrade());
        System.out.printf("Lowest Marks: %.2f%n", getLowestGrade());

        System.out.println("-----------------------------------");
    }
}

public class StudentGradeTracker {

    private static Scanner sc = new Scanner(System.in);

    private static ArrayList<Student> students = new ArrayList<>();

    private static int nextStudentId = 101;

    public static void main(String[] args) {

        int choice;

        System.out.println("======================================");
        System.out.println("        STUDENT GRADE TRACKER");
        System.out.println("======================================");

        do {

            System.out.println("\n1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Display Class Summary");
            System.out.println("5. Exit");

            System.out.print("\nEnter your choice: ");

            choice = readInt();

            switch (choice) {

                case 1:
                    addStudent();
                    break;

                case 2:
                    viewAllStudents();
                    break;

                case 3:
                    searchStudent();
                    break;

                case 4:
                    displayClassSummary();
                    break;

                case 5:
                    System.out.println("Thank you for using Student Grade Tracker!");
                    break;

                default:
                    System.out.println("Invalid choice! Please select 1-5.");
            }

        } while (choice != 5);

        sc.close();
    }

    private static int readInt() {

        while (!sc.hasNextInt()) {
            System.out.print("Invalid input! Enter a whole number: ");
            sc.next();
        }

        return sc.nextInt();
    }

    private static void addStudent() {

        sc.nextLine();

        System.out.print("\nEnter student name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Student name cannot be empty.");
            return;
        }

        System.out.print("Enter number of subjects: ");
        int numberOfSubjects = readInt();

        if (numberOfSubjects <= 0) {
            System.out.println("Number of subjects must be positive.");
            return;
        }

        Student student = new Student(nextStudentId, name);

        for (int i = 1; i <= numberOfSubjects; i++) {

            double marks;

            while (true) {

                System.out.print("Enter marks for Subject " + i + " (0-100): ");

                if (!sc.hasNextDouble()) {
                    System.out.println("Invalid input! Enter numeric marks.");
                    sc.next();
                    continue;
                }

                marks = sc.nextDouble();

                if (marks >= 0 && marks <= 100) {
                    break;
                }

                System.out.println("Marks must be between 0 and 100.");
            }

            student.addGrade(marks);
        }

        students.add(student);

        System.out.println("\nStudent added successfully!");
        System.out.println("Student ID: " + nextStudentId);

        nextStudentId++;
    }

    private static void viewAllStudents() {

        if (students.isEmpty()) {
            System.out.println("\nNo student records available.");
            return;
        }

        System.out.println("\n========== ALL STUDENT RECORDS ==========");

        for (Student student : students) {
            student.displayStudentDetails();
        }
    }

    private static void searchStudent() {

        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        System.out.print("\nEnter Student ID: ");
        int id = readInt();

        for (Student student : students) {

            if (student.getStudentId() == id) {
                System.out.println("\nStudent Found!");
                student.displayStudentDetails();
                return;
            }
        }

        System.out.println("Student not found!");
    }

    private static void displayClassSummary() {

        if (students.isEmpty()) {
            System.out.println("\nNo student records available.");
            return;
        }

        double totalAverage = 0;
        double highestMarks = 0;
        double lowestMarks = 100;

        Student topper = null;

        System.out.println("\n========== CLASS SUMMARY ==========");

        System.out.printf("%-10s %-20s %-12s%n",
                "ID", "Student Name", "Average");

        for (Student student : students) {

            double average = student.calculateAverage();

            totalAverage += average;

            if (average > highestMarks) {
                highestMarks = average;
                topper = student;
            }

            if (student.getLowestGrade() < lowestMarks) {
                lowestMarks = student.getLowestGrade();
            }

            System.out.printf("%-10d %-20s %.2f%%%n",
                    student.getStudentId(),
                    student.getName(),
                    average);
        }

        double classAverage = totalAverage / students.size();

        System.out.println("\n---------- PERFORMANCE REPORT ----------");

        System.out.println("Total Students: " + students.size());

        System.out.printf("Class Average: %.2f%%%n", classAverage);

        System.out.printf("Highest Individual Subject Mark: %.2f%n",
                getOverallHighestMarks());

        System.out.printf("Lowest Individual Subject Mark: %.2f%n",
                getOverallLowestMarks());

        System.out.println("Class Topper: " + topper.getName());

        System.out.println("----------------------------------------");
    }

    private static double getOverallHighestMarks() {

        double highest = 0;

        for (Student student : students) {
            if (student.getHighestGrade() > highest) {
                highest = student.getHighestGrade();
            }
        }

        return highest;
    }

    private static double getOverallLowestMarks() {

        double lowest = 100;

        for (Student student : students) {
            if (student.getLowestGrade() < lowest) {
                lowest = student.getLowestGrade();
            }
        }

        return lowest;
    }
}
