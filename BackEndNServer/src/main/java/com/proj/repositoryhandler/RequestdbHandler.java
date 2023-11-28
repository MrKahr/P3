package com.proj.repositoryhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.proj.repositories.RequestRepository;
import com.proj.exception.UserNotFoundException;
import com.proj.model.events.Request;
import com.proj.model.events.RequestType;

/**
 * Handles interaction with the requst repository that stores user requests in the
 * database.
 * NB: Make sure to instantiate this handler in the relevant manager (e.g.
 * userdbHandler in UserManager).
 * For more details and justification, see DbHandler.java
 */
@Service
public class RequestdbHandler extends DbHandler<Request> {
    // Field
    @Autowired
    private RequestRepository requestRepository;

    // Method
    @Override
    public void save(Request request) {
        try {
            requestRepository.save(request);
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
    public void saveAll(Iterable<Request> requestIterable) {
        try {
            requestRepository.saveAll(requestIterable);
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
    public Request findById(Integer userID) {
        return requestRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));
    }

    @Override
    public boolean existsById(Integer userID) {
        /*
         * IllegalArgumentException thrown by existsById when userID = null is handled
         * by Spring
         * Try-catch is redundant here.
         */
        return userID == null ? false : requestRepository.existsById(userID);
    }

    @Override
    public Iterable<Request> findAll() {
        return requestRepository.findAll();
    }

    @Override
    public Iterable<Request> findAllById(Iterable<Integer> userIdIterable) {
        /*
         * Unsure how findAllByID throws exceptions in cases when multiple null value
         * ids are present
         * No try catch is included because documentation is too sparse to address
         * above-mentioned issue:
         * See https://docs.spring.io/spring-data/commons/docs/current/api/org/
         * springframework/data/repository/CrudRepository.html#findAllById(java.lang.
         * Iterable)
         */
        return requestRepository.findAllById(userIdIterable);
    }

    @Override
    public long count() {
        return requestRepository.count();
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
        requestRepository.deleteAllById(userIdIterable);
    }

    @Override
    public void delete(Request request) {
        try {
            requestRepository.delete(request);
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
    public void deleteAll(Iterable<Request> requestIterable) {
        try {
            requestRepository.deleteAll(requestIterable);
        } catch (IllegalArgumentException iae) {
            // TODO: Add this to a logging function. Display logs to admins on frontend.
            // Delete the printStackTrace.
            iae.printStackTrace();

        } catch (OptimisticLockingFailureException olfe){
            // TODO: Add this to a logging function. Display logs to admins on frontend.
            // Delete the printStackTrace.           
            olfe.printStackTrace();
        }
    }

    public Iterable<Request> findAllByUserId(int requestingId){
        return requestRepository.findAllByUserId(requestingId);
    }

    public Iterable<Request> findAllByRequestType(RequestType requestType){
        return requestRepository.findAllByRequestType(requestType);
    }
}