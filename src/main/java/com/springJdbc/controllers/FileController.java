package com.springJdbc.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.springJdbc.dao.BookDao;
import com.springJdbc.dao.OrderDao;
import com.springJdbc.dao.PublishingHouseDao;
import com.springJdbc.dao.UserDao;

@Controller
public class FileController {
	
	@Autowired
	BookDao bookDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	PublishingHouseDao houseDao;
	
	@Autowired
	OrderDao orderDao;
	
	@GetMapping(value = "/file")
	public String getFileList() {
		return "files_list";
	}
	@GetMapping(value = "/files/{file_name}")
	public void getFile(@PathVariable("file_name") String fileName, HttpServletResponse response) {
		bookDao.saveToExcelReport();
		userDao.saveToExcelReport();
		houseDao.saveToExcelReport();
		orderDao.saveToExcelReport();
		try {
		      // get your file as InputStream
		      InputStream is = new FileInputStream(new File (fileName));
		      // copy it to response's OutputStream
		      org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
		      response.flushBuffer();
		    } catch (IOException ex) {
		      
		      throw new RuntimeException("IOError writing file to output stream");
		    }
	}
}
