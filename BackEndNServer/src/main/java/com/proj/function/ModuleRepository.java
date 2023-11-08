package com.proj.function;

import org.springframework.data.repository.CrudRepository;

import com.proj.model.session.Module;

public interface ModuleRepository extends CrudRepository<Module, Integer> {
    // You can define custom query methods here if needed
}
