package com.proj.repositoryhandler;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proj.model.session.Module;
import com.proj.repositories.ModuleRepository;
import com.proj.exception.NoModuleFoundException;

@Service
public class ModuledbHandler extends DbHandler<Module>{
    // Fields
    @Autowired
    private ModuleRepository moduleRepository;

    //Methods
    @Override
    public void save(Module module){
        moduleRepository.save(module);
    }
    
    @Override
    public void saveAll(Iterable<Module> moduleIterable){
        moduleRepository.saveAll(moduleIterable);
    }
    
    @Override
    public Module findById(Integer moduleID){
        Module module = null;
        try {
            module = moduleRepository.findById(moduleID).orElseThrow();
        } catch (NoSuchElementException nsee) {
            throw new NoModuleFoundException("Module '"+ module +"' with id'"+ moduleID +"'");
        }
        return module;
    }
    
    @Override
    public boolean existsById(Integer moduleID){
        return moduleRepository.existsById(moduleID);
    }
    
    @Override
    public Iterable<Module> findAll(){
        return moduleRepository.findAll();
    }
    
    @Override
    public Iterable<Module> findAllById(Iterable<Integer> moduleIdIterable){
        return moduleRepository.findAllById(moduleIdIterable);
    }
    
    @Override
    public long count(){
        return moduleRepository.count();
    }
    
    @Override
    public void deleteAllById(Iterable<Integer> moduleIdIterable){
        moduleRepository.deleteAllById(moduleIdIterable);
    }
    
    @Override
    public void delete(Module module){
        moduleRepository.delete(module);
    }
    
    @Override
    public void deleteAll(Iterable<Module> moduleIterable){
        moduleRepository.deleteAll(moduleIterable);
    }
}