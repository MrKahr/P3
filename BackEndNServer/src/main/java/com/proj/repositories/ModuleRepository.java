package com.proj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proj.model.session.Module;

@Repository
public interface ModuleRepository extends CrudRepository<Module, Integer> {
    // You can define custom query methods here if needed
}
