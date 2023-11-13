package com.proj.function;

import org.springframework.data.repository.CrudRepository;

import com.proj.model.users.User;

public interface AccountRepository extends CrudRepository<User, Integer> {
    // You can define custom query methods here if needed
}