public class Module {
    private int marks;

    public Module(int marks) {
        this.marks = marks;
    }

    public int getMarks() {
        return marks;
    }

    @Override
    public String toString() {
        return String.valueOf(marks);
    }
}
