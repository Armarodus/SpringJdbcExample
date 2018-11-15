package com.springJdbc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	
	private Integer id;
	private Integer book_id;
	private Integer user_id;
	private Integer count;
}
