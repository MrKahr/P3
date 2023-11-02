package com.proj.database.accessingdatamysql;

import org.springframework.data.repository.CrudRepository;

import com.proj.database.accessingdatamysql.UserInfo;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<UserInfo, Integer> {

}