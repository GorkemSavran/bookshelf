package com.gorkemsavran.common.dao;

import java.util.List;
import java.util.Optional;

public interface IDao<T> {

    Optional<T> get(Long id);

    List<T> getAll();

    void save(T t);

    void update(T t, T u);

    void delete(T t);
}
