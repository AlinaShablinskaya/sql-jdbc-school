package ua.com.foxminded.school.domain;

import java.util.Objects;

public class Course {
    private static final String SPACE = " ";
    private static final String NEW_LINE = "\n";
    
    private final Integer courseId;
    private final String courseName;
    private final String courseDescription;
    
    public Course(Builder builder) {
        this.courseId = builder.courseId;
        this.courseName = builder.courseName;
        this.courseDescription = builder.courseDescription;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public Integer getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, courseName, courseDescription);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Course other = (Course) obj;
        return Objects.equals(courseId, other.courseId) &&
               Objects.equals(courseName, other.courseName) &&
               Objects.equals(courseDescription, other.courseDescription);
    }

    @Override
    public String toString() {
        return courseId + SPACE + courseName + SPACE + courseDescription + NEW_LINE;
    }
    
    public static class Builder {
        private Integer courseId;
        private String courseName;
        private String courseDescription;
        
        public Builder withCourseId(Integer courseId) {
            this.courseId = courseId;
            return this;
        }
        
        public Builder withCourseName(String courseName) {
            this.courseName = courseName;
            return this;
        }
        
        public Builder withCourseDescription(String courseDescription) {
            this.courseDescription = courseDescription;
            return this;
        }
        
        public Course build() {
            return new Course(this);
        }
    }
}
