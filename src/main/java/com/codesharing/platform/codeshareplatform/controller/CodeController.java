package com.codesharing.platform.codeshareplatform.controller;

import com.codesharing.platform.codeshareplatform.exception.CodeNotFoundException;
import com.codesharing.platform.codeshareplatform.exception.TimeExceededException;
import com.codesharing.platform.codeshareplatform.model.Code;
import com.codesharing.platform.codeshareplatform.repository.CodeRepository;
import com.codesharing.platform.codeshareplatform.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CodeController {

    @Autowired
    private CodeService codeService;

    private static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";


    @GetMapping(path = "/")
    public String welcomePage() {
        return "index";
    }


    @GetMapping(path = "/code/new")
    public String getNewCode(){
        return "NewCode";
    }

    @PostMapping(path = "/code/new")
    public String addCodeFromForm() {
        return "index";
    }

    @GetMapping(path = "/code/{uuid}", produces = "text/html")
    public String getCode(@PathVariable String uuid, Model model) {

        //First getting code
       Optional<Code> code = Optional.ofNullable(codeService.findCodeByUuid(uuid));

       if(code.isPresent()) {

           if (code.get().getViewsLeft() < 1) {
               //Deleting from the database
               codeService.delete(code.get().getId());
               throw new CodeNotFoundException("Code of UUID: " + uuid + " is already deleted or does not exists!");
           } else {
               Code decreasedCodeView = codeService.decreaseCodeView(uuid, code.get());

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
                    *
                    * This part is same as in /api/code/{uuid}
                    */

                   if (localDateTimeCode.plusSeconds(decreasedCodeView.getTimeInSeconds()).isBefore(localDateTimeNow)) {
                       codeService.delete(decreasedCodeView.getId());
                       throw new TimeExceededException("Time exceeded for: " + decreasedCodeView.getUuid());
                   }
               }
           }
           model.addAttribute("snippet_date", code.get().getDateTime());
           model.addAttribute("snippet_code", code.get().getBody());
           model.addAttribute("snippet_viewsLeft", code.get().getViewsLeft());
           model.addAttribute("snippet_timeInSeconds", code.get().getTimeInSeconds());
       }else{
           throw new CodeNotFoundException("ID doesn't exists for: " + uuid);
       }

        return "codedynamic";
    }

    @GetMapping(path = "/code/latest", produces = "text/html")
    public String getLatestCode(Model model) {
        Optional<Code> optionalCode = codeService.findCodeById(codeService.getLastCodeId());

        if (optionalCode.isPresent()) {
            model.addAttribute("snippet_date", optionalCode.get().getDateTime());
            model.addAttribute("snippet_code", optionalCode.get().getBody());
            model.addAttribute("snippet_timeInSeconds", null);
        } else {
            /**
             * Throwing an exception with message 'Deleted too much of code' since,
             * we already started the application with data in the database. So,
             * this method needs to return first inserted data even if we haven't
             * manually added after starting spring boot application.
             */
            throw new CodeNotFoundException("Deleted too much of code!");
        }
        return "codedynamic";
    }

    @GetMapping(path = "/code/last/{count}")
    public String getLastCountCode(@PathVariable int count, Model model) {
        Long noOfRows = codeService.count();

        if (noOfRows <= count) {
            List<Code> codeList = codeService.findAll();
            model.addAttribute("code_list", codeList);
            model.addAttribute("last", noOfRows);
        }else{
            Long startFromId = noOfRows - count;
            ArrayList<Code> codeList = new ArrayList<>();

            for (Long i = startFromId; i < noOfRows; i++) {
                Optional<Code> optionalCode = codeService.findCodeById(i);

                if (optionalCode.isPresent()) {
                    codeList.add(optionalCode.get());
                }
            }
            model.addAttribute("code_list", codeList);
            model.addAttribute("last", count);
        }
        return "lastcountcode";

    }

    @GetMapping("/signup")
    public String loadSignupPage() {
        return "SignUp";
    }

}
