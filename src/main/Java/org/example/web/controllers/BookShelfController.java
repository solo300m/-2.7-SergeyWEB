package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.service.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping(value = "/books")
@Scope("singleton")
public class BookShelfController {
    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model){
        logger.info("Ok go");
        model.addAttribute("book",new Book());
        model.addAttribute("bookIdToRemove",new BookIdToRemove());
        model.addAttribute("bookList",bookService.getAllBooks());
        return "book_shelf";
    }
    @GetMapping("/filter_out")
    public String newbooks(Model model){
        logger.info("got book shelf no filtered");
        bookService.filterBookOff();
        model.addAttribute("book",new Book());
        model.addAttribute("bookIdToRemove",new BookIdToRemove());
        model.addAttribute("bookList",bookService.getAllBooks());
        return "book_shelf";
    }
    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("book",book);
            model.addAttribute("bookIdToRemove",new BookIdToRemove());
            model.addAttribute("bookList",bookService.getAllBooks());
            return "book_shelf";
        }
        else{
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove")//@RequestParam(value = "bookIdToRemove")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model){//, BindingResult bindingResult, Model model
        if(bindingResult.hasErrors()){
            model.addAttribute("book",new Book());
            model.addAttribute("bookList",bookService.getAllBooks());
            return "book_shelf";
        }
        else{
            bookService.removeBookById(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
//        logger.info("bookIdRemove is - "+bookIdToRemove);
//        if(bookIdToRemove == null)
//            return "redirect:/books/shelf";
//        if(bookService.removeBookById(bookIdToRemove)){
//            return "redirect:/books/shelf";
//        }
//        else{
//            return "redirect:/books/shelf";//"book_shelf";
//        }
    }

    @GetMapping("/remove_filter")
    public String rFilter(Model model){
        logger.info("Remove page");
        model.addAttribute("book",new Book());
        model.addAttribute("bookList",bookService.getAllBooks());
        return "group_remove_book";
    }

    @GetMapping("/filter")
    public String vFilter(Model model){
        logger.info("Filter page");
        model.addAttribute("book",new Book());
        model.addAttribute("bookList",bookService.getAllBooks());
        return "filter_book";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception{
        if (!file.isEmpty()) {
            String name = file.getOriginalFilename();
            byte[] bytes = file.getBytes();

            //create dir
            String rootPath = System.getProperty("catalina.home");//директория домашняя сервера TomCat
            File dir = new File(rootPath + File.separator + "external_uploads");//наименование конечной папки
            //куда будут загружаться файлы
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //создание файла который будет сохранен на сервере
            File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();

            logger.info("new file save at: " + serverFile.getAbsolutePath());
            return "redirect:/books/shelf";
        }
        else{
            logger.info("You haven't selected a file");
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/inloadFile")
    public String inloadFile(@RequestParam("file") MultipartFile file) throws Exception{
        if (!file.isEmpty()) {
            String name = file.getOriginalFilename();
            byte[] bytes = file.getBytes();

            //create dir
            String rootPath = "C:\\IdeaProjects\\MyWeb\\SergeyWEB\\src\\main\\Resorsies";//директория домашняя сервера TomCat
            File dir = new File(rootPath + File.separator + "server_inloads");//наименование конечной папки
            //куда будут загружаться файлы
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //создание файла который будет сохранен на сервере
            File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();

            logger.info("new file save at: " + serverFile.getAbsolutePath());
            return "redirect:/books/shelf";
        }
        else{
            logger.info("You haven't selected a file");
            return "redirect:/books/shelf";
        }
    }

}
