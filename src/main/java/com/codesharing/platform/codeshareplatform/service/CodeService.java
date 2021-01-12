package com.codesharing.platform.codeshareplatform.service;

import com.codesharing.platform.codeshareplatform.model.Code;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CodeService {

    List<Code> findAll();

    String save(Code code);

    Optional<Code> findCodeById(Long id);

    String delete(Long id);

    Long count();

    String findUuidById(Long id);

    Code findCodeByUuid(String uuid);

    Long getLastCodeId();

    //String deleteCodeByUuid(String uuid);
}
