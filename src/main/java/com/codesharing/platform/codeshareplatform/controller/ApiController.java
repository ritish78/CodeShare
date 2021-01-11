package com.codesharing.platform.codeshareplatform.controller;

import com.codesharing.platform.codeshareplatform.exception.CodeNotFoundException;
import com.codesharing.platform.codeshareplatform.model.Code;
import com.codesharing.platform.codeshareplatform.repository.CodeRepository;
import com.codesharing.platform.codeshareplatform.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ApiController {

    @Autowired
    CodeService codeService;

    @Autowired
    CodeRepository codeRepository;

    @PostMapping(path = "/api/code/new", consumes = "application/json")
    public @ResponseBody ResponseEntity<Object> addCode(@RequestBody Code code) {
        codeService.save(code);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(path = "/api/code/{id}")
    public Code getCodeById(@PathVariable Long id){
        Optional<Code> codeById = codeService.findCodeById(id);

        if (codeById.isPresent()) {
            return codeById.get();
        }else{
            throw new CodeNotFoundException("ID: " + id + " does not exists!");
        }
    }

    @GetMapping(path = "/api/code/latest")
    public Object getLatestCode() {
        Long count = codeService.count();
        Optional<Code> code = codeService.findCodeById(count - 1);

        if (code.isPresent()) {
            return code;
        }else{
            /**
             * Returning an empty JSON if we don't have any code in database.
             */
            return "{}";
        }

    }

}
