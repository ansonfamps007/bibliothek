package com.dlib.bibliothek.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemRecords implements Serializable {

	private static final long serialVersionUID = 1L;

	private String itemRecord;

	private String locationId;

	private String itemCategory;
	
	private String itemDetails;

	private double price;

	private int quantity;

	private double minPrice;

	private double maxPrice;
}
