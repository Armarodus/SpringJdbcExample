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

import com.springJdbc.dao.OrderDao;
import com.springJdbc.models.Order;
import com.springJdbc.models.dto.OrderDto;

public class OrderDaoImpl implements OrderDao {

	private static final String XLSX_TEMPLATE = "Order_modified_template.xlsx";
	private static final String XLSX_OUTPUT = "order_output.xlsx";
	private static final String CONTEXT_XLSX_VAR = "orders";
	private static final String QUERY_INSERT = "INSERT INTO Orders (book_id, user_id, count) VALUES (?,?,?)";
	private static final String QUERY_UPDATE = "UPDATE Orders SET book_id = ?, user_id = ?, count = ? WHERE id = ?";
	private static final String QUERY_SELECT_ALL = "SELECT * FROM Orders";
	private static final String QUERY_SELECT_BY_ID = "SELECT * FROM Orders WHERE id = ?";
	private static final String QUERY_DELETE_BY_ID = "DELETE FROM Orders WHERE id = ?";
	private static final String QUERRY_GET_ORDERS_MODIFIED = "SELECT * FROM Orders_view";
	private static final String QUERY_GET_USER_ORDERS = "select orders.id AS order_id, books.id AS book_id, books.name "
			+ "AS book_name, users.id AS user_id, users.name AS user_name,orders.order_date AS order_date,orders.count "
			+ "AS orders_count,books.price AS book_price,((orders.count*books.price)::REAL) AS total from orders inner "
			+ "join books on books.id=orders.book_id inner join users on users.id=orders.user_id where orders.user_id = ?";
	private SimpleJdbcCall simpleJdbcCall;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void add(Order order) {
		jdbcTemplate.update(QUERY_INSERT, order.getBook_id(), order.getUser_id(), order.getCount());
	}

	@Override
	public void remove(Integer id) {
		jdbcTemplate.update(QUERY_DELETE_BY_ID, id);

	}

	@Override
	public void update(Order order) {
		jdbcTemplate.update(QUERY_UPDATE, order.getBook_id(), order.getUser_id(), order.getCount(), order.getId());

	}

	@Override
	public List<Order> getAll() {
		List<Order> orders = jdbcTemplate.query(QUERY_SELECT_ALL, new OrderMapper());
		return orders;
	}

	@Override
	public Order getById(Integer id) {
		Order order = (Order) jdbcTemplate.queryForObject(QUERY_SELECT_BY_ID, new Object[] { id }, new OrderMapper());
		return order;
	}

	@Override
	public List<OrderDto> getAllModified() {
		List<OrderDto> orders = jdbcTemplate.query(QUERRY_GET_ORDERS_MODIFIED, new OrderDtoMapper());
		return orders;
	}

	private class OrderMapper implements RowMapper<Order> {

		@Override
		public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
			Order order = new Order();
			order.setId(rs.getInt("id"));
			order.setBook_id(rs.getInt("book_id"));
			order.setUser_id(rs.getInt("user_id"));
			order.setCount(rs.getInt("count"));
			return order;
		}

	}

	private class OrderDtoMapper implements RowMapper<OrderDto> {

		@Override
		public OrderDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderDto order = new OrderDto();
			order.setId(rs.getInt("order_id"));
			order.setBook_id(rs.getInt("book_id"));
			order.setBook_name(rs.getString("book_name"));
			order.setUser_id(rs.getInt("user_id"));
			order.setUser_name(rs.getString("user_name"));
			order.setPrice(rs.getDouble("book_price"));
			order.setCount(rs.getInt("orders_count"));
			order.setTotal(rs.getDouble("total"));
			order.setDate(rs.getDate("order_date"));
			return order;
		}

	}

	@Override
	public Float getOrdersSumTotal() {
		this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName("get_orders_sum_total");
		return simpleJdbcCall.executeFunction(Float.class);
	}

	@Override
	public Float getOrdersSumTotalByUser(Integer user_id) {
		this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName("get_orders_sum_total_by_user");
		SqlParameterSource in = new MapSqlParameterSource().addValue("user_total_id", user_id);
		return simpleJdbcCall.executeFunction(Float.class, in);
	}

	@Override
	public List<OrderDto> getUserOrders(Integer user_id) {
		List<OrderDto> orders = jdbcTemplate.query(QUERY_GET_USER_ORDERS, new Object[] { user_id },
				new OrderDtoMapper());
		return orders;
	}

	@Override
	public void saveToExcelReport() {
		List<OrderDto> orders = this.getAllModified();
		try {
			InputStream is = new FileInputStream(new File(XLSX_TEMPLATE));

			OutputStream os = new FileOutputStream(XLSX_OUTPUT);
			Context context = new Context();
			context.putVar(CONTEXT_XLSX_VAR, orders);
			JxlsHelper.getInstance().processTemplate(is, os, context);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
