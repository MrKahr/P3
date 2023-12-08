package com.proj.repositoryhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
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
            userRepository.save(user);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            // TODO: Add this to a logging function. Display logs to admins on frontend.
            // Delete the printStackTrace.
        } catch (OptimisticLockingFailureException olfe) {
            // TODO: Add this to a logging function. Display logs to admins on frontend.
            // Delete the printStackTrace.
            olfe.printStackTrace();
        }
    }

    @Override
    public void saveAll(Iterable<User> userIterable) {
        try {
            userRepository.saveAll(userIterable);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            // TODO: Add this to a logging function. Display logs to admins on frontend.
            // Delete the printStackTrace.
        } catch (OptimisticLockingFailureException olfe) {
            // TODO: Add this to a logging function. Display logs to admins on frontend.
            // Delete the printStackTrace.
            olfe.printStackTrace();
        }
    }

    @Override
    public User findById(Integer userID) {
        return userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));
    }

    @Override
    public boolean existsById(Integer userID) {
        /*
         * IllegalArgumentException thrown by existsById when userID = null is handled
         * by Spring
         * Try-catch is redundant here.
         */
        return userID == null ? false : userRepository.existsById(userID);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Iterable<User> findAllById(Iterable<Integer> userIdIterable) {
        /*
         * Unsure how findAllByID throws exceptions in cases when multiple null value
         * ids are present
         * No try catch is included because documentation is too sparse to address
         * above-mentioned issue:
         * See https://docs.spring.io/spring-data/commons/docs/current/api/org/
         * springframework/data/repository/CrudRepository.html#findAllById(java.lang.
         * Iterable)
         */
        return userRepository.findAllById(userIdIterable);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public void deleteAllById(Iterable<Integer> userIdIterable) {
        /*
         * Unsure how findAllByID throws exceptions in cases when multiple null value
         * ids are present
         * No try catch is included because documentation is too sparse to address
         * above-mentioned issue:
         * See https://docs.spring.io/spring-data/commons/docs/current/api/org/
         * springframework/data/repository/CrudRepository.html#findAllById(java.lang.
         * Iterable)
         */
        /* Spring documentation: */
        userRepository.deleteAllById(userIdIterable);
    }

    @Override
    public void delete(User user) {
        try {
            userRepository.delete(user);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            // TODO: Add this to a logging function. Display logs to admins on frontend.
            // Delete the printStackTrace.
        } catch (OptimisticLockingFailureException olfe) {
            // TODO: Add this to a logging function. Display logs to admins on frontend.
            // Delete the printStackTrace.
            olfe.printStackTrace();
        }
    }

    @Override
    public void deleteAll(Iterable<User> userIterable) {
        try {
            userRepository.deleteAll(userIterable);
        } catch (IllegalArgumentException iae) {
            // TODO: Add this to a logging function. Display logs to admins on frontend.
            // Delete the printStackTrace.
            iae.printStackTrace();

        } catch (OptimisticLockingFailureException olfe) {
            // TODO: Add this to a logging function. Display logs to admins on frontend.
            // Delete the printStackTrace.
            olfe.printStackTrace();
        }
    }

    /**
     * Quiries database for first user with given user name
     * @param username - username to lookup in database
     * @return first instance of users with given username
     */

    public User findByUserName(String username) {
        Iterable<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            if (username.equals(user.getBasicUserInfo().getUserName())) {
                return user;
            }
        }
        throw new UserNotFoundException("UserdbHandler: User " + username + " not found");
    }
}