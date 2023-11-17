package com.proj.function;

import org.springframework.data.repository.CrudRepository;

import com.proj.model.session.PlaySession;
import java.time.LocalDateTime;

public interface PlaySessionRepository extends CrudRepository<PlaySession, Integer> {
    // You can define custom query methods here if needed

    Iterable<PlaySession> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
