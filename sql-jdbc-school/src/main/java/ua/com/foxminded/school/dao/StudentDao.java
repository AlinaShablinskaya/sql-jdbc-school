package ua.com.foxminded.school.dao;

import java.util.List;

import ua.com.foxminded.school.domain.Student;

public interface StudentDao extends CrudDao<Student, Integer>{
    void assignStudentsToCourses(List<Student> student);
    
    List<Student> findAllByCourseName(String courseName);
    
    void addToTheCourse(int studentId, int courseId);
    
    void removeFromCourse(int studentId, int courseId);
}
