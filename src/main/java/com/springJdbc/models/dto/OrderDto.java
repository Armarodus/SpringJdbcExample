package com.springJdbc.models.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
	private Integer id;
	private Integer book_id;
	private String book_name;
	private Integer user_id;
	private String user_name;
	private Integer count;
	private Double price;
	private Double total;
	private Date date;
}
