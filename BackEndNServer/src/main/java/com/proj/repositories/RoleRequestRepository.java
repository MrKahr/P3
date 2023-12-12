package com.proj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proj.model.events.RoleRequest;

@Repository
public interface RoleRequestRepository extends CrudRepository<RoleRequest, Integer> {
    // You can define custom query methods here if needed
    Iterable<RoleRequest> findAllByUserId(int userId);
}