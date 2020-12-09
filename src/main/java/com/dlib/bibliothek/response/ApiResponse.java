

package com.dlib.bibliothek.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ApiResponse {

	private boolean error;
	
	@JsonInclude(Include.NON_NULL)
	private int errorCode;

	private String message;

	@JsonInclude(Include.NON_NULL)
	private Data data;
}
