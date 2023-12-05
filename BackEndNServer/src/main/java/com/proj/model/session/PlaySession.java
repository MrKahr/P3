package com.proj.model.session;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.proj.model.events.ModuleSet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

//TODO: Consider renaming ModuleSetEvent for added readability
@Entity
public class PlaySession {
    // Field

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    private Integer maxNumberOfPlayers;

    private Integer currentNumberOfPlayers;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;

    private String state;

    @JdbcTypeCode(SqlTypes.JSON)
    private ArrayList<ModuleSet> moduleSetEvents;

    @JdbcTypeCode(SqlTypes.JSON)
    private Module module; // TODO: Consider whether we want object or simple string description

    // validation check with DM = create session and check without DM = update
    // session. ?????

    // Constructor
    /**
     * Creates a play session for players to attend
     * 
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
    public PlaySession(String title, Integer currentNumberOfPlayers, LocalDateTime date, PlaySessionStateEnum state,
            Integer maxNumberOfPlayers, Module module) {
        this.title = title;
        this.currentNumberOfPlayers = currentNumberOfPlayers;
        this.date = date;
        this.state = state.toString();
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.moduleSetEvents = new ArrayList<ModuleSet>();
        this.module = module;
    }

    public PlaySession() {
    }

    // Method
    public String getTitle() {
        return title;
    }

    public Integer getId() {
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

    public void setId(Integer id) {
        this.id = id;
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

    public void setState(PlaySessionStateEnum state) {
        this.state = state.toString();
    }

    /**
     * Sets the module description of a module and adds a module set event to relect
     * this change
     * 
     * @param module module user wants to associate with a PlaySession
     * @throws NullPointerException
     */
    public void setModule(Module module) {
        this.module = module;
        this.addModuleSet(module);
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
        int currentNumberOfEvents;
        String from = "";
        String to = "";

        try {
            currentNumberOfEvents = getModuleSetEvents().size();
            previousModuleSet = getModuleSetEvents().get(currentNumberOfEvents - 1);
            from = previousModuleSet.getChangedTo();
            to = module.toString();
        } catch (IndexOutOfBoundsException iobe) {
            // Catch case when only one element exists
            from = "";
            try {
                to = module.toString();
            } catch (NullPointerException npe) {
                // Catch case when module is null and only one element exists
                to = "null";
            }
        } catch (NullPointerException npe) {
            // Catch case when module is null
            to = "null";
        } finally {
            this.moduleSetEvents.add(new ModuleSet(from, to));
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

    public void setReward() {

    }
}
