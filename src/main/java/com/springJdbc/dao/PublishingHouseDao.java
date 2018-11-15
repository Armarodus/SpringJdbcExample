package com.springJdbc.dao;

import java.util.List;

import com.springJdbc.interfaces.FileInterface;
import com.springJdbc.models.PublishingHouse;

public interface PublishingHouseDao extends FileInterface{

	void add(PublishingHouse house);

	void remove(Integer id);

	void update(PublishingHouse house);

	List<PublishingHouse> getAll();

	PublishingHouse getById(Integer id);
}
