package org.example.app.service;

import java.util.List;

public interface ProjectRepository<T> {
    boolean store(T book);

    List<T> retreiveAll();

    List<T> retreiveFilterAll(String author, String title, int size);

    void filterOut();

    boolean removeItemById(Integer bookIdToRemove);

    int removeFilter(String author, String title, int size);

    boolean filterItemBook(String author, String title, int size);
}