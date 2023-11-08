package com.proj.model.session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.proj.model.events.ModuleSet;
import java.util.Objects;

//TODO: Consider renaming ModuleSetEvent for added readability
public class PlaySession {
    // Field
    private String id;
    private String title;
    private Integer maxNumberOfPlayers;
    private Integer currentNumberOfPlayers;
    private LocalDateTime date;
    private String state;
    private ArrayList<ModuleSet> moduleSetEvents;
    private Module module; // TODO: Consider whether we want object or simple string description

    // Constructor
    /**
     * Creates a play session for players to attend
     * @param title                  - Use to show correct session on frontend
     * @param id                     - id of the playsession \\TODO: Find way to
     *                               reliably generated ID
     * @param currentNumberOfPlayers - number of players associated with the current
     *                               OlaySession
     * @param date                   - date that the PlaySession will be held
     * @param state                  - status of the current PlaySession e.g. full
     *                               or cancelled.
     * @param maxNumberOfPlayers     - current maximal number of players allowed in
     *                               a session
     */
    public PlaySession(String title, String id, Integer currentNumberOfPlayers, LocalDateTime date, String state,
            Integer maxNumberOfPlayers, Module module) {
        this.title = title;
        this.id = id;
        this.currentNumberOfPlayers = currentNumberOfPlayers;
        this.date = date;
        this.state = state;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.moduleSetEvents = new ArrayList<ModuleSet>();
        this.module = module;
    }

    // Method
    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public Integer getCurrentNumberOfPlayers() {
        return currentNumberOfPlayers;
    }

    public Integer getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getState() {
        return state;
    }

    public ArrayList<ModuleSet> getModuleSetEvents() {
        return this.moduleSetEvents;
    }

    public Module getModule() {
        return this.module;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String title) {
        this.id = title;
    }

    public void setMaxNumberOfPlayers(Integer maxNumberOfPlayers) {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    public void setCurrentNumberOfPlayers(Integer currentNumberOfPlayers) {
        this.currentNumberOfPlayers = currentNumberOfPlayers;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Sets the module description of a module and adds a module set event to relect
     * this change
     * 
     * @param module module user wants to associate with a PlaySession
     * @throws NullPointerException
     */
    public void setModule(Module module) throws NullPointerException {
        if (Objects.isNull(module)) {
            throw new NullPointerException("Module doesn't exist"); // TODO: add new no module found exception
        } else {
            this.module = module;
            this.addModuleSet(module);
        }
    }

    /**
     * Handles the creation of a module set event and adds this event to module set
     * events
     * 
     * @pre-con caller function has set a valid module
     * @throws NullPointerException
     */

    public void addModuleSet(Module module) throws NullPointerException {
        ModuleSet previousModuleSet;
        ModuleSet currentModuleSet = new ModuleSet("", "");
        int currentNumberOfEvents;

        try {
            currentNumberOfEvents = getModuleSetEvents().size();
            previousModuleSet = getModuleSetEvents().get(currentNumberOfEvents - 1);
            currentModuleSet = new ModuleSet(previousModuleSet.getChangedTo(), module.toString());      
        } catch (NullPointerException npe) {
            // Catch case when no previous modules existed.
            currentModuleSet = new ModuleSet("", module.toString()); 
        } catch (IndexOutOfBoundsException iobe){
            // Catch case when only one element exists 
            currentModuleSet = new ModuleSet("", module.toString()); 
        } finally{
            this.moduleSetEvents.add(currentModuleSet);
        }
    }

    /**
     * Removes a module associated with a playSession and updates module set event
     * 
     * @throws NullPointerException
     */
    public void removeModule() throws NullPointerException {
        Module currentModule = getModule();

        if (currentModule == null) {
            throw new NullPointerException("Module doesn't exist");
        } else {
            Module emptyModule = new Module("", "", ""); // TODO: Consider whether we want to model empty modules this
                                                         // way
            this.setModule(emptyModule); // Use empty module as base
            this.addModuleSet(emptyModule); // Module set event fixed /
        }
    }
    public void setReward(){

    }
}
