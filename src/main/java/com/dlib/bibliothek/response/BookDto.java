package com.dlib.bibliothek.response;

import lombok.Data;

@Data
public class BookDto {

	private Integer id;

	private String title;

	private String description;

	private String author;

	private String language;

	private String category;
}
