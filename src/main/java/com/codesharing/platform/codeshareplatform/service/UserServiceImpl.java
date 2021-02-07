package com.codesharing.platform.codeshareplatform.service;

import com.codesharing.platform.codeshareplatform.model.Users;
import com.codesharing.platform.codeshareplatform.repository.UserRepository;
import com.codesharing.platform.codeshareplatform.security.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    private HashService hashService;

    @Override
    public Users save(Users user) {
        Users toBeSavedUser = new Users();
        toBeSavedUser.setEmail(user.getEmail());
        toBeSavedUser.setUsername(user.getUsername());
        toBeSavedUser.setUuid(user.getUuid());

        /**
         * Creating a salt which acts as a hashing value and then
         * hashing the password and saving it to the database
         */

        SecureRandom random = new SecureRandom();

        byte[] salt = new byte[16];

        random.nextBytes(salt);

        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);

        toBeSavedUser.setSalt(encodedSalt);

        //Saving the hashed password
        toBeSavedUser.setPassword(hashedPassword);
        return repository.save(toBeSavedUser);
    }

    @Override
    public void delete(String uuid) {
        Users userByUuid = repository.findUserByUuid(uuid);
        repository.deleteById(userByUuid.getId());
    }

    @Override
    public List<Users> listAllUsers() {
        return (List<Users>) repository.findAll();
    }

    @Override
    public Optional<Users> findUserById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Users findUserByUuid(String uuid) {
        return repository.findUserByUuid(uuid);
    }

    @Override
    public Users findUserByEmail(String email) {
        return repository.findUserByEmail(email);
    }

    public Boolean checkEmailAvailability(String email) {
        if (repository.findUserByEmail(email) == null) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * To get the current User ID.
     * Principal is the current logged in user.
     *
     * @return User ID or null if not logged in
     */
    @Override
    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        if (username != null) {
            Users users = this.findUserByEmail(username);

            if (users != null) {
                return users.getId();
            }
        }
        return null;
    }

}
