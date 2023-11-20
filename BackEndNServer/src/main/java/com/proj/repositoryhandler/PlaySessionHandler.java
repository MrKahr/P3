package com.proj.repositoryhandler;

import java.util.NoSuchElementException;
import java.time.LocalDateTime;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proj.model.session.PlaySession;
import com.proj.repositories.PlaySessionRepository;
import com.proj.exception.PlaySessionNotFoundException;


@Service
public class PlaySessionHandler extends DbHandler<PlaySession>{
    
    @Autowired
    private PlaySessionRepository playSessionRepository;

    @Override
    public void save(PlaySession playSession){
        playSessionRepository.save(playSession);
    }

    @Override
    public void saveAll(Iterable<PlaySession> playSessionIterable){
        playSessionRepository.saveAll(playSessionIterable);
    }

    @Override
    public PlaySession findById(Integer playSessionID){
        PlaySession playSession = null;
        try{
            playSession = playSessionRepository.findById(playSessionID).orElseThrow();
        } catch(NoSuchElementException nsee){
            throw new PlaySessionNotFoundException();
        }
        return playSession;
    }

    @Override
    public boolean existsById(Integer playSessionID){
        return playSessionRepository.existsById(playSessionID);
    }

    @Override
    public Iterable<PlaySession> findAll(){
        return playSessionRepository.findAll();
    }

    @Override
    public Iterable<PlaySession> findAllById(Iterable<Integer> playSessionIdIterable){
        return playSessionRepository.findAllById(playSessionIdIterable);
    }

    @Override
    public long count(){
        return playSessionRepository.count();
    }

    @Override
    public void deleteAllById(Iterable<Integer> playSessionIdIterable){
        playSessionRepository.deleteAllById(playSessionIdIterable);
    }

    @Override
    public void delete(PlaySession playSession){
        playSessionRepository.delete(playSession);
    }

    @Override
    public void deleteAll(Iterable<PlaySession> playSessionIterable){
        playSessionRepository.deleteAll(playSessionIterable);
    }

    
    public Iterable<PlaySession> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate){
        return playSessionRepository.findByDateBetween(startDate, endDate);
    }

}