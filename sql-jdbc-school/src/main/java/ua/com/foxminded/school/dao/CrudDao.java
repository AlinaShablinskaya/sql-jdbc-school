package ua.com.foxminded.school.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<E, ID> {
    void save(E entity);
    
    void saveAll(List<E> entities);
    
    Optional<E> findById(ID id);
    
    List<E> findAll(int page, int itemsPerPage);
    
    void update(E entity);
    
    void deleteById(ID id);
}
