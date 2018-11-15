package com.springJdbc.models.dto;

import com.springJdbc.models.PublishingHouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

	private Integer id;
	private String name;
	private String author;
	private PublishingHouse ph;
	private Double price;
}
