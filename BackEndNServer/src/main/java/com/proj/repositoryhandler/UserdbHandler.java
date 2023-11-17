package com.proj.repositoryhandler;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proj.model.users.User;
import com.proj.repositories.UserRepository;
import com.proj.exception.UserNotFoundException;

/**
 * Handles interaction with the user repository that stores users in the
 * database.
 * NB: Make sure to instantiate this handler in the relevant manager (e.g.
 * userdbHandler in UserManager).
 * For more details and justification, see DbHandler.java
 */
@Service
public class UserdbHandler extends DbHandler<User> {
    // Field
    @Autowired
    private UserRepository userRepository;

    // Method
    @Override

    public void save(User user) {
        try {
            System.out.println(user);
            userRepository.save(user);
        } catch (Exception e) {
        //TODO: This error is critical. 
        //The user should be informed in the web UI (Admin+). 
        //Another exception should propagate this error upwards.

            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveAll(Iterable<User> userIterable) {
        userRepository.saveAll(userIterable);
    }

    @Override
    public User findById(Integer userID) {
        User user;
        try {
            user = userRepository.findById(userID).orElseThrow();
        } catch (NoSuchElementException nsee) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public boolean existsById(Integer userID) {
        return userRepository.existsById(userID);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Iterable<User> findAllById(Iterable<Integer> userIdIterable) {
        return userRepository.findAllById(userIdIterable);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public void deleteAllById(Iterable<Integer> userIdIterable) {
        userRepository.deleteAllById(userIdIterable);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteAll(Iterable<User> userIterable) {
        userRepository.deleteAll(userIterable);
    }
}