package com.codesharing.platform.codeshareplatform.service;

import com.codesharing.platform.codeshareplatform.model.Code;

import java.util.List;
import java.util.Optional;

public interface CodeService {

    List<Code> findAll();

    void save(Code code);

    Optional<Code> findCodeById(Long id);

    void delete(Long id);

    Long count();
}
