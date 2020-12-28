package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.service.LoginService;
import org.example.web.dto.LoginForm;
import org.example.app.exception.BookShelfLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(value="/index")
public class BeginController {
    private final Logger logger = Logger.getLogger(BeginController.class);
    private LoginService loginService;

    @Autowired
    public BeginController(LoginService loginService){
        this.loginService = loginService;
    }
    @GetMapping
    public String begin(Model model){
        logger.info("GET/begin started");
        model.addAttribute("loginForm",new LoginForm());
        return "/index";
    }

    @PostMapping("/auth")
    public String authenticate(LoginForm loginForm) throws BookShelfLoginException{
//        if(bindingResult.hasErrors()) {
            if (loginService.authenticate(loginForm)) {
                logger.info("login OK redirect to book shelf");
                return "redirect:/books/shelf";
            } else {
                logger.info("login FAIL redirect back to login");
                throw new BookShelfLoginException("invalid username or password");
                //return "redirect:/index";
            }
//        }
//        else{
//            logger.info("bed login");
//            return "redirect:/index";
//        }
    }
    @ExceptionHandler(BookShelfLoginException.class)
    public String handlerError(Model model, BookShelfLoginException exception){
        model.addAttribute("errorMessage",exception.getMessage());
        return"errors/error_identification";
    }
}
