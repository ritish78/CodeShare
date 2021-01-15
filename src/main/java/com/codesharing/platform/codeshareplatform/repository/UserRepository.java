package com.codesharing.platform.codeshareplatform.repository;

import com.codesharing.platform.codeshareplatform.model.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.uuid=:uuid")
    Users findUserByUuid(String uuid);

    @Query("SELECT u FROM Users u WHERE u.email=:email")
    Users findUserByEmail(String email);

}
