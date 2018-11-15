package com.springJdbc.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springJdbc.dao.BookDao;
import com.springJdbc.dao.PublishingHouseDao;
import com.springJdbc.models.Book;

@Controller
public class BookController {

	@Autowired
	private BookDao bookDao;

	@Autowired
	private PublishingHouseDao houseDao;

	@GetMapping("/book")
	public String getBooks(Model model) {
		model.addAttribute("books", bookDao.getAll());
		return "book_list";
	}

	@GetMapping("/bookmodified")
	public String getBooksModified(Model model) {
		model.addAttribute("books", bookDao.getAllModified());
		return "book_modified_list";
	}

	@GetMapping("/book/{id}/delete")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirect) {
		bookDao.remove(id);
		;
		redirect.addFlashAttribute("success", new StringBuilder().append("Deleted").append(id).toString());
		return "redirect:/book";
	}

	@GetMapping("/book/add")
	public String add(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("phouses", houseDao.getAll());
		return "book_form";
	}

	@GetMapping("/book/{id}/edit")
	public String edit(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("book", bookDao.getById(id));
		model.addAttribute("phouses", houseDao.getAll());
		return "book_form";
	}

	@RequestMapping(value = "/book/save", method = RequestMethod.POST, params = "action=edit")
	public String saveEdited(@Valid Book book, Model model, RedirectAttributes redirect) {
		model.addAttribute("phouses", houseDao.getAll());

		bookDao.update(book);

		redirect.addFlashAttribute("success", "Збережено");
		return "redirect:/book";
	}

	@RequestMapping(value = "/book/save", method = RequestMethod.POST, params = "action=save")
	public String save(@Valid Book book, Model model, RedirectAttributes redirect) {
		model.addAttribute("phouses", houseDao.getAll());

		bookDao.add(book);

		redirect.addFlashAttribute("success", "Збережено");
		return "redirect:/book";
	}
}
