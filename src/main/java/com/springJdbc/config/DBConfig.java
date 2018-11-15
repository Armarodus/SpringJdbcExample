package com.springJdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springJdbc.dao.BookDao;
import com.springJdbc.dao.OrderDao;
import com.springJdbc.dao.PublishingHouseDao;
import com.springJdbc.dao.UserDao;
import com.springJdbc.dao.impl.BookDaoImpl;
import com.springJdbc.dao.impl.OrderDaoImpl;
import com.springJdbc.dao.impl.PublishingDaoImpl;
import com.springJdbc.dao.impl.UserDaoImpl;

@Configuration
public class DBConfig {

	@Bean
	public UserDao getUserDaoBean() {
		return new UserDaoImpl();
	}
	@Bean
	public BookDao getBookDaoBean() {
		return new BookDaoImpl();
	}
	@Bean
	public PublishingHouseDao getPublishingHouseDaoBean() {
		return new PublishingDaoImpl();
	}
	@Bean
	public OrderDao getOrderDaoBean() {
		return new OrderDaoImpl();
	}
}
