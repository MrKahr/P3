package com.proj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import com.proj.model.session.PlaySession;
import java.time.LocalDateTime;

@Repository
public interface PlaySessionRepository extends CrudRepository<PlaySession, Integer> {
    // You can define custom query methods here if needed
    Iterable<PlaySession> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
