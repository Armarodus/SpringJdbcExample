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
import com.springJdbc.dao.OrderDao;
import com.springJdbc.dao.UserDao;
import com.springJdbc.models.Order;

@Controller
public class OrderController {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private BookDao bookDao;
	@Autowired
	private UserDao userDao;

	@GetMapping("/order")
	public String index(Model model) {
		model.addAttribute("orders", orderDao.getAllModified());
		model.addAttribute("sum_total", orderDao.getOrdersSumTotal());
		return "order_list";
	}

	@GetMapping("/order/{id}/delete")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirect) {
		orderDao.remove(id);
		redirect.addFlashAttribute("success", new StringBuilder().append("Deleted").append(id).toString());
		return "redirect:/order";
	}

	@GetMapping("/order/add")
	public String add(Model model) {
		model.addAttribute("order", new Order());
		model.addAttribute("books", bookDao.getAll());
		model.addAttribute("users", userDao.getAll());
		return "order_form";
	}

	@GetMapping("/order/{id}/edit")
	public String edit(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("order", orderDao.getById(id));
		model.addAttribute("books", bookDao.getAll());
		model.addAttribute("users", userDao.getAll());
		return "order_form";
	}

	@RequestMapping(value = "/order/save", method = RequestMethod.POST, params = "action=edit")
	public String saveEdited(@Valid Order order, Model model, RedirectAttributes redirect) {
		orderDao.update(order);
		redirect.addFlashAttribute("success", "Збережено");
		return "redirect:/order";
	}

	@RequestMapping(value = "/order/save", method = RequestMethod.POST, params = "action=save")
	public String save(@Valid Order order, Model model, RedirectAttributes redirect) {
		orderDao.add(order);
		redirect.addFlashAttribute("success", "Збережено");
		return "redirect:/order";
	}
}
