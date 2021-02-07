package com.codesharing.platform.codeshareplatform.service;

import com.codesharing.platform.codeshareplatform.model.Code;
import com.codesharing.platform.codeshareplatform.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CodeServiceImpl implements CodeService {

    @Autowired
    private CodeRepository repository;

    @Autowired
    UserService userService;

    @Override
    public List<Code> findAll() {
        return (List<Code>) repository.findAll();
    }

    @Override
    public Code save(Code code) {
        // repository.save(code);
        //Returning count, which is equal to the saved code id - 1
        // since, we have a value at id 0.
//        Long noOfRows = repository.count() - 1;
//        return repository.findById(noOfRows).get().getUuid();
        Code codeToBeSaved = new Code();
        codeToBeSaved.setBody(code.getBody());
        codeToBeSaved.setDateTime(code.getDateTime());
        codeToBeSaved.setTimeInSeconds(code.getTimeInSeconds());
        codeToBeSaved.setViewsLeft(code.getViewsLeft());
        //codeToBeSaved.setUser(userService.findUserByUuid(code.getUuid()));
        return repository.save(codeToBeSaved);
    }

    @Override
    public Optional<Code> findCodeById(Long id) {
        return repository.findById(id);
    }

    @Override
    public String delete(Long id) {
        String uuid = repository.findById(id).get().getUuid();
        repository.deleteById(id);
        return uuid;
    }

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public String findUuidById(Long id) {
        return repository.findById(id).get().getUuid();
    }

    @Override
    public Code findCodeByUuid(String uuid) {
        return repository.findCodeByUuid(uuid);
    }

    @Override
    public Long getLastCodeId() {
        return repository.getLatestCodeId();
    }

    @Override
    public Code decreaseCodeView(String uuid, Code codeToUpdate) {
        Code codeFromDb = repository.findCodeByUuid(uuid);
        codeFromDb.setUuid(uuid);
        codeFromDb.setBody(codeToUpdate.getBody());
        codeFromDb.setTimeInSeconds(codeToUpdate.getTimeInSeconds());
        codeFromDb.setDateTime(codeToUpdate.getDateTime());
        codeFromDb.setViewsLeft(codeToUpdate.getViewsLeft() - 1);
        repository.save(codeFromDb);
        return codeFromDb;
    }

    @Override
    public void deleteAllCodeOfAUser(Long id_user) {
        Iterable<Code> allCode = repository.findAll();

        for (Code code : allCode) {
            if (code.getUser().getId().equals(id_user)) {
                repository.deleteById(code.getId());
            }
        }
    }

}
