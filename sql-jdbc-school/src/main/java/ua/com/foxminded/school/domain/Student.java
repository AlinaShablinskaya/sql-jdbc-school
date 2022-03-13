package ua.com.foxminded.school.domain;

import java.util.List;
import java.util.Objects;

public class Student {
    private static final String SPACE = " ";
    private static final String NEW_LINE = "\n";

    private final Integer studentId;
    private final Integer groupId;
    private final String firstName;
    private final String lastName;
    private final List<Course> courses;

    private Student(Builder builder) {
        this.studentId = builder.studentId;
        this.groupId = builder.groupId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.courses = builder.courses;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getStudentId() {
        return studentId;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, groupId, firstName, lastName, courses);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Student other = (Student) obj;
        return Objects.equals(studentId, other.studentId) &&
                Objects.equals(groupId, other.groupId) && 
                Objects.equals(firstName, other.firstName) && 
                Objects.equals(lastName, other.lastName)&& 
                Objects.equals(courses, other.courses);
    }

    @Override
    public String toString() {
        return studentId + SPACE + groupId + SPACE + firstName + SPACE + lastName + NEW_LINE;
    }

    public static class Builder {
        private Integer studentId;
        private Integer groupId;
        private String firstName;
        private String lastName;
        private List<Course> courses;

        public Builder withStudentId(Integer studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder withGroupId(Integer groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withCourses(List<Course> courses) {
            this.courses = courses;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }
}
