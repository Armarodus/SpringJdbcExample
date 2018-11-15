package com.springJdbc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

	private Integer id;
	private String name;
	private String author;
	private Integer ph_id;
	private Double price;
	
//	public boolean isSelected(Integer userId){
//	    if (userId != null) {
//	        return userId.equals(ph_id);
//	    }
//	    return false;
//	}
}

