package com.codesharing.platform.codeshareplatform.controller;

import com.codesharing.platform.codeshareplatform.exception.CodeNotFoundException;
import com.codesharing.platform.codeshareplatform.exception.TimeExceededException;
import com.codesharing.platform.codeshareplatform.exception.UserNotFoundException;
import com.codesharing.platform.codeshareplatform.model.Code;
import com.codesharing.platform.codeshareplatform.model.Users;
import com.codesharing.platform.codeshareplatform.model.UuidJsonHandler;
import com.codesharing.platform.codeshareplatform.repository.CodeRepository;
import com.codesharing.platform.codeshareplatform.service.CodeService;
import com.codesharing.platform.codeshareplatform.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ApiController {

    @Autowired
    CodeService codeService;

    @Autowired
    UserService userService;

    @Autowired
    CodeRepository codeRepository;


    Logger codeLogger = LoggerFactory.getLogger(this.getClass());
    Logger userLogger = LoggerFactory.getLogger(this.getClass());

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

        return ResponseEntity.status(HttpStatus.CREATED).body(uuid);
    }


    @PostMapping(path = "/api/code/{uuid}/new", consumes = "application/json")
    public @ResponseBody
    ResponseEntity<Object> addCodeFromUser(@PathVariable String uuid, @RequestBody Code code) {
        Optional<Users> optionalUser = Optional.ofNullable(userService.findUserByUuid(uuid));

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            code.setUser(user);

            Code savedCode = codeService.save(code);

            userService.save(user);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{uuid}")
                    .buildAndExpand(code.getUuid())
                    .toUri();

            UuidJsonHandler uuidJsonHandler = new UuidJsonHandler();
            uuidJsonHandler.setUuid(savedCode.getUuid());

            return ResponseEntity.created(location).body(uuidJsonHandler);
        } else {
            userLogger.info("User not found of uuid: " + uuid);
            throw new UserNotFoundException("User doesn't exists or is deleted for uuid: " + uuid);
        }

    }

    @GetMapping(path = "/api/code/{uuid}")
    public Code getCodeByUuid(@PathVariable String uuid) {
        Optional<Code> codeByUuid = Optional.ofNullable(codeService.findCodeByUuid(uuid));

        if (codeByUuid.isPresent()) {
            //Decreasing the viewsLeft of code once the GET Request is successful
            //decreaseCodeView(uuid);


            if (codeByUuid.get().getViewsLeft() < 1) {
                //Deleting from the database
                codeService.delete(codeByUuid.get().getId());
                codeLogger.info("Deleted code for limiting views limit for: " + uuid);
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
                        codeLogger.info("Deleted code of UUID: " + decreasedCodeView.getUuid() + " for passing time limit.");
                        throw new TimeExceededException("Time exceeded for: " + decreasedCodeView.getUuid());
                    }
                }
                return decreasedCodeView;
            }

        }else{
            codeLogger.error("Invalid uuid: " + uuid);
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
            codeLogger.info("No code in repository. Check DB!");
            return "{}";
        }
    }

    @GetMapping(path = "/api/code/last/{count}")
    public Object getLastCountCode(@PathVariable int count) {
        Long noOfRows = codeService.count();

        if (count >= noOfRows) {
            List<Code> codeList = codeService.findAll();
            codeLogger.info("Asked for more code body than stored in DB(" + count + "/" + noOfRows + ")");
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

        } else {
            codeLogger.info("Deleting code with uuid: " + uuid + " was unsuccessful.");
            throw new CodeNotFoundException("Code with id: " + uuid + " already deleted or doesn't exists!");

        }
    }


    @PostMapping(value = "/api/user/new", consumes = "application/json")
    public ResponseEntity<Object> addUser(@RequestBody Users user) {
        if (userService.checkEmailAvailability(user.getEmail())) {
            Users savedUser = userService.save(user);

            UuidJsonHandler uuidJsonHandler = new UuidJsonHandler();
            uuidJsonHandler.setUuid(savedUser.getUuid());

            return ResponseEntity.status(HttpStatus.CREATED).body(uuidJsonHandler);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(path = "/api/user/{uuid}")
    public Users getUserByUuid(@PathVariable String uuid) {
        Optional<Users> userByUuid = Optional.ofNullable(userService.findUserByUuid(uuid));

        if (userByUuid.isPresent()) {
            return userByUuid.get();
        } else {
            userLogger.info("User not found for UUID: " + uuid);
            throw new UserNotFoundException("User doesn't exists for UUID: " + uuid);
        }
    }

    @DeleteMapping(path = "/api/user/{uuid}")
    public ResponseEntity<Object> deleteUserByUuid(@PathVariable String uuid) {
        Optional<Users> toBeDeletedUser = Optional.ofNullable(userService.findUserByUuid(uuid));

        if (toBeDeletedUser.isPresent()) {
            //Deleting all code associated with the user
            codeService.deleteAllCodeOfAUser(toBeDeletedUser.get().getId());

            //First, need to delete code then only we can delete user.
            userService.delete(uuid);

            userLogger.info("Deleted user of uuid: " + uuid);
            codeLogger.info("Deleted all code associated with user of uuid: " + uuid);

            UuidJsonHandler uuidJsonHandler = new UuidJsonHandler();
            uuidJsonHandler.setUuid(uuid);

            return ResponseEntity.status(HttpStatus.OK).body(uuidJsonHandler);
        } else {
            userLogger.info("User already deleted or doesn't exists for UUID: " + uuid);
            throw new UserNotFoundException("User is already deleted or doesn't exists for UUID: " + uuid);
        }
    }


    @GetMapping(path = "/api/user/{uuid}/code")
    public List<Code> retrieveAllCodeByUser(@PathVariable String uuid) {
        Optional<Users> optionalUser = Optional.ofNullable(userService.findUserByUuid(uuid));

        if (optionalUser.isPresent()) {
            return optionalUser.get().getCodeList();
        } else {
            throw new UserNotFoundException("User with uuid: " + uuid + "does not exists!");
        }
    }

}
