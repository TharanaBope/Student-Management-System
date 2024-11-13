public class Student {
    private String studentID;
    private String name;
    private Module module1;
    private Module module2;
    private Module module3;

    public Student(String studentID, String name, int marks1, int marks2, int marks3) {
        this.studentID = studentID;
        this.name = name;
        this.module1 = new Module(marks1);
        this.module2 = new Module(marks2);
        this.module3 = new Module(marks3);
    }

    public String getStudentID() {
        return studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMarks1() {
        return module1.getMarks();
    }

    public int getMarks2() {
        return module2.getMarks();
    }

    public int getMarks3() {
        return module3.getMarks();
    }

    public double getAverageMarks() {
        double average = (module1.getMarks() + module2.getMarks() + module3.getMarks()) / 3.0;
        return Math.round(average * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return studentID + "," + name + "," + getMarks1() + "," + getMarks2() + "," + getMarks3();
    }
    public String getGrade() {
        double average = getAverageMarks();
        if (average >= 80) {
            return "Distinction";
        } else if (average >= 70) {
            return "Merit";
        } else if (average >= 40) {
            return "Pass";
        } else {
            return "Fail";
        }
    }
    public static Student fromString(String str) {
        String[] parts = str.split(",");
        if (parts.length == 5) {
            return new Student(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
        }
        return null;
    }
}
