package ua.com.foxminded.school.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.foxminded.school.dao.ConnectionFactory;
import ua.com.foxminded.school.dao.StudentDao;
import ua.com.foxminded.school.domain.Student;
import ua.com.foxminded.school.validator.DataBaseRuntimeException;

public class StudentDaoImpl extends AbstractCrudDaoImpl<Student> implements StudentDao{
    private static final String SAVE_QUERY = "INSERT INTO school.students (student_id, group_id, first_name, last_name) VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM school.students WHERE student_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM school.students limit ? offset ?;";
    private static final String UPDATE_QUERY = "UPDATE school.students SET group_id = ?, first_name=?, " +
            "last_name=? WHERE student_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM school.students WHERE student_id=?";

        public StudentDaoImpl(ConnectionFactory connector) {
            super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY,
                    UPDATE_QUERY, DELETE_BY_ID_QUERY);
        }
    
    public static final String CREATE_STUDENTS = 
            "INSERT INTO school.students (student_id, group_id, first_name, last_name) VALUES (?, ?, ?, ?)";
    public static final String CREATE_STUDENTS_COURSES = 
            "INSERT INTO school.students_courses (student_id, course_id) VALUES (?, ?)";
    public static final String SELECT_ALL__STUDENTS_COURSES = 
            "SELECT * FROM school.students_courses";
    public static final String DELETE_STUDENT_FROM_STUDENTS_COURSES = 
            "DELETE FROM school.students_courses WHERE student_id = ?";
    public static final String DELETE_STUDENT_FROM_STUDENTS = 
            "DELETE FROM school.students WHERE student_id = ?";
    public static final String DELETE_STUDENT_FROM_COURSE = 
            "DELETE FROM school.students_courses WHERE student_id = ? AND course_id = ?";
    public static final String FIND_ALL_TO_COURSE_NAME = "SELECT * FROM school.students WHERE student_id IN"
            + "(SELECT student_id FROM school.students_courses WHERE course_id IN "
            + "(SELECT course_id FROM school.courses WHERE course_name = ?)) ORDER BY student_id";
      
    @Override
    public void assignStudentsToCourses(List<Student> student) throws DataBaseRuntimeException {
        try(Connection connection = connector.connect();
                PreparedStatement statement = connection.prepareStatement(CREATE_STUDENTS_COURSES)) {
                    for (Student students : student) {
                        for (Map.Entry<Integer, Integer> entry : placeInTheTable(students).entrySet()) {
                            int courseId = entry.getKey();
                            int studentId = entry.getValue();
                            
                            statement.setInt(1, studentId);
                            statement.setInt(2, courseId);
                            statement.executeUpdate();
                        }
                    }
        } catch (SQLException throwables) {
            throw new DataBaseRuntimeException("Can't assign students to the courses.");
        }
    }
    
    @Override
    public List<Student> findAllByCourseName(String courseName) {
        return findAllByStringParam(courseName, FIND_ALL_TO_COURSE_NAME);
    }

    @Override
    public void addToTheCourse(int studentId, int courseId) {
        try (Connection connection = connector.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(CREATE_STUDENTS_COURSES)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseRuntimeException("Can't add student to the course.");
        }
    }
    
    @Override
    public void removeFromCourse(int studentId, int courseId) throws DataBaseRuntimeException {
        try (Connection connection = connector.connect();
                PreparedStatement statement = connection.prepareStatement(DELETE_STUDENT_FROM_COURSE)) { 
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DataBaseRuntimeException("Can't remove student from course.");
        }
    }
    
    private Map<Integer, Integer> placeInTheTable(Student students) {
        Map<Integer, Integer> result = new HashMap<>();
        
        for (int i = 0; i < students.getCourses().size(); i++) {
            int courseId = students.getCourses().get(i).getCourseId();
            result.put(courseId, students.getStudentId());
        }
        return result;
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Student students) throws SQLException {
        preparedStatement.setInt(1, students.getStudentId());
        preparedStatement.setInt(2, students.getGroupId());
        preparedStatement.setString(3, students.getFirstName());
        preparedStatement.setString(4, students.getLastName());   
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, Student students) throws SQLException {
        preparedStatement.setInt(1, students.getGroupId());
        preparedStatement.setString(2, students.getFirstName());
        preparedStatement.setString(3, students.getLastName());
        preparedStatement.setInt(4, students.getStudentId());   
    }

    @Override
    protected Student mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Student.Builder()
                .withStudentId(resultSet.getInt("student_id"))
                .withGroupId(resultSet.getInt("group_id"))
                .withFirstName(resultSet.getString("first_name"))
                .withLastName(resultSet.getString("last_name"))
                .build();
    }
}
