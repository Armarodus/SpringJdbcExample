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

import com.springJdbc.dao.PublishingHouseDao;
import com.springJdbc.models.PublishingHouse;

@Controller
public class PublishingHouseController {

	@Autowired
	private PublishingHouseDao houseDao;

	@GetMapping("/house")
	public String index(Model model) {
		model.addAttribute("phouses", houseDao.getAll());
		return "house_list";
	}

	@GetMapping("/house/{id}/delete")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirect) {
		houseDao.remove(id);
		redirect.addFlashAttribute("success", new StringBuilder().append("Row deleted ").append(id).toString());
		return "redirect:/house";
	}

	@GetMapping("/house/add")
	public String add(Model model) {
		model.addAttribute("phouse", new PublishingHouse());
		return "house_form";
	}

	@GetMapping("/house/{id}/edit")
	public String edit(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("phouse", houseDao.getById(id));
		return "house_form";
	}

	@RequestMapping(value = "/house/save", method = RequestMethod.POST, params = "action=edit")
	public String saveEdited(@Valid PublishingHouse house, Model model, RedirectAttributes redirect) {
		houseDao.update(house);
		redirect.addFlashAttribute("success", "Edited");
		return "redirect:/house";
	}

	@RequestMapping(value = "/house/save", method = RequestMethod.POST, params = "action=save")
	public String save(@Valid PublishingHouse house, Model model, RedirectAttributes redirect) {
		houseDao.add(house);
		redirect.addFlashAttribute("success", "Saved");
		return "redirect:/house";
	}
}
