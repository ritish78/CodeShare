package com.codesharing.platform.codeshareplatform.controller;

import com.codesharing.platform.codeshareplatform.exception.CodeNotFoundException;
import com.codesharing.platform.codeshareplatform.exception.TimeExceededException;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    private static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";


    @PostMapping(path = "/api/code/new", consumes = "application/json")
    public @ResponseBody
    ResponseEntity<Object> addCode(@RequestBody Code code) {
        Code savedCode = codeService.save(code);
        String uuidId = savedCode.getUuid();

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
            //decreaseCodeView(uuid);


            if (codeByUuid.get().getViewsLeft() < 1) {
                //Deleting from the database
                codeService.delete(codeByUuid.get().getId());
                logger.info("Deleted code for limiting views limit for: " + uuid);
                throw new CodeNotFoundException("Code of UUID: " + uuid + " is already deleted or does not exists!");
            }else {
                //Decreasing the number of views
//                codeByUuid.get().setViewsLeft(codeByUuid.get().getViewsLeft() - 1);
//                codeService.save(codeByUuid.get());

                Code decreasedCodeView = codeService.decreaseCodeView(uuid, codeByUuid.get());


                //Checking if the time limit has passed or not
                if (decreasedCodeView.getTimeInSeconds() != null) {
                    LocalDateTime localDateTimeNow = LocalDateTime.now();

                    //Then, getting the LocalDateTime of saved code.
                    LocalDateTime localDateTimeCode = LocalDateTime.parse(decreasedCodeView.getDateTime(),
                            DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER));

                    /**
                     * Then, adding the LocalDateTime of code to the time limit of the code.
                     * If, the time that we get is before current time, then we delete the
                     * code and return a TimeExceededException.
                     */

                    if (localDateTimeCode.plusSeconds(decreasedCodeView.getTimeInSeconds()).isBefore(localDateTimeNow)) {
                        codeService.delete(decreasedCodeView.getId());
                        logger.info("Deleted code of UUID: " + decreasedCodeView.getUuid() + " for passing time limit.");
                        throw new TimeExceededException("Time exceeded for: " + decreasedCodeView.getUuid());
                    }
                }
                return decreasedCodeView;
            }

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
        ArrayList<Code> listOfCode = new ArrayList<>();

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
            Code savedCode = codeService.save(code);
            String saved = savedCode.getUuid();
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


}
