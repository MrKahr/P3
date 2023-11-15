package com.proj.director;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.proj.model.users.User;
import com.proj.database.UserRepository;
import com.proj.exception.UserNotFoundException;


//@Repository
public class UserManager extends Manager<User>{
    // Fields
    @Autowired
    private UserRepository userRepository;

    //Methods
    @Override
    public void save(User user){
        userRepository.save(user);
    }
    
    @Override
    public void saveAll(Iterable<User> userIterable){
        userRepository.saveAll(userIterable);
    }
    
    @Override
    public User findById(Integer userID){
        User user;
        try {
            user = userRepository.findById(userID).orElseThrow();
        } catch (NoSuchElementException nsee) {
            throw new UserNotFoundException();
        }
        return user;
    }
    
    @Override
    public boolean existsById(Integer userID){
        return userRepository.existsById(userID);
    }
    
    @Override
    public Iterable<User> findAll(){
        return userRepository.findAll();
    }
    
    @Override
    public Iterable<User> findAllById(Iterable<Integer> userIdIterable){
        return userRepository.findAllById(userIdIterable);
    }
    
    @Override
    public long count(){
        return userRepository.count();
    }
    
    @Override
    public void deleteAllById(Iterable<Integer> userIdIterable){
        userRepository.deleteAllById(userIdIterable);
    }
    
    @Override
    public void delete(User user){
        userRepository.delete(user);
    }
    
    @Override
    public void deleteAll(Iterable<User> userIterable){
        userRepository.deleteAll(userIterable);
    }
}