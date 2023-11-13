package com.proj.function;

import org.springframework.data.repository.CrudRepository;

import com.proj.model.session.PlaySession;

public interface CalendarRepository extends CrudRepository<PlaySession, String> {
    // You can define custom query methods here if needed
}
