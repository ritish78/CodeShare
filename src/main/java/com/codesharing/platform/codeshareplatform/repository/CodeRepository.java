package com.codesharing.platform.codeshareplatform.repository;

import com.codesharing.platform.codeshareplatform.model.Code;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CodeRepository extends CrudRepository<Code, Long> {

    //Code findCodeById(Long id);

//    @Query(value = "SELECT count(id) FROM code")
//    Long countCode();
}
