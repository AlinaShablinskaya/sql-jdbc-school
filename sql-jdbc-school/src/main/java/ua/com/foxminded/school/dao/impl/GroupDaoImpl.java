package ua.com.foxminded.school.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ua.com.foxminded.school.dao.ConnectionFactory;
import ua.com.foxminded.school.dao.GroupDao;
import ua.com.foxminded.school.domain.Group;

public class GroupDaoImpl extends AbstractCrudDaoImpl<Group> implements GroupDao {
    private static final String SAVE_QUERY = "INSERT INTO school.groups(group_id, group_name) VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM school.groups WHERE group_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM school.groups limit ? offset ?;";
    private static final String UPDATE_QUERY = "UPDATE school.groups SET group_name = ? WHERE group_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM school.groups WHERE group_id=?";
    private static final String FIND_ALL_BY_STUDENT_COUNT_QUERY = "SELECT * FROM school.groups WHERE group_id IN"
            + "(SELECT group_id FROM school.students GROUP BY group_id HAVING count(student_id)>=?)";

    public GroupDaoImpl(ConnectionFactory connector) {
            super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY,
                    UPDATE_QUERY, DELETE_BY_ID_QUERY);
        }

    @Override
    public List<Group> findAllByStudentCount(int count) {
        return findAllByIntParameter(count, FIND_ALL_BY_STUDENT_COUNT_QUERY);
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Group groups) throws SQLException {
        preparedStatement.setInt(1, groups.getGroupId());
        preparedStatement.setString(2, groups.getGroupName());
    }

    @Override
    protected Group mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Group.Builder()
                .withGroupId(resultSet.getInt("group_id"))
                .withGroupName(resultSet.getString("group_name"))
                .build();
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, Group groups) throws SQLException {
        preparedStatement.setString(1, groups.getGroupName());
        preparedStatement.setInt(2, groups.getGroupId());
    }
}
