package com.codesharing.platform.codeshareplatform.controller;

import com.codesharing.platform.codeshareplatform.exception.CodeNotFoundException;
import com.codesharing.platform.codeshareplatform.model.Code;
import com.codesharing.platform.codeshareplatform.repository.CodeRepository;
import com.codesharing.platform.codeshareplatform.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CodeController {

    @Autowired
    private CodeService codeService;


//    @Autowired
//    public void setCodeRepository(CodeRepository codeRepository) {
//        this.codeRepository = codeRepository;
//    }

    @GetMapping(path = "/")
    public String welcomePage() {
        return "welcome";
    }


    @GetMapping(path = "/code/new")
    public String getNewCode(){
        return "NewCode";
    }

    @PostMapping(path = "/code/new")
    public String addCodeFromForm() {
        return "welcome";
    }

    @GetMapping(path = "/code/{uuid}", produces = "text/html")
    public String getCode(@PathVariable String uuid, Model model) {

        //First getting code
       Optional<Code> code = Optional.ofNullable(codeService.findCodeByUuid(uuid));

       if(code.isPresent()) {
           model.addAttribute("snippet_date", code.get().getDateTime());
           model.addAttribute("snippet_code", code.get().getBody());
       }else{
           throw new CodeNotFoundException("ID doesn't exists for: " + uuid);
       }

        return "codedynamic";
    }

    @GetMapping(path = "/code/latest", produces = "text/html")
    public String getLatestCode(Model model) {
        Long count = codeService.count();
        Optional<Code> code = codeService.findCodeById(count - 1);

        if (code.isPresent()) {
            model.addAttribute("snippet_date", code.get().getDateTime());
            model.addAttribute("snippet_code", code.get().getBody());
        }else{
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

}
