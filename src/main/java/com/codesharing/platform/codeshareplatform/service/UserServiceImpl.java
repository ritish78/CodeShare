package com.codesharing.platform.codeshareplatform.service;

import com.codesharing.platform.codeshareplatform.model.Users;
import com.codesharing.platform.codeshareplatform.repository.UserRepository;
import com.codesharing.platform.codeshareplatform.security.PasswordConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    private PasswordConfig passwordConfig;

    @Override
    public Users save(Users user) {
        Users toBeSavedUser = new Users();
        toBeSavedUser.setUsername(user.getUsername());
        toBeSavedUser.setEmail(user.getEmail());
        toBeSavedUser.setPassword(passwordConfig.passwordEncoder().encode(user.getPassword()));
        toBeSavedUser.setEnabled(true);
        toBeSavedUser.setAccountNonExpired(true);
        toBeSavedUser.setAccountNonLocked(true);
        toBeSavedUser.setCredentialsNonExpired(true);
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

}
