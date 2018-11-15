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

import com.springJdbc.dao.OrderDao;
import com.springJdbc.dao.UserDao;
import com.springJdbc.models.User;

@Controller
public class UserController {

	@Autowired
	private UserDao userDao;
	@Autowired
	private OrderDao orderDao;
	
	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/user")
	public String index(Model model) {
		model.addAttribute("users", userDao.getAll());
		return "user_list";
	}

	@GetMapping("/user/{id}/delete")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirect) {
		userDao.remove(id);
		redirect.addFlashAttribute("success", new StringBuilder().append("Row deleted ").append(id).toString());
		return "redirect:/user";
	}

	@GetMapping("/user/add")
	public String add(Model model) {
		model.addAttribute("user", new User());
		return "user_form";
	}

	@GetMapping("/user/{id}/edit")
	public String edit(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("user", userDao.getById(id));
		return "user_form";
	}
	
	@GetMapping("/user/{id}/orders")
	public String getOrders(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("user", userDao.getById(id));
		model.addAttribute("orders",orderDao.getUserOrders(id));
		model.addAttribute("user_total",orderDao.getOrdersSumTotalByUser(id));
		return "user_orders";
	}

	@RequestMapping(value = "/user/save", method = RequestMethod.POST, params = "action=edit")
	public String saveEdited(@Valid User user, Model model, RedirectAttributes redirect) {
		userDao.update(user);
		redirect.addFlashAttribute("success", "Edited");
		return "redirect:/user";
	}

	@RequestMapping(value = "/user/save", method = RequestMethod.POST, params = "action=save")
	public String save(@Valid User user, Model model, RedirectAttributes redirect) {
		userDao.add(user);
		redirect.addFlashAttribute("success", "Saved");
		return "redirect:/user";
	}
}
