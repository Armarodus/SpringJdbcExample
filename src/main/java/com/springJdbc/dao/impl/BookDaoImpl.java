package com.springJdbc.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.springJdbc.dao.BookDao;
import com.springJdbc.interfaces.FileInterface;
import com.springJdbc.models.Book;
import com.springJdbc.models.PublishingHouse;
import com.springJdbc.models.dto.BookDto;

public class BookDaoImpl implements BookDao, FileInterface {

	private static final String XLSX_TEMPLATE = "Book_template.xlsx";
	private static final String XLSX_OUTPUT = "book_output.xlsx";
	private static final String XLSX_TEMPLATE_MODIFIED = "Book_modified_template.xlsx";
	private static final String XLSX_OUTPUT_MODIFIED = "book_modified_output.xlsx";
	private static final String CONTEXT_XLSX_VAR = "books";
	private static final String QUERY_SELECT_ALL = "SELECT * FROM Books";
	private static final String QUERY_SELECT_BY_ID = "SELECT * FROM Books WHERE id = ?";
	private static final String QUERY_DELETE_BY_ID = "DELETE FROM Books WHERE id = ?";
	private static final String QUERRY_GET_BOOKS_MODIFIED = "SELECT * FROM Books_view";
	private SimpleJdbcCall simpleJdbcCall;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void add(Book book) {
		this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName("insert_book_data");
		SqlParameterSource in = new MapSqlParameterSource().addValue("book_name", book.getName())
				.addValue("book_author", book.getAuthor()).addValue("book_publishing_house_id", book.getPh_id())
				.addValue("book_price", book.getPrice());
		simpleJdbcCall.executeFunction(void.class, in);
	}

	@Override
	public void remove(Integer id) {
		jdbcTemplate.update(QUERY_DELETE_BY_ID, id);

	}

	@Override
	public void update(Book book) {
		this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName("update_book_data");
		SqlParameterSource in = new MapSqlParameterSource().addValue("book_name", book.getName())
				.addValue("book_author", book.getAuthor()).addValue("book_publishing_house_id", book.getPh_id())
				.addValue("book_price", book.getPrice()).addValue("book_id", book.getId());
		simpleJdbcCall.executeFunction(void.class, in);
	}

	@Override
	public List<Book> getAll() {
		List<Book> books = jdbcTemplate.query(QUERY_SELECT_ALL, new BookMapper());
		try {
			InputStream is = new FileInputStream(new File("Book1.xlsx"));

			OutputStream os = new FileOutputStream("book_output.xlsx");
			Context context = new Context();
			context.putVar("books", books);
			JxlsHelper.getInstance().processTemplate(is, os, context);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return books;
	}

	@Override
	public Book getById(Integer id) {
		Book book = (Book) jdbcTemplate.queryForObject(QUERY_SELECT_BY_ID, new Object[] { id }, new BookMapper());
		return book;
	}

	/*
	 * Mapper for Book from table books
	 */
	private class BookMapper implements RowMapper<Book> {

		@Override
		public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
			Book book = new Book();
			book.setId(rs.getInt("id"));
			book.setName(rs.getString("name"));
			book.setAuthor(rs.getString("author"));
			book.setPh_id(rs.getInt("ph_id"));
			book.setPrice(rs.getDouble("price"));
			return book;
		}

	}

	@Override
	public List<BookDto> getAllModified() {
		List<BookDto> books = jdbcTemplate.query(QUERRY_GET_BOOKS_MODIFIED, new BookDtoMapper());
		return books;
	}

	@Override
	public void saveToExcelReport() {
		List<Book> books = jdbcTemplate.query(QUERY_SELECT_ALL, new BookMapper());
		try {
			InputStream is = new FileInputStream(new File(XLSX_TEMPLATE));

			OutputStream os = new FileOutputStream(XLSX_OUTPUT);
			Context context = new Context();
			context.putVar("books", books);
			JxlsHelper.getInstance().processTemplate(is, os, context);

		} catch (IOException e) {
			e.printStackTrace();
		}
		List<BookDto> booksModified = jdbcTemplate.query(QUERRY_GET_BOOKS_MODIFIED, new BookDtoMapper());
		try {
			InputStream is = new FileInputStream(new File(XLSX_TEMPLATE_MODIFIED));

			OutputStream os = new FileOutputStream(XLSX_OUTPUT_MODIFIED);
			Context context = new Context();
			context.putVar(CONTEXT_XLSX_VAR, booksModified);
			JxlsHelper.getInstance().processTemplate(is, os, context);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Mapper for BookDto from view
	 */
	private class BookDtoMapper implements RowMapper<BookDto> {

		@Override
		public BookDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			BookDto bookDto = new BookDto();
			bookDto.setId(rs.getInt("book_id"));
			bookDto.setName(rs.getString("name"));
			bookDto.setAuthor(rs.getString("author"));
			bookDto.setPh(new PublishingHouse(null, rs.getString("phouse_name"), rs.getString("owner")));
			bookDto.setPrice(rs.getDouble("book_price"));
			return bookDto;
		}

	}
}
