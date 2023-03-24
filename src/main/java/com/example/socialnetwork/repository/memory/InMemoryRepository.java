package com.example.socialnetwork.repository.memory;

import com.example.socialnetwork.domain.Entity;
import com.example.socialnetwork.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private final Map<ID, E> entities;

    public InMemoryRepository() {
        this.entities = new HashMap<>();
    }

    @Override
    public E findOne(ID id){
        if (id == null)
            throw new IllegalArgumentException("ID must not be null!");
        return this.entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return this.entities.values();
    }

    @Override
    public E save(E E) {
        if (E == null)
            throw new IllegalArgumentException("E must not be null!");

        // if there's a mapping for this id
        if(this.entities.get(E.getId()) != null)
            return E;

        // otherwise add it
        this.entities.put(E.getId(),E);

        return null;
    }

    @Override
    public E delete(ID id) {
        if (id == null)
            throw new IllegalArgumentException("ID must not be null!");

        // null if there's no mapping for this id
        return this.entities.remove(id);
    }

    @Override
    public E update(E E) {
        if(E == null)
            throw new IllegalArgumentException("E must not be null!");

        // if there is a mapping for this id
        if(this.entities.get(E.getId()) != null) {
            // update the value
            this.entities.put(E.getId(), E);
            return null;
        }
        // if there's no mapping for this id
        return E;
    }
}
