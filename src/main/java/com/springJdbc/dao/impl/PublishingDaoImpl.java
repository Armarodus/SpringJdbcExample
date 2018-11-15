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

import com.springJdbc.dao.PublishingHouseDao;
import com.springJdbc.models.PublishingHouse;

public class PublishingDaoImpl implements PublishingHouseDao {

	private static final String QUERY_INSERT = "INSERT INTO Publishing_house (name, owner) VALUES (?,?)";
	private static final String QUERY_UPDATE = "UPDATE Publishing_house SET name = ?, owner = ? WHERE id = ?";
	private static final String QUERY_SELECT_ALL = "SELECT * FROM Publishing_house";
	private static final String QUERY_SELECT_BY_ID = "SELECT * FROM Publishing_house WHERE id = ?";
	private static final String QUERY_DELETE_BY_ID = "DELETE FROM Publishing_house WHERE id = ?";
	private static final String XLSX_TEMPLATE= "Ph_template.xlsx";
	private static final String XLSX_OUTPUT="ph_output.xlsx";
	private static final String CONTEXT_XLSX_VAR = "phs";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void add(PublishingHouse house) {
		jdbcTemplate.update(QUERY_INSERT, house.getName(), house.getOwner());
	}

	@Override
	public void remove(Integer id) {
		jdbcTemplate.update(QUERY_DELETE_BY_ID, id);

	}

	@Override
	public void update(PublishingHouse house) {
		jdbcTemplate.update(QUERY_UPDATE, house.getName(), house.getOwner(), house.getId());

	}

	@Override
	public List<PublishingHouse> getAll() {
		List<PublishingHouse> houses = jdbcTemplate.query(QUERY_SELECT_ALL, new HouseMapper());
		return houses;
	}

	@Override
	public PublishingHouse getById(Integer id) {
		PublishingHouse house = (PublishingHouse) jdbcTemplate.queryForObject(QUERY_SELECT_BY_ID, new Object[] { id },
				new HouseMapper());
		return house;
	}

	private class HouseMapper implements RowMapper<PublishingHouse> {

		@Override
		public PublishingHouse mapRow(ResultSet rs, int rowNum) throws SQLException {
			PublishingHouse house = new PublishingHouse();
			house.setId(rs.getInt("id"));
			house.setName(rs.getString("name"));
			house.setOwner(rs.getString("owner"));
			return house;
		}

	}

	@Override
	public void saveToExcelReport() {
		List<PublishingHouse> phs = this.getAll();
		try {
			InputStream is = new FileInputStream(new File(XLSX_TEMPLATE));

			OutputStream os = new FileOutputStream(XLSX_OUTPUT);
			Context context = new Context();
			context.putVar(CONTEXT_XLSX_VAR, phs);
			JxlsHelper.getInstance().processTemplate(is, os, context);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
