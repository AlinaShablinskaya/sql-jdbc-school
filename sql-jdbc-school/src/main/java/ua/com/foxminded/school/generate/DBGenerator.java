package ua.com.foxminded.school.generate;

import java.util.List;

import ua.com.foxminded.school.domain.Course;
import ua.com.foxminded.school.domain.Group;
import ua.com.foxminded.school.domain.Student;

public interface DBGenerator {
    List<Group> generateGroups();
    
    List<Course> generateCourses();
    
    List<Student> generateStudents(List<Group> groups, List<Course> course);
}
