package com.codesharing.platform.codeshareplatform.service;

import com.codesharing.platform.codeshareplatform.model.Code;
import com.codesharing.platform.codeshareplatform.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CodeServiceImpl implements CodeService{

    @Autowired
    private CodeRepository repository;

    @Override
    public List<Code> findAll() {
        return (List<Code>) repository.findAll();
    }

    @Override
    public void save(Code code) {
        repository.save(code);
    }

    @Override
    public Optional<Code> findCodeById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Long count() {
        return repository.count();
    }

}
