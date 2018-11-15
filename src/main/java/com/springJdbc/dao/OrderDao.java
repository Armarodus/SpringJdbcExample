package com.springJdbc.dao;

import java.util.List;

import com.springJdbc.interfaces.FileInterface;
import com.springJdbc.models.Order;
import com.springJdbc.models.dto.OrderDto;

public interface OrderDao extends FileInterface{

	void add(Order order);

	void remove(Integer id);

	void update(Order order);

	List<Order> getAll();
	
	List<OrderDto> getAllModified();

	Order getById(Integer id);
	
	/*
	 * Returns total price of all Orders
	 * 
	 * @return 			total price of all orders
	 */
	Float getOrdersSumTotal();
	
	/*
	 * Return total price of all Orders that make user by selected id
	 * 
	 * @param user_id	user's id
	 * @return 			total price by user's orders
	 */
	Float getOrdersSumTotalByUser(Integer user_id);

	/*
	 * Returns list of orders where it user participates
	 * 
	 * @param user_id	user's id in DB
	 * @return 			modified order list
	 */
	List<OrderDto> getUserOrders(Integer user_id);

}
