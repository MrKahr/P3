package com.proj.repositoryhandler;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.proj.model.session.Module;
import com.proj.repositories.ModuleRepository;
import com.proj.exception.NoModuleFoundException;

/**
 * Handles module saving, deleting and getting to a repository.
 * NOTE: be sure to instantitate one in the relevant manager (e.g. userdbHandler
 * in UserManager).
 * For more details and justification, see DbHandler.java
 */
@Service
public class ModuledbHandler extends DbHandler<Module> {
    // Fields
    @Autowired
    protected ModuleRepository moduleRepository;

    // Methods
    @Override
    public void save(Module module) {
        try {
            moduleRepository.save(module);
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
    public void saveAll(Iterable<Module> moduleIterable) {
        try {
            moduleRepository.saveAll(moduleIterable);
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
    public Module findById(Integer moduleID) {
        Module module = null;
        try {
            module = moduleRepository.findById(moduleID).orElseThrow();
        } catch (NoSuchElementException nsee) {
            throw new NoModuleFoundException("Module '" + module + "' with id'" + moduleID + "'");
        }
        return module;
    }

    @Override
    public boolean existsById(Integer moduleID) {
        return moduleID == null ? false : moduleRepository.existsById(moduleID);
    }

    @Override
    public Iterable<Module> findAll() {
        return moduleRepository.findAll();
    }

    @Override
    public Iterable<Module> findAllById(Iterable<Integer> moduleIdIterable) {
        return moduleRepository.findAllById(moduleIdIterable);
    }

    @Override
    public long count() {
        return moduleRepository.count();
    }

    @Override
    public void deleteAllById(Iterable<Integer> moduleIdIterable) {
        moduleRepository.deleteAllById(moduleIdIterable);
    }

    @Override
    public void delete(Module module) {
        try {
            moduleRepository.delete(module);
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
    public void deleteAll(Iterable<Module> moduleIterable) {
        try {
            moduleRepository.deleteAll(moduleIterable);
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
}