package com.codesharing.platform.codeshareplatform.repository;

import com.codesharing.platform.codeshareplatform.model.Code;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CodeRepository extends CrudRepository<Code, Long> {

    @Query("SELECT c FROM Code c WHERE c.uuid=:uuid")
    Code findCodeByUuid(String uuid);

    @Query("SELECT MAX(c.id) FROM Code c")
    Long getLatestCodeId();


}
