package model.dto;

public class InstructorDto {
    int     instructorId;
    String  name;
    int     age;

    public InstructorDto(int instructorId, String name, int age) {
        this.instructorId = instructorId;
        this.name = name;
        this.age = age;
    }

    public InstructorDto() {}

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
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
}
