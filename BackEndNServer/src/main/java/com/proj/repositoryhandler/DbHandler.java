package com.proj.repositoryhandler;

/**
 * This responsiblity of a handler is to interact with the database, removing this responsiblity form the manager classes.
 * The benefits of this choice is that we 
 * 1) Delegate responsibility/Maintaining SOLID-principles  
 * 2) Ensure that each handler can handle saving/finding etc differently according to repository type 
 * 3) Ensure that the client does not directly interact with the saving of databases through the CRUD-interface. 
 * 
 * To use a handler in a manager
 * 1) create a new instance of a handler from the repository type you want to interact with. 
 * 2) make calls to handler like you would with a crud repository (except deleteall() - entire repositories cannot be deleted with this handler)
 *
 *   -- Here's an example with the user handler: 
 *  @Autowired 
 *  UserdbHandler userdbHandler;
 *   -- Calling the handler in manager functions is similar to calling repository methods e.g. 
 *  userdbHandler.save(user); 
 * 
 * TODO: Suggestion, make repository generic type so default methods can be defined in this superclass "DbHandler"
 */

public abstract class DbHandler<T> {
    public abstract void save(T elem);
    public abstract void saveAll(Iterable<T> iter);
    public abstract T findById(Integer id);
    public abstract Iterable <T> findAll();    
    public abstract Iterable <T> findAllById(Iterable <Integer> ids);
    public abstract void deleteAllById(Iterable<Integer> userIdIterable);
    public abstract void delete(T elem);
    public abstract void deleteAll(Iterable<T> iterable);
    public abstract long count();
    public abstract boolean existsById(Integer id);
}
