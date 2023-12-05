package com.proj.model.session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.proj.exception.AddRewardsFailedException;
import com.proj.exception.PlaySessionFullException;
import com.proj.model.events.DescriptionChanged;
import com.proj.model.events.ModuleSet;
import com.proj.model.events.RewardsGiven;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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

    @JdbcTypeCode(SqlTypes.JSON)
    private ArrayList<String> users;

    @JdbcTypeCode(SqlTypes.JSON)
    private ArrayList<Reward> rewards;

    private String description;

    private String dm;

    @JdbcTypeCode(SqlTypes.JSON)
    private RewardsGiven rewardsGiven;

    @JdbcTypeCode(SqlTypes.JSON)
    private ArrayList<DescriptionChanged> descriptionChanges;

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
     * @param users                  - Usernames of players currently signed up
     */
    public PlaySession(String title, String description, String dm, Integer currentNumberOfPlayers, LocalDateTime date, PlaySessionStateEnum state,
            Integer maxNumberOfPlayers, Module module) {
        this.title = title;
        this.currentNumberOfPlayers = currentNumberOfPlayers;
        this.date = date;
        this.state = state.toString();
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.moduleSetEvents = new ArrayList<ModuleSet>();
        this.module = module;
        this.users = new ArrayList<String>();
        this.description = description;
        this.dm = dm;
        this.descriptionChanges = new ArrayList<DescriptionChanged>();
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

    public String getDescription() {
        return description;
    }

    public String getDm() {
        return dm;
    }

    public ArrayList<Reward> getRewards() {
        return rewards;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public ArrayList<DescriptionChanged> getDescriptionChanges() {
        return descriptionChanges;
    }

    public RewardsGiven getRewardsGiven() {
        return rewardsGiven;
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

    public void setDescription(String description) {
        DescriptionChanged descriptionChanged = new DescriptionChanged(this.description);
        this.descriptionChanges.add(descriptionChanged);
        this.description = description;
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
     * 
     * @param rewards
     * @throws AddRewardsFailedException
     * @throws NullPointerException
     */
    public void setRewards(ArrayList<Reward> rewards) throws AddRewardsFailedException, NullPointerException {
        if(rewards == null) { // Allows hiding of rewards
            this.rewards = null;
        } else if(PlaySessionStateEnum.valueOf(this.state).equals(PlaySessionStateEnum.CONCLUDED)) {
            this.rewards = rewards;
            this.rewardsGiven = new RewardsGiven(LocalDateTime.now(), dm);
        } else {
            throw new AddRewardsFailedException("PlaySession is not concluded");
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
            Module emptyModule = new Module("", "", ""); // TODO: Consider whether we want to model empty modules this way
            this.setModule(emptyModule); // Use empty module as base
            this.addModuleSet(emptyModule); // Module set event fixed /
        }
    }


    /**
     * Assign a user to playsession
     * @param username of user to be assigned
     * @throws PlaySessionFullException
     */
    public void assignUser(String username) throws PlaySessionFullException {
        if (users.size() < maxNumberOfPlayers) {
            this.users.add(username);
            this.currentNumberOfPlayers = users.size();
        } else {
            throw new PlaySessionFullException("Could not add '" + username + "' as playsession is full");
        }
    }

    /**
     * Remove user from playsession
     * @param username of user to be removed
     */
    public void unassignUser(String username) {
        if (users.contains(username)) {
            users.remove(username);
        } else {
            throw new NoSuchElementException("User '" + username + "' not found in playsession");
        }
    }
}
