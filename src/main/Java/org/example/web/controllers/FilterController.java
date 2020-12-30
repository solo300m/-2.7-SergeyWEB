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
    public String filterData(Model model, String author, String title, String size)throws BookShelfLoginException {
        char[] b = size.toCharArray();
        for(char dd: b) {
            if (Character.isAlphabetic(dd)){
                logger.info("\"Size\" field format must be numeric only");
                throw new BookShelfLoginException("\"Size\" field format must be numeric only");
            }
        }
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
            siz = Integer.parseInt(size);
        logger.info("Filtered bookshelf "+ aut +", "+ tit +", "+siz);
        if(aut.equals("") && tit.equals("") && siz == 0)
            throw new BookShelfLoginException("Filter is NULL");
            //return"redirect:/filter";
        if(bookService.containAuthorBook(aut)==0 && tit.equals("") && siz == 0)
            throw new BookShelfLoginException("incorrect field \"author\" ");
        if(aut.equals("") && bookService.containTitleBook(tit)==0 && siz == 0)
            throw new BookShelfLoginException("incorrect field \"title\" ");
        if(aut.equals("") && tit.equals("") && bookService.containSizeBook(siz) == 0)
            throw new BookShelfLoginException("incorrect field \"size\" ");

        if(((!aut.equals("") && bookService.containAuthorBook(aut)==0) ||(!tit.equals("") && bookService.containTitleBook(tit)==0)) && siz == 0)
            throw new BookShelfLoginException("incorrect field \"title\" and/or \"author\" ");
        if(((!aut.equals("") && bookService.containAuthorBook(aut)==0) ||(siz != 0 && bookService.containSizeBook(siz)==0)) && tit.equals(""))
            throw new BookShelfLoginException("incorrect field \"size\" and/or \"author\" ");
        if(((siz != 0 && bookService.containSizeBook(siz)==0) && (tit.equals("") && bookService.containTitleBook(tit)==0)) && aut.equals(""))
            throw new BookShelfLoginException("incorrect field \"title\" and/or \"size\" ");
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
