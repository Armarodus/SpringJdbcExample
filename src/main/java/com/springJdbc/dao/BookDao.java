package com.springJdbc.dao;

import java.util.List;

import com.springJdbc.interfaces.FileInterface;
import com.springJdbc.models.Book;
import com.springJdbc.models.dto.BookDto;

public interface BookDao extends FileInterface{

	void add(Book book);

	void remove(Integer id);

	void update(Book book);

	List<Book> getAll();

	List<BookDto> getAllModified();

	Book getById(Integer id);
}
