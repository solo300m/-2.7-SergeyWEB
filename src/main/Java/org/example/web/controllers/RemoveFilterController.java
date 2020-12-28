package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.exception.BookShelfLoginException;
import org.example.app.service.BookService;
import org.example.web.dto.Book;
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
@RequestMapping(value = "remove_filter")
public class RemoveFilterController {
    private final Logger logger = Logger.getLogger(RemoveFilterController.class);
    private BookService bookService;

    @Autowired
    public RemoveFilterController(BookService bookService){
        logger.info("wow");
        this.bookService = bookService;
    }

    @GetMapping//("/data")
    public String remove_filter(Model model){
        logger.info("Interest page Wow!!!");
        model.addAttribute("book",new Book());
        model.addAttribute("bookList",bookService.getAllBooks());
        return "group_remove_book";
    }
    @PostMapping("/data")
    public String dataRemoveFilter(String author, String title, Integer size)throws BookShelfLoginException{
        String aut;
        String tit;
        int siz = 0;
        if(author == null)
            aut = "";
        else
            aut = author;
        if(title == null)
            tit = "";
        else
            tit = title;
        if(size == null)
            siz = 0;
        else
            siz = size;
        logger.info("remove to filter "+ aut +", "+ tit +", "+siz);
        if(aut.equals("") && tit.equals("") && siz == 0) {
            //if (bindingResult.hasErrors()) {
                logger.info("All fields are Null!");
                throw new BookShelfLoginException("All fields are NULL!");
                //return "redirect:/remove_filter";
            //}
        }
        if(bookService.removeFilterBook(aut,tit,siz) != 0)
            return"redirect:/books/shelf";
        else
            return"redirect:/remove_filter";
    }

    @GetMapping("/books")
    public String cancelFilter(){
        return"redirect:/books/shelf";
    }

    @ExceptionHandler(BookShelfLoginException.class)
    public String handlerError(Model model, BookShelfLoginException exception){
        model.addAttribute("errorMessage",exception.getMessage());
        return"errors/404";
    }
}

