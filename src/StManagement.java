import java.io.*;
import java.util.*;

public class StManagement {
    private static final int maxStudents = 100; //maximum students
    private static Student[] students = new Student[maxStudents]; //array to store students
    private static int studentCount = 0;

    public static void main(String[] args) {
        StManagement management = new StManagement();
        management.run(); //start program
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            printMenu();//display main menu
            int choice = 0;
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 0 - 10!!");
                sc.nextLine();
                continue;
            }
            sc.nextLine();
            try {
                switch (choice) {
                    case 1:
                        checkAvailableSeats();//method to available student count for register
                        break;
                    case 2:
                        registerStudent(sc); //register students
                        break;
                    case 3:
                        removeStudent(); //remove student
                        break;
                    case 4:
                        findStudent(); //find student by Id or name
                        break;
                    case 5:
                        storeStudentDetails(); //store student details to a file
                        break;
                    case 6:
                        loadStudentDetails(); // load student details from file
                        break;
                    case 7:
                        viewSortedStudents(); // view student details using bubble sort
                        break;
                    case 8:
                        additionalControls(sc); //additional controls for updates
                        break;
                    case 9:
                        studentSummary(); //display summary
                        break;
                    case 10:
                        studentReport(); // generate a report
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice, please try again!!");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
    private void printMenu() { //main menu option
        System.out.println("\n--- Student Management System ---");
        System.out.println("1. Check available seats");
        System.out.println("2. Register student");
        System.out.println("3. Delete student");
        System.out.println("4. Find student");
        System.out.println("5. Store student details into a file");
        System.out.println("6. Load student details from a file");
        System.out.println("7. View list of students sorted by name");
        System.out.println("8. Additional controls (Update Name/Marks)");
        System.out.println("9. Generate summary");
        System.out.println("10. Generate complete report");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private void checkAvailableSeats() { //method to chech avalibility of seats
        int registeredStudents = getRegisteredStudentCountFromFile(); //get the count of students who are register from file
        System.out.println("These are " + (maxStudents - registeredStudents) + " seats available."); //calculate the number
    }

    private int getRegisteredStudentCountFromFile() { //method to get registerd count of students from file
        try (BufferedReader br = new BufferedReader(new FileReader("st.txt"))) {
            String countStr = br.readLine();
            return countStr != null ? Integer.parseInt(countStr) : 0; //if file empty return 0
        } catch (IOException e) {
            return 0;
        }
    }

    private void registerStudent(Scanner sc) { //method for register student details
        while (true) {
            if (lastIndexUsed() >= maxStudents) { //check for registration availability
                System.out.println("No more seats available. Cannot register new students!!");
                return;
            }
            String studentID = getStudentId(sc); //get student Id
            String name = getStudentName(sc); //get student name
            int marks1 = getStudentMarks(sc, "SD2"); //get the marks
            int marks2 = getStudentMarks(sc, "WDD");
            int marks3 = getStudentMarks(sc, "TCS");
            students[studentCount++] = new Student(studentID, name, marks1, marks2, marks3); //creat a student obj with the inputted data
            System.out.println("Student registered successfully.");
            if (!continuing(sc)) { //ask if want to continue registering
                break;
            }
        }
    }

    private static String getStudentId(Scanner sc) { //method for student Id
        String id;
        do {
            System.out.print("Enter student ID (w1234567): "); //get the student Id
            id = sc.next();
            if (!isValidStudentId(id)) { //check id validation
                System.out.println("Invalid ID format. ID must start with 'w' and have 8 characters in total!!");
            } else if (isDuplicateStudentId(id)) { //check for duplicate Id
                System.out.println("Error: Duplicate ID found. Please enter a unique ID!!");
            }
        } while (!isValidStudentId(id) || isDuplicateStudentId(id)); //ask util entered write
        sc.nextLine();
        return id;
    }

    private static String getStudentName(Scanner sc) { //method for student name
        String rawName;
        do {
            System.out.print("Enter student name: "); //get student name
            rawName = sc.nextLine();
            if (!rawName.matches("[a-zA-Z ]+")) { //check for alphabetical characters and spaces
                System.out.println("Name can only contain alphabetical characters and spaces!!");
                continue;
            }
            String formattedName = namingFormat(rawName);
            if (hasDuplicateStudentNames(formattedName)) { // check for duplicate name
                System.out.println("Error: Name already exists. Please enter a unique name!!");
            } else {
                return formattedName;
            }
        } while (true);
    }

    private static int getStudentMarks(Scanner sc, String moduleName) { //module for student marks
        int marks;
        while (true) {
            try {
                System.out.print("Enter marks for " + moduleName + ": "); // get student marks
                marks = sc.nextInt();
                sc.nextLine();
                if (marks < 0 || marks > 100) { //check mark in valid range
                    throw new IllegalArgumentException("Marks should be between 0 and 100!!");
                }
                return marks;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer value for marks!!");
                sc.nextLine(); // Consume the invalid input
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage()+" !!");
            }
        }
    }

    private static boolean isValidStudentId(String id) { //method for Id validation
        if (id.length() != 8 || Character.toLowerCase(id.charAt(0)) != 'w') {//checking for 'w' and altogether 8 characters
            return false;
        }
        try {
            Integer.parseInt(id.substring(1));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isDuplicateStudentId(String id) { //method for duplicate Id
        for (int i = 0; i < lastIndexUsed(); i++) { //iterate through registered students
            if (students[i] != null && students[i].getStudentID().equals(id)) { //check if student obj is null and if it's matching to the Id
                return true;
            }
        }
        return false;
    }

    private static boolean hasDuplicateStudentNames(String name) { //method for duplicate name
        for (int i = 0; i < lastIndexUsed(); i++) { //iterate throughh registerd students
            if (students[i] != null && students[i].getName().equalsIgnoreCase(name)) { //check idf matches  with ignore case
                return true;
            }
        }
        return false;
    }

    private static String namingFormat(String name) { // method for capitalize first letter of each part od student name
        StringBuilder formattedName = new StringBuilder();
        String[] nameParts = name.trim().split("\\s+"); // split name using whitspaces
        for (String part : nameParts) { //iterate through the name
            if (part.length() > 0) {
                formattedName.append(Character.toUpperCase(part.charAt(0))) // capitalize fist letter and simple others
                        .append(part.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return formattedName.toString().trim();
    }

    private static int lastIndexUsed() { //method to get the last used student count/index
        return studentCount;
    }

    private static void removeStudent() { //method for to remove student
        Scanner sc = new Scanner(System.in);
        if (lastIndexUsed() == 0) {
            System.out.println("No students have been registered to remove!");
            return;
        }
        while (true) {
            try {
                view(); //display lst of students
                if (lastIndexUsed() > 0) {
                    System.out.println("1. Remove by index.");
                    System.out.println("2. Remove by ID.");
                    System.out.println("3. Remove by name.");
                } else {
                    System.out.println("Please run the program again to register students!!");
                    return;
                }
                System.out.print("Enter remove method: ");
                int choice = sc.nextInt(); //ask which method
                sc.nextLine();
                switch (choice) {
                    case 1:
                        removeByIndex(sc);
                        break;
                    case 2:
                        removeByID(sc);
                        break;
                    case 3:
                        removeByName(sc);
                        break;
                    default:
                        System.out.println("Please enter a choice between 1-3!");
                        continue;
                }
                if (!continuing(sc)) { //ask if continuing
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Only numerals 1-3 are allowed for remove methods!!");
                sc.nextLine();
            }
        }
    }

    private static void view() { //method to view list of students
        if (lastIndexUsed() == 0) {
            System.out.println("No students have been registered!");
            return;
        }
        System.out.println("Current Student Registrations");
        for (int i = 0; i < lastIndexUsed(); i++) {
            if (students[i] != null) {
                System.out.println("Index: " + (i + 1) + ". ID: " + students[i].getStudentID() + ", Name: " + students[i].getName());
            }
        }
    }

    private static boolean continuing(Scanner sc) { //method for cntinuing validation
        while (true) {
            try {
                System.out.print("Do you want to continue (yes/no): ");
                String choice = sc.nextLine().trim().toLowerCase();

                if (choice.equals("no")) {
                    return false;
                } else if (choice.equals("yes")) {
                    return true;
                } else {
                    System.out.println("Only 'yes' or 'no' are accepted!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Only 'yes' or 'no' are accepted!!");
                sc.nextLine();
            }
        }
    }

    private static void removeByIndex(Scanner sc) { //method for remove student by index
        while (true) {
            try {
                System.out.print("Enter student index to remove: ");
                int index = sc.nextInt() - 1; //adjust for zero index
                sc.nextLine();
                if (index >= 0 && index < lastIndexUsed()) { //check if withing valid range
                    String studentName = students[index].getName(); // get name of student to be removed
                    for (int i = index; i < lastIndexUsed() - 1; i++) { //shift the students in array to fill the removed students position
                        students[i] = students[i + 1];
                    }
                    students[--studentCount] = null; //decrease student count
                    System.out.println("Student " + studentName + " has been removed successfully.");
                    break;
                } else {
                    System.out.println("Invalid student index. Please enter a valid index!!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numerical index!!");
                sc.nextLine();
            }
        }
    }

    private static void removeByID(Scanner sc) { //method for removing student by Id
        while (true) {
            try {
                System.out.print("Enter student ID to remove: "); //get user Id to be removed
                String id = sc.nextLine().trim();
                boolean found = false;
                for (int i = 0; i < lastIndexUsed(); i++) { //iterate through the student list to find
                    if (students[i].getStudentID().equals(id)) {
                        String studentName = students[i].getName();
                        for (int j = i; j < lastIndexUsed() - 1; j++) { ////shift the students in array to fill the removed students position
                            students[j] = students[j + 1];
                        }
                        students[--studentCount] = null;// decrease the student count
                        found = true;
                        System.out.println("Student " + studentName + " has been removed successfully.");
                        break;
                    }
                }
                if (found) {
                    break;
                } else {
                    System.out.println("No student found with the given ID. Please enter a valid ID!!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid student ID!!");
                sc.nextLine();
            }
        }
    }

    private static void removeByName(Scanner sc) { //method to remove student by name
        while (true) {
            try {
                System.out.print("Enter student name to remove: ");
                String name= sc.nextLine().trim();
                boolean found = false;
                for (int i = 0; i < lastIndexUsed(); i++) {
                    if (students[i].getName().equalsIgnoreCase(name)) {
                        String studentName = students[i].getName();
                        for (int j = i; j < lastIndexUsed() - 1; j++) {
                            students[j] = students[j + 1];
                        }
                        students[--studentCount] = null;
                        found = true;
                        System.out.println("Student " + studentName + " has been removed successfully.");
                        break;
                    }
                }
                if (found) {
                    break;
                } else {
                    System.out.println("No student found with the given name. Please enter a valid name!!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid student name!!");
                sc.nextLine();
            }
        }
    }

    private static void findStudent() { //method to find student id or name
        Scanner sc = new Scanner(System.in);
        if (lastIndexUsed() == 0) { //check for any student registered
            System.out.println("No students registered.");
            return;
        }
        while (true) {
            System.out.println("Find student by:");
            System.out.println("1. Student ID");
            System.out.println("2. Student Name");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1: //search by student ID
                    System.out.print("Enter student ID or part of ID: ");
                    String partialId = sc.nextLine(); // get part or full Id
                    boolean idFound = false;
                    for (int i = 0; i < lastIndexUsed(); i++) { //iterate and finf for any matches
                        if (students[i] != null && students[i].getStudentID().contains(partialId)) {
                            if (!idFound) {
                                System.out.println("Matching students found:");
                            }
                            System.out.println("ID: " + students[i].getStudentID() + ", Name: " + students[i].getName());
                            idFound = true;
                        }
                    }
                    if (!idFound) {
                        System.out.println("No student found with the provided ID or part of ID!!");
                    }
                    break;
                case 2: //search by student name
                    System.out.print("Enter student name or part of name: ");
                    String partialName = sc.nextLine();
                    boolean nameFound = false;
                    for (int i = 0; i < lastIndexUsed(); i++) {
                        if (students[i] != null && students[i].getName().toLowerCase().contains(partialName.toLowerCase())) {
                            if (!nameFound) {
                                System.out.println("Matching students found:");
                            }
                            System.out.println("ID: " + students[i].getStudentID() + ", Name: " + students[i].getName());
                            nameFound = true;
                        }
                    }
                    if (!nameFound) {
                        System.out.println("No student found with the provided name or part of name!!");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1 or 2!!");
                    break;
            }
            if (!continuing(sc)) { //ask if continuing
                break;
            }
        }
    }

    private void storeStudentDetails() { //method for storing student details into a file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("st.txt"))) { //buffer reder method
            bw.write(String.valueOf(lastIndexUsed())); //write last index used to the file
            bw.newLine();
            for (int i = 0; i < lastIndexUsed(); i++) { //loop through each and write details to the file
                if (students[i] != null) {
                    bw.write(students[i].toString());//write as string
                    bw.newLine();
                }
            }
            System.out.println("Student details stored successfully.");
        } catch (IOException e) {
            System.out.println("Error storing student details: " + e.getMessage()+" !!");
        }
    }

    private void loadStudentDetails() { // method for loading the sorted student details from file
        int loadedCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("st.txt"))) {
            studentCount = Integer.parseInt(br.readLine()); // read first line which contains number of students
            for (int i = 0; i < studentCount; i++) { // iterate to read each student detail
                String line = br.readLine();
                if (line != null) {
                    Student student = Student.fromString(line); //convert to student obj
                    students[i] = student; //store student in a array
                    loadedCount++;
                }
            }
            System.out.println("Successfully loaded " + loadedCount + " student details.");
        } catch (IOException e) {
            System.out.println("Error loading student details: " + e.getMessage()+" !!");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage()+" !!");
        }
    }

    private void viewSortedStudents() { // method ro view bubble sorted student details
        bubbleSortByName(); //calling the bubble sort method foe alphabetical order
        if (lastIndexUsed() == 0) {
            System.out.println("There are currently no students registered!!\n");
            return;
        }
        System.out.println("\n--- Sorted Student Details ---");
        System.out.println("Student ID  |    Student Name    |   SD2 Marks   |   WDD Marks   |  TCS Marks  ");
        System.out.println("-------------------------------------------------------------------------------");
        for (int i = 0; i < lastIndexUsed(); i++) { //iterate through each and print details accordingly
            if (students[i] != null) {
                String idFormatted = " " + students[i].getStudentID() + "      ";
                String nameFormatted = " " + students[i].getName() + "              ";
                String sd2Formatted = " " + students[i].getMarks1() + "             ";
                String wddFormatted = " " + students[i].getMarks2() + "             ";
                String tcsFormatted = " " + students[i].getMarks3() + "             ";
                System.out.println(idFormatted + nameFormatted + sd2Formatted + wddFormatted + tcsFormatted);
            }
        }
    }

    private void additionalControls(Scanner sc) { //method for additional controls like updating
        boolean continueFlag = true; // to control the loop
        while (continueFlag) {
            try {
                System.out.println("1. Update student name");
                System.out.println("2. Uppdate module marks (1, 2, 3)");
                System.out.print("Enter your choice: ");
                char choice = sc.next().charAt(0); // read choice
                sc.nextLine();

                switch (choice) {
                    case '1':
                        updateStudentName(sc); //call method to update student name
                        break;
                    case '2':
                        updateModuleMarks(sc); // call method to update student module mark
                        break;
                    default:
                        System.out.println("Invalid choice, please try again!!");
                        continueFlag = continuing(sc); //check if want to continue to update
                        continue;
                }
                continueFlag = continuing(sc);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input type, please enter a valid choice!!");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage()+" !!");
            }
        }
    }

    private void updateStudentName(Scanner sc) { //method for update student name
    System.out.print("Enter Student ID to update: ");
    String studentID = sc.nextLine();
    boolean studentFound = false;
    for (int i = 0; i < lastIndexUsed(); i++) {
        if (students[i].getStudentID().equals(studentID)) { //check if student find
            studentFound = true;
            while (true) {
                System.out.print("Enter new Student Name: ");
                String name = sc.nextLine();
                if (hasDuplicateStudentNames(name)) { //check if new name duplicate name
                    System.out.println("Error: Name already exists. Please enter a unique name!!");
                } else {
                    students[i].setName(name); // set new
                    System.out.println("Student name updated successfully.");
                    storeStudentDetails(); // store updated name
                    break;
                }
            }
            break;
        }
    }if (!studentFound) {
            System.out.println("Student not found!!");
        }
    }

    private void updateModuleMarks(Scanner sc) { //method for update module marks
        System.out.print("Enter Student ID to update: ");
        String studentID = sc.nextLine();
        for (int i = 0; i < lastIndexUsed(); i++) {
            if (students[i].getStudentID().equals(studentID)) { //check if given id same as given
                System.out.print("Enter marks for SD2: "); //new marks
                int marks1 = sc.nextInt();
                System.out.print("Enter marks for WDD: ");
                int marks2 = sc.nextInt();
                System.out.print("Enter marks for TCS: ");
                int marks3 = sc.nextInt();
                sc.nextLine();
                students[i] = new Student(studentID, students[i].getName(), marks1, marks2, marks3);
                System.out.println("Student module marks updated successfully!");
                storeStudentDetails(); // store new marks
                return;
            }
        }
        System.out.println("Student not found!!");
    }

    private void studentSummary() { //method fto get summary of students marks
        if (lastIndexUsed() == 0) {
            System.out.println("No student registrations found. Please load student details!");
            return;
        }
        System.out.println("Total student registrations: " + lastIndexUsed()); // total student registered
        System.out.println();
        int module1Pass = 0, module2Pass = 0, module3Pass = 0; // intaialized count for each module
        for (int i = 0; i < lastIndexUsed(); i++) { // iterate throgh eack student and see who have scored  >= in each modeule
            if (students[i].getMarks1() >= 40) {
                module1Pass++;
            }
            if (students[i].getMarks2() >= 40) {
                module2Pass++;
            }
            if (students[i].getMarks3() >= 40) {
                module3Pass++;
            }
        }
        System.out.println("Total no of students who scored more than 40 marks in SD2: " + module1Pass);
        System.out.println("Total no of students who scored more than 40 marks in WDD: " + module2Pass);
        System.out.println("Total no of students who scored more than 40 marks in TCS: " + module3Pass);
    }



    private void studentReport() { //method to get a report on all
        if (lastIndexUsed() == 0) {
            System.out.println("No student registrations found. Please load student details!");
            return;
        }
        bubbleSortByAverageMarks(); //calling the to store students by their avarage marks in descending order
        System.out.println("\n--- Student Details Report ---");
        System.out.println("Student ID  |    Student Name    |   SD2 Marks   |   WDD Marks   |  TCS Marks  | Average Marks |     Grade    ");
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < lastIndexUsed(); i++) { //iterate through each student details
            if (students[i] != null) {
                String idFormatted = " " + students[i].getStudentID() + "      ";
                String nameFormatted = " " + students[i].getName() + "               ";
                String sd2Formatted = " " + students[i].getMarks1() + "              ";
                String wddFormatted = " " + students[i].getMarks2() + "              ";
                String tcsFormatted = " " + students[i].getMarks3() + "              ";
                String avgMarksFormatted = " "+ students[i].getAverageMarks() + "                ";
                String gradeFormatted = " " + students[i].getGrade() + "                    ";
                System.out.println(idFormatted + nameFormatted + sd2Formatted + wddFormatted + tcsFormatted + avgMarksFormatted + gradeFormatted);
            }
        }
    }

    private void bubbleSortByName() { //method to store name on alphabetical order using bubble sort
        for (int i = 0; i < lastIndexUsed() - 1; i++) { //oter loop to control the number od passes
            for (int j = 0; j < lastIndexUsed() - i - 1; j++) { //inner loop to compare elemnts
                if (students[j].getName().compareTo(students[j + 1].getName()) > 0) { //compare names of students
                    Student temp = students[j];
                    students[j] = students[j + 1]; // swap the current student with the next student
                    students[j + 1] = temp;
                }
            }
        }
    }

    private void bubbleSortByAverageMarks() { //method to sort students by average marks in descending order using bubble sort
        for (int i = 0; i < lastIndexUsed() - 1; i++) { // outer loop to control the numver of passes
            for (int j = 0; j < lastIndexUsed() - i - 1; j++) { // inner ;oop to compare elements
                if (students[j].getAverageMarks() < students[j + 1].getAverageMarks()) {
                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;
                }
            }
        }
    }
}
