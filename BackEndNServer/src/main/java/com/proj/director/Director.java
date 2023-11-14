package com.proj.director;

import com.proj.model.users.User;

public abstract class Director {
    // Fields
    private Manager manager; 
    private String operation;


    // Methods

    //// Determine Manager | Based on input type, delegate the call to the appropriate Manager (e.g. for User -> UserManager)
    public Manager determineManager(User user){
        return new UserManager();
    } 

    public Manager determineManager(Module module){
        return new ModuleManager();
    }

    //// Determine Operation | Based on enum type, delegate the call to the appropriate Method (e.g. for save -> Repository.save())
    public EnumdbOperation determineOperation(EnumdbOperation enumdbOperation){
        return enumdbOperation.COUNT;
    }; 

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
