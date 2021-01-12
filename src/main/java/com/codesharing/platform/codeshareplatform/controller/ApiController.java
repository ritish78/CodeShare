package com.codesharing.platform.codeshareplatform.controller;

import com.codesharing.platform.codeshareplatform.exception.CodeNotFoundException;
import com.codesharing.platform.codeshareplatform.model.Code;
import com.codesharing.platform.codeshareplatform.model.UuidJsonHandler;
import com.codesharing.platform.codeshareplatform.repository.CodeRepository;
import com.codesharing.platform.codeshareplatform.service.CodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ApiController {

    @Autowired
    CodeService codeService;

    @Autowired
    CodeRepository codeRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(path = "/api/code/new", consumes = "application/json")
    public @ResponseBody ResponseEntity<Object> addCode(@RequestBody Code code) {
        String uuidId = codeService.save(code);

        //Creating an object of UuidJsonHandler class so that we can
        //send response of JSON with uuid in it.
        UuidJsonHandler uuid = new UuidJsonHandler();
        uuid.setUuid(uuidId);

        return ResponseEntity.status(HttpStatus.OK).body(uuid);
    }

    @GetMapping(path = "/api/code/{uuid}")
    public Code getCodeByUuid(@PathVariable String uuid){
        Optional<Code> codeByUuid = Optional.ofNullable(codeService.findCodeByUuid(uuid));

        if (codeByUuid.isPresent()) {
            //Decreasing the viewsLeft of code once the GET Request is successful
//            decreaseCodeView(uuid);

            return codeByUuid.get();
        }else{
            logger.error("Invalid uuid: " + uuid);
            throw new CodeNotFoundException("ID: " + uuid + " does not exists!");
        }
    }

    @GetMapping(path = "/api/code/latest")
    public Object getLatestCode() {
        //Long count = codeService.count() - 1;
        //Optional<Code> code = codeService.findCodeById(count);

        Long lastCodeId = codeService.getLastCodeId();
        Optional<Code> lastCode = codeService.findCodeById(lastCodeId);

        if (lastCode.isPresent()) {
            return lastCode;
        }else{
            /**
             * Returning an empty JSON if we don't have any code in database.
             */
            logger.info("No code in repository. Check DB!");
            return "{}";
        }
    }

    @GetMapping(path = "/api/code/last/{count}")
    public Object getLastCountCode(@PathVariable int count) {
        Long noOfRows = codeService.count();

        if (count >= noOfRows) {
            List<Code> codeList = codeService.findAll();
            logger.info("Asked for more code body than stored in DB(" + count + "/" + noOfRows + ")");
            return codeList;
        }

        Long startFromId = noOfRows - count;
        ArrayList<Code> listOfCode = new ArrayList<Code>();

        for (Long i = startFromId; i < noOfRows; i++) {
            Optional<Code> codeById = codeService.findCodeById(i);

            if (codeById.isPresent()) {
                listOfCode.add(codeById.get());
            }
        }
        return listOfCode;
    }

    @PostMapping(path = "/api/code/addAll")
    public List<UuidJsonHandler> addAllCodes(@RequestBody List<Code> codeList) {
        List<UuidJsonHandler> uuidOfSavedCode = new ArrayList<>();

        for (Code code : codeList) {
            UuidJsonHandler uuidJsonHandler = new UuidJsonHandler();
            String saved = codeService.save(code);
            uuidJsonHandler.setUuid(saved);
            uuidOfSavedCode.add(uuidJsonHandler);
        }

        return uuidOfSavedCode;
    }

    @DeleteMapping(path = "/api/code/{uuid}")
    public Object deleteFromUuid(@PathVariable String uuid) {
        //First getting the code to see if the code exists or not.
        Optional<Code> optionalCode = Optional.ofNullable(codeService.findCodeByUuid(uuid));

        if (optionalCode.isPresent()) {
            String deletedUuid = codeService.delete(optionalCode.get().getId());

            //To return Uuid of deleted row
            UuidJsonHandler uuidJsonHandler = new UuidJsonHandler();
            uuidJsonHandler.setUuid(deletedUuid);

            return uuidJsonHandler;

        }else{
            logger.info("Deleting code with uuid: " + uuid + " was unsuccessful.");
            throw new CodeNotFoundException("Code with id: " + uuid + " already deleted or doesn't exists!");

        }
    }


    //Not a rest api
    public void decreaseCodeView(String uuid) {
        //No need to make object 'optionalCode' since, we will call
        //this method only if ifPresent() method returns true.
        Code codeByUuid = codeService.findCodeByUuid(uuid);


        if (codeByUuid.getViewsLeft() <= 1) {
            codeService.delete(codeByUuid.getId());
        }else{

            //Then deleting the codeByUuid
            codeService.delete(codeByUuid.getId());

            Code code = new Code();

            //Setting value to newly created code object
            code.setId(codeByUuid.getId());
            code.setBody(codeByUuid.getBody());
            code.setDateTime(codeByUuid.getDateTime());
            code.setUuid(uuid);
            code.setTimeInSeconds(codeByUuid.getTimeInSeconds());
            code.setViewsLeft(codeByUuid.getViewsLeft() - 1);


            //Then adding code object with same value except viewLeft
            String save = codeService.save(code);
        }
    }

}
