package com.codesharing.platform.codeshareplatform.service;

import com.codesharing.platform.codeshareplatform.model.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Users save(Users user);

    void delete(String uuid);

    List<Users> listAllUsers();

    Optional<Users> findUserById(Long id);

    Users findUserByUuid(String uuid);

    Users findUserByEmail(String email);

    Boolean checkEmailAvailability(String email);

    Long getCurrentUserId();
}
