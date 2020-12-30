package org.example.app.service;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final ProjectRepository<Book> bookRepo;
    private final Logger logger = Logger.getLogger(BookService.class);


    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks(){
        return bookRepo.retreiveAll();
    }

    public int containAuthorBook(String author){
        List<Book> list = getAllBooks();
        if(list.stream().filter(w->w.getAutor().equals(author)).count()!=0)
            return 1;
        else
            return 0;
    }
    public int containTitleBook(String title){
        List<Book> list = getAllBooks();
        if(list.stream().filter(w->w.getTitle().equals(title)).count()!=0)
            return 1;
        else
            return 0;
    }
    public int containSizeBook(int size){
        List<Book> list = getAllBooks();
        if(list.stream().filter(w->w.getSize() == size).count()!=0)
            return 1;
        else
            return 0;
    }

    public void filterBookOff(){
        bookRepo.filterOut();
    }

    public boolean saveBook(Book book) {
        return bookRepo.store(book);
    }

    public boolean removeBookById(Integer bookIdToRemove){
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public int removeFilterBook(String author, String title, int size){
        return bookRepo.removeFilter(author,title,size);
    }

    public List<Book> getFilteredBooks(String author, String title, int size){
        return bookRepo.retreiveFilterAll(author, title, size);
    }

    public boolean filterBook(String author, String title, int size) {
        return bookRepo.filterItemBook(author,title,size);
    }

    private void defaultInit(){
        logger.info("default INIT in book service");
    }

    private void defaultDestroy(){
        logger.info("default DESTROY in book service");
    }
}
