package com.shop.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRepository<T> {

    protected Map<Integer, T> entities = new HashMap<>();
    protected int nextId = 1;

    public T findById(int id) {
        return entities.get(id);
    }

    public List<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    public void save(T entity) {
        int id = getEntityId(entity);
        entities.put(id, entity);
    }

    public void delete(int id) {
        entities.remove(id);
    }

    public int count() {
        return entities.size();
    }

    public int getNextId() {
        return nextId++;
    }

    protected abstract int getEntityId(T entity);
} 