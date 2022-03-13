package ua.com.foxminded.school.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ua.com.foxminded.school.dao.CourseDao;
import ua.com.foxminded.school.domain.Course;
import ua.com.foxminded.school.dao.ConnectionFactory;

public class CourseDaoImpl extends AbstractCrudDaoImpl<Course> implements CourseDao {

    private static final String SAVE_QUERY = "INSERT INTO school.courses(course_id, course_name, course_description) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM school.courses WHERE course_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM school.courses limit ? offset ?;";
    private static final String UPDATE_QUERY = "UPDATE school.courses SET course_name = ? , course_description = ?"
            + " WHERE course_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM school.courses WHERE course_id=?";
    public static final String SELECT_ALL_COURSES = "SELECT * FROM school.courses";
    private static final String FIND_ALL_STUDENT_COURSES_QUERY = "SELECT * FROM school.courses WHERE course_id IN" +
            "(SELECT course_id FROM school.students_courses WHERE student_id = ?)";

    public CourseDaoImpl(ConnectionFactory connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<Course> findAllByStudentId(int studentId) {
        return findAllByIntParameter(studentId, FIND_ALL_STUDENT_COURSES_QUERY);
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Course courses) throws SQLException {
        preparedStatement.setInt(1, courses.getCourseId());
        preparedStatement.setString(2, courses.getCourseName());
        preparedStatement.setString(3, courses.getCourseDescription());
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, Course courses) throws SQLException {
        preparedStatement.setString(1, courses.getCourseName());
        preparedStatement.setString(2, courses.getCourseDescription());
        preparedStatement.setInt(3, courses.getCourseId());
    }

    @Override
    protected Course mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Course.Builder()
                .withCourseId(resultSet.getInt("course_id"))
                .withCourseName(resultSet.getString("course_name"))
                .withCourseDescription(resultSet.getString("course_description"))
                .build();        
    }
}
