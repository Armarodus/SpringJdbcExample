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

import com.springJdbc.dao.UserDao;
import com.springJdbc.models.User;

public class UserDaoImpl implements UserDao {

	private static final String XLSX_TEMPLATE = "User_template.xlsx";
	private static final String XLSX_OUTPUT = "user_output.xlsx";
	private static final String CONTEXT_XLSX_VAR = "users";
	private static final String QUERY_SELECT_ALL = "SELECT * FROM Users";
	private static final String QUERY_SELECT_BY_ID = "SELECT * FROM Users WHERE id = ?";
	private static final String QUERY_DELETE_BY_ID = "DELETE FROM Users WHERE id = ?";
	private SimpleJdbcCall simpleJdbcCall;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void add(User user) {
		this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName("insert_user_data");
		SqlParameterSource in = new MapSqlParameterSource().addValue("user_name", user.getName())
				.addValue("user_second_name", user.getLastName());
		simpleJdbcCall.executeFunction(void.class, in);
	}

	@Override
	public void remove(Integer id) {
		jdbcTemplate.update(QUERY_DELETE_BY_ID, id);

	}

	@Override
	public void update(User user) {
		this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName("update_user_data");
		SqlParameterSource in = new MapSqlParameterSource().addValue("user_name", user.getName())
				.addValue("user_second_name", user.getLastName()).addValue("user_id", user.getId());
		simpleJdbcCall.executeFunction(void.class, in);
	}

	@Override
	public List<User> getAll() {
		List<User> users = jdbcTemplate.query(QUERY_SELECT_ALL, new UserMapper());
		return users;
	}

	@Override
	public User getById(Integer id) {
		User user = (User) jdbcTemplate.queryForObject(QUERY_SELECT_BY_ID, new Object[] { id }, new UserMapper());
		return user;
	}

	private class UserMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setLastName(rs.getString("lastName"));
			return user;
		}

	}

	@Override
	public void saveToExcelReport() {
		List<User> users = this.getAll();
		try {
			InputStream is = new FileInputStream(new File(XLSX_TEMPLATE));

			OutputStream os = new FileOutputStream(XLSX_OUTPUT);
			Context context = new Context();
			context.putVar(CONTEXT_XLSX_VAR, users);
			JxlsHelper.getInstance().processTemplate(is, os, context);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
