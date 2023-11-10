package com.proj.model.events;
import java.util.ArrayList; 

public class ModuleEdited {
    // Field 
    ArrayList <String> changes;

    // Constructor 
    public ModuleEdited(){
        this.changes = new ArrayList<String>();
    }

    // Method 
    public ArrayList<String> getChanges() {
        return changes;
    }
    //TODO: Module edited is difficult to finish right now, because I have to find a clever way to loop through fields of object - consider getdeclaredFields

}
