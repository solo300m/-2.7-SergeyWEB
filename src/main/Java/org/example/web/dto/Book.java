package org.example.web.dto;

import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullFields;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class Book {
    private Integer id;
    @NotEmpty
    private String autor;
    @NotEmpty
    private String title;
    @Digits(integer = 4, fraction = 0)
    @Min(1)
    @NonNull
    private Integer size = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", autor='" + autor + '\'' +
                ", title='" + title + '\'' +
                ", size=" + size +
                '}';
    }
}

