package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.exception.BookShelfLoginException;
import org.example.app.service.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "filter")
public class FilterController {
    private final Logger logger = Logger.getLogger(RemoveFilterController.class);
    private BookService bookService;

    @Autowired
    public FilterController(BookService bookService){
        logger.info("This is the filter settings form");
        this.bookService = bookService;
    }

    @GetMapping
    public String redir(Model model){
        logger.info("Remove page");
        model.addAttribute("book",new Book());
        model.addAttribute("bookList",bookService.getAllBooks());
        return "filter_book";
        //return "redirect:/filter";
    }

    @GetMapping("/data")
    public String filterData(Model model, String author, String title, Integer size)throws BookShelfLoginException {
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
        logger.info("Filtered bookshelf "+ aut +", "+ tit +", "+siz);
        if(aut.equals("") && tit.equals("") && siz == 0)
            throw new BookShelfLoginException("Filter is NULL");
            //return"redirect:/filter";
        else {
            model.addAttribute("book", new Book());
            model.addAttribute("bookList", bookService.getFilteredBooks(aut, tit, siz));
            return "redirect:/books/shelf";
        }
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
