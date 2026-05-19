package model.dto;

public class StudentDto implements Comparable<StudentDto> {
    int     studentId;
    String  name;
    int     age;
    int     score;

    public StudentDto(int studentId, String name, int age, int score) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.score = score;
    }

    public StudentDto() {}

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(StudentDto o) {
        return Integer.compare(this.getScore(), o.getScore());
    }
}
