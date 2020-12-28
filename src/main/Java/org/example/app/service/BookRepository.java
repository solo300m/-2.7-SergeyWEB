package org.example.app.service;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    //private final List<Book> repo = new ArrayList<>();
    private List<Book> repoFiltered = new ArrayList<>();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> retreiveAll() {
        List<Book>books = jdbcTemplate.query("SELECT * FROM books",(ResultSet rs,int rowNum)->{
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAutor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
                });
        if(repoFiltered.size()!=0){
            return new ArrayList<>(repoFiltered);
        }
        else
            return new ArrayList<>(books);
    }
    @Override
    public boolean store(Book book) {
        if (!book.getAutor().trim().isEmpty() && !book.getTitle().trim().isEmpty() && book.getSize()!=0) {
            //book.setId(book.hashCode());
            // repo.add(book);
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("author",book.getAutor());
            parameterSource.addValue("title",book.getTitle());
            parameterSource.addValue("size",book.getSize());
            jdbcTemplate.update("INSERT INTO books(author,title,size) VALUES(:author, :title, :size)",parameterSource);
            logger.info("store new book: " + book);

            return true;
        }
        else {
            logger.info("no book add ");
            return false;
        }
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
//        if(!bookIdToRemove.equals(null)) {
//            for (Book book : retreiveAll()) {
//                if (book.getId().equals(bookIdToRemove)) {
//
//                    return true;//repo.remove(book);
//                }
//            }
//            return false;
//        }
//        else
//            return false;
        if(!bookIdToRemove.equals(null)) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("id", bookIdToRemove);
            jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);
            logger.info("remove book completed: ");
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int removeFilter(String author, String title, int size) {
        int count = 0;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if(!author.equals("") && !title.equals("") && size!=0){
            for(Book book: retreiveAll()){
                if(book.getAutor().equals(author) && book.getTitle().equals(title) && book.getSize()==size){
                    parameterSource.addValue("author",book.getAutor());
                    parameterSource.addValue("title",book.getTitle());
                    parameterSource.addValue("size",book.getSize());
                    jdbcTemplate.update("DELETE FROM books WHERE author = :author " +
                            "AND title = :title " +
                            "AND size = :size",parameterSource);
                    logger.info("remove filtered book completed: "+book);
                    //repo.remove(book);
                    count++;
                }
            }
            return count;
        }
        else if(!author.equals("") && !title.equals("") && size==0) {
            count = 0;
            for (Book book : retreiveAll()) {
                if (book.getAutor().equals(author) && book.getTitle().equals(title)) {
                    parameterSource.addValue("author",book.getAutor());
                    parameterSource.addValue("title",book.getTitle());
                    jdbcTemplate.update("DELETE FROM books WHERE author = :author " +
                            "AND title = :title",parameterSource);
                    logger.info("remove filtered book completed: " + book);
                   // repo.remove(book);
                    count++;
                }
            }
            return count;
        }
        else if(!author.equals("") && title.equals("") && size==0) {
            count = 0;
            for (Book book : retreiveAll()) {
                if (book.getAutor().equals(author)) {
                    parameterSource.addValue("author",book.getAutor());
                    jdbcTemplate.update("DELETE FROM books WHERE author = :author",parameterSource);
                    logger.info("remove filtered book completed: " + book);
                    //repo.remove(book);
                    count++;
                }
            }
            return count;
        }
        else if(author.equals("") && !title.equals("") && size==0) {
            count = 0;
            for (Book book : retreiveAll()) {
                if (book.getTitle().equals(title)) {
                    parameterSource.addValue("title",book.getTitle());
                    jdbcTemplate.update("DELETE FROM books WHERE title = :title",parameterSource);
                    logger.info("remove filtered book completed: " + book);
                    //repo.remove(book);
                    count++;
                }
            }
            return count;
        }
        else if(author.equals("") && title.equals("") && size!=0) {
            count = 0;
            for (Book book : retreiveAll()) {
                if (book.getSize() == size) {
                    parameterSource.addValue("size",book.getSize());
                    jdbcTemplate.update("DELETE FROM books WHERE size = :size",parameterSource);
                    logger.info("remove filtered book completed: " + book);
                    //repo.remove(book);
                    count++;
                }
            }
            return count;
        }
        return 0;
    }

    @Override
    public void filterOut() {
        repoFiltered.clear();
    }


    @Override

    //retreiveAll().stream().filter(w -> author.isEmpty() || w.getAutor().equals(author))
    // .filter(w -> title.isEmpty() || w.getTitle().equals(title))
    //        .filter(w -> size == 0 || w.getSize().equals(size))
    //        .collect(Collectors.toList());
    public List<Book> retreiveFilterAll(String author, String title, int size) {
        //List<Book> listReturn = new ArrayList<>();
        if(!author.equals("") && !title.equals("") && size!=0) {
            Stream<Book> filter = retreiveAll().stream().filter(w -> w.getAutor().equals(author) && w.getTitle().equals(title) && w.getSize() == size);
            repoFiltered = filter.collect(Collectors.toList());
        }
        else if(author.equals("") && title.equals("") && size == 0){
            Stream<Book> filter = retreiveAll().stream()/*.filter(w-> w.getAutor().isEmpty() && w.getTitle().isEmpty() && w.getSize() == 0)*/;
            repoFiltered = filter.collect(Collectors.toList());
        }
        else if(!author.equals("") && title.equals("") && size==0){
            Stream<Book>filter = retreiveAll().stream().filter(w -> w.getAutor().equals(author));
            repoFiltered = filter.collect(Collectors.toList());
        }
        else if(author.equals("") && !title.equals("") && size==0){
            Stream<Book>filter = retreiveAll().stream().filter(w -> w.getTitle().equals(title));
            repoFiltered = filter.collect(Collectors.toList());
        }
        else if(author.equals("") && title.equals("") && size!=0){
            Stream<Book>filter = retreiveAll().stream().filter(w -> w.getSize() == size);
            repoFiltered = filter.collect(Collectors.toList());
        }
        else if(!author.equals("") && !title.equals("") && size==0) {
            Stream<Book>filter = retreiveAll().stream().filter(w -> w.getAutor().equals(author) && w.getTitle().equals(title));
            repoFiltered = filter.collect(Collectors.toList());
        }
        else if(!author.equals("") && title.equals("") && size!=0) {
            Stream<Book>filter = retreiveAll().stream().filter(w -> w.getAutor().equals(author) && w.getSize() == size);
            repoFiltered = filter.collect(Collectors.toList());
        }
        else if(author.equals("") && !title.equals("") && size!=0) {
            Stream<Book>filter = retreiveAll().stream().filter(w -> w.getTitle().equals(title) && w.getSize() == size);
            repoFiltered = filter.collect(Collectors.toList());
        }
        return repoFiltered;
    }

    @Override
    public boolean filterItemBook(String author, String title, int size) {
        if(retreiveFilterAll(author,title,size).size()!=retreiveAll().size())
            return true;
        else
            return false;
    }
}
