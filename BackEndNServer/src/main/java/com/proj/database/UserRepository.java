package com.proj.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import com.proj.model.users.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    // You can define custom query methods here if needed

}