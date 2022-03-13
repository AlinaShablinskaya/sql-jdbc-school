package ua.com.foxminded.school.dao;

import java.util.List;

import ua.com.foxminded.school.domain.Course;

public interface CourseDao extends CrudDao<Course, Integer> {
    List<Course> findAllByStudentId(int studentId);
}
