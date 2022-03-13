package ua.com.foxminded.school.dao;

import java.util.List;

import ua.com.foxminded.school.domain.Group;

public interface GroupDao extends CrudDao<Group, Integer> {
    List<Group> findAllByStudentCount(int number);
}
