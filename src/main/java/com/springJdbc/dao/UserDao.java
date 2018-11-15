package com.springJdbc.dao;

import java.util.List;

import com.springJdbc.interfaces.FileInterface;
import com.springJdbc.models.User;

public interface UserDao extends FileInterface{
	void add(User user);

	void remove(Integer id);

	void update(User user);

	List<User> getAll();

	User getById(Integer id);
}
