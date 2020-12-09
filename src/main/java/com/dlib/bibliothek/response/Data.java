
package com.dlib.bibliothek.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@lombok.Data
public class Data {

	@JsonInclude(Include.NON_NULL)
	private Object user;
	
	@JsonInclude(Include.NON_NULL)
	private Object newApiKey;

	@JsonInclude(Include.NON_NULL)
	private Object prefs;

	@JsonInclude(Include.NON_NULL)
	private List<FeedItems> feedItems;

	@JsonInclude(Include.NON_NULL)
	private Meta meta;

	@JsonInclude(Include.NON_NULL)
	private Object authors;

	@JsonInclude(Include.NON_NULL)
	private Object languages;

	@JsonInclude(Include.NON_NULL)
	private Object books;

	@JsonInclude(Include.NON_NULL)
	private Object book;

	@JsonInclude(Include.NON_NULL)
	private Object categories;

	@JsonInclude(Include.NON_NULL)
	private List<Notifications> notifications;
}
