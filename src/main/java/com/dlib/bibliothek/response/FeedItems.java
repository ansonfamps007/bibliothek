
package com.dlib.bibliothek.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@EqualsAndHashCode
public class FeedItems {

	@JsonInclude(Include.NON_NULL)
	@EqualsAndHashCode.Include
	private Object type;
	
	@JsonInclude(Include.NON_NULL)
	private Object book;
}
