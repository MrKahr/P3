package com.proj.repositories;

import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;

import com.proj.model.session.PlaySession;
import java.time.LocalDateTime;

public interface PlaySessionRepository extends CrudRepository<PlaySession, Integer> {
    // You can define custom query methods here if needed
    @Bean
    Iterable<PlaySession> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
