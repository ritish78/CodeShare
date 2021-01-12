package com.codesharing.platform.codeshareplatform.service;

import com.codesharing.platform.codeshareplatform.model.Code;
import com.codesharing.platform.codeshareplatform.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CodeServiceImpl implements CodeService{

    @Autowired
    private CodeRepository repository;

    @Override
    public List<Code> findAll() {
        return (List<Code>) repository.findAll();
    }

    @Override
    public String save(Code code) {
        repository.save(code);
        //Returning count, which is equal to the saved code id - 1
        // since, we have a value at id 0.
        Long noOfRows = repository.count() - 1;
        return repository.findById(noOfRows).get().getUuid();
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


}
