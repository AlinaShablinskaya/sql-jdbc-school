package ua.com.foxminded.school.dao.impl;

import ua.com.foxminded.school.dao.CrudDao;
import ua.com.foxminded.school.dao.ConnectionFactory;
import ua.com.foxminded.school.validator.DataBaseRuntimeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E, Integer> {

    private static final BiConsumer<PreparedStatement, Integer> INTEGER_CONSUMER = (PreparedStatement preparedStatement,
            Integer param) -> {
        try {
            preparedStatement.setInt(1, param);
        } catch (SQLException e) {
            throw new DataBaseRuntimeException(e);
        }
    };

    private static final BiConsumer<PreparedStatement, String> STRING_CONSUMER = (PreparedStatement preparedStatement,
            String param) -> {
        try {
            preparedStatement.setString(1, param);
        } catch (SQLException e) {
            throw new DataBaseRuntimeException(e);
        }
    };
    
    protected final ConnectionFactory connector;
    private final String saveQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String updateQuery;
    private final String deleteByIdQuery;

    public AbstractCrudDaoImpl(ConnectionFactory connector, String saveQuery, String findByIdQuery, String findAllQuery,
            String updateQuery, String deleteByIdQuery) {
        this.connector = connector;
        this.saveQuery = saveQuery;
        this.findByIdQuery = findByIdQuery;
        this.findAllQuery = findAllQuery;
        this.updateQuery = updateQuery;
        this.deleteByIdQuery = deleteByIdQuery;
    }

    @Override
    public void save(E entity) {
        try (Connection connection = connector.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {
                    insert(preparedStatement, entity);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new DataBaseRuntimeException(e);
                }
    }
    
    @Override
    public void saveAll(List<E> entities) {
        try (Connection connection = connector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {
            for (E entity : entities) {
                insert(preparedStatement, entity);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new DataBaseRuntimeException(e);
        }
    }
    
    @Override
    public Optional<E> findById(Integer id) {
        try (Connection connection = connector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(findByIdQuery)) {
            INTEGER_CONSUMER.accept(preparedStatement, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Optional.ofNullable(mapResultSetToEntity(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataBaseRuntimeException(e);
        }
    }
    
    @Override
    public void update(E entity) {
        try (Connection connection = connector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            updateValues(preparedStatement, entity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseRuntimeException(e);
        }
    }
    
    @Override
    public List<E> findAll(int page, int itemsPerPage) {
        try (Connection connection = connector.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(findAllQuery)) {
               List<E> entities = new ArrayList<>();
               preparedStatement.setInt(1, itemsPerPage);
               preparedStatement.setInt(2, page);
               try (ResultSet resultSet = preparedStatement.executeQuery()) {
                   while (resultSet.next()) {
                       entities.add(mapResultSetToEntity(resultSet));
               }
                return entities;
               }
        } catch (SQLException e) {
            throw new DataBaseRuntimeException(e);
        }
    }
    
    @Override
    public void deleteById(Integer id) {
        try (Connection connection = connector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteByIdQuery)) {
            INTEGER_CONSUMER.accept(preparedStatement, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseRuntimeException(e);
        }
    }

    protected List<E> findAllByIntParameter(Integer parameter, String query) {
        return findAllByParameter(parameter, query, INTEGER_CONSUMER);
    }

    protected List<E> findAllByStringParam(String parameter, String query) {
        return findAllByParameter(parameter, query, STRING_CONSUMER);
    }

    protected <P> List<E> findAllByParameter(P parameter, String query,
                                             BiConsumer<PreparedStatement, P> consumer) {
        try (Connection connection = connector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            List<E> entities = new ArrayList<>();
            consumer.accept(preparedStatement, parameter);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new DataBaseRuntimeException(e);
        }
    }

    protected abstract E mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract void insert(PreparedStatement preparedStatement, E entity) throws SQLException;

    protected abstract void updateValues(PreparedStatement preparedStatement, E entity) throws SQLException;
} 
