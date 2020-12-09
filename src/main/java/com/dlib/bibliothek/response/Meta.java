
package com.dlib.bibliothek.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class Meta {

	private Integer totalPages;
	
	private Integer itemsPerPage;
	
	private Long totalItems;
}
