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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String dataRemoveFilter( String author, String title,String size/*,BindingResult bindingResult, Model model*/)throws BookShelfLoginException{
        char[] b = size.toCharArray();
        for(char dd: b) {
            if (Character.isAlphabetic(dd)){
                logger.info("\"Size\" field format must be numeric only");
                throw new BookShelfLoginException("\"Size\" field format must be numeric only");
            }
        }
            String aut;
            String tit;
            Integer siz = 0;
            if (author == null)
                aut = "";
            else
                aut = author;
            if (title == null)
                tit = "";
            else
                tit = title;
            if (size.equals(""))
                siz = 0;
            else
                siz = Integer.parseInt(size);
            logger.info("remove to filter " + aut + ", " + tit + ", " + siz);
            if (aut.equals("") && tit.equals("") && siz == 0) {
                logger.info("All fields are Null!");
                throw new BookShelfLoginException("All fields are NULL!");
            }
            //удаление по автору, остальные поля не заполнены
            if (bookService.containAuthorBook(author) != 0 && tit.equals("") &&
                    siz == 0) {
                bookService.removeFilterBook(aut, tit, siz);
                return "redirect:/books/shelf";
            } else if (bookService.containAuthorBook(author) == 0 && tit.equals("") &&
                    siz == 0) {
                logger.info("incorrect field \"author\" entry");
                throw new BookShelfLoginException("incorrect field \"author\" entry");
            }
            //удаление по названию, остальные поля не заполнены
            if (aut.equals("") && bookService.containTitleBook(tit) != 0 &&
                    siz == 0) {
                bookService.removeFilterBook(aut, tit, siz);
                return "redirect:/books/shelf";
            } else if (aut.equals("") && bookService.containTitleBook(tit) == 0 &&
                    siz == 0) {
                logger.info("incorrect field \"title\" entry");
                throw new BookShelfLoginException("incorrect field \"title\" entry");
            }
            //удаление по количеству страниц, остальные поля не заполнены
            if (aut.equals("") && tit.equals("") && bookService.containSizeBook(siz) != 0) {
                bookService.removeFilterBook(aut, tit, siz);
                return "redirect:/books/shelf";
            } else if (aut.equals("") && tit.equals("") && bookService.containSizeBook(siz) == 0) {
                logger.info("incorrect field \"size\" entry");
                throw new BookShelfLoginException("incorrect field \"size\" entry");
            }
            //удаление по автору и названию остальные поля не заполнены
            if (bookService.containAuthorBook(aut) != 0 && bookService.containTitleBook(tit) != 0 &&
                    siz == 0) {
                bookService.removeFilterBook(aut, tit, siz);
                return "redirect:/books/shelf";
            } else if (bookService.containAuthorBook(author) == 0 && bookService.containTitleBook(tit) == 0 &&
                    siz == 0) {
                logger.info("incorrect fields \"author\" and/or \"title\" entry");
                throw new BookShelfLoginException("incorrect field \"author\" and/or \"title\" entry");
            }
            //удаление по автору и количеству страниц остальные поля не заполнены
            if (bookService.containAuthorBook(aut) != 0 && tit.equals("") &&
                    bookService.containSizeBook(siz) != 0) {
                bookService.removeFilterBook(aut, tit, siz);
                return "redirect:/books/shelf";
            } else if (bookService.containAuthorBook(author) == 0 && tit.equals("") &&
                    bookService.containSizeBook(siz) == 0) {
                logger.info("incorrect field \"author\" and/or \"size\" entry");
                throw new BookShelfLoginException("incorrect field \"author\" and/or \"size\" entry");
            }

            //удаление по названию и количеству страниц остальные поля не заполнены
            if ( aut.equals("") && bookService.containTitleBook(tit) != 0 &&
                    bookService.containSizeBook(siz) != 0) {
                bookService.removeFilterBook(aut, tit, siz);
                return "redirect:/books/shelf";
            } else if (aut.equals("") && (bookService.containTitleBook(tit) == 0 ||
                    bookService.containSizeBook(siz) == 0)) {
                logger.info("incorrect fields \"title\" and/or \"size\" entry");
                throw new BookShelfLoginException("incorrect fields \"title\" and/or \"size\" entry");
            }
            //удаление когда все реквизиты заполнены
            if (bookService.containAuthorBook(aut) != 0 && bookService.containTitleBook(tit) != 0 &&
                    bookService.containSizeBook(siz) != 0) {
                bookService.removeFilterBook(aut, tit, siz);
                return "redirect:/books/shelf";
            } else if (bookService.containAuthorBook(aut) == 0 || bookService.containTitleBook(tit) == 0 ||
                    bookService.containSizeBook(siz) == 0) {
                logger.info("incorrect fields \"author\" and/or \"title\" and/or \"size\" entry");
                throw new BookShelfLoginException("incorrect fields \"author\" and/or \"title\" and/or \"size\" entry");
            }
            else {
                logger.info("All fields are incorrect!");
                throw new BookShelfLoginException("All fields are incorrect!");
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

