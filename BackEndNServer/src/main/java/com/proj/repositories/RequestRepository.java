package com.proj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proj.model.events.Request;
import com.proj.model.events.RequestType;

@Repository
public interface RequestRepository extends CrudRepository<Request, Integer> {
    // You can define custom query methods here if needed
    Iterable<Request> findAllByUserId(int requestingId);
    Iterable<Request> findAllByRequestType(RequestType requestType);
}
