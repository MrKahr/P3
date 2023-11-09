package com.proj.function;

import org.springframework.data.repository.CrudRepository;

import com.proj.model.users.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {
    // You can define custom query methods here if needed
}