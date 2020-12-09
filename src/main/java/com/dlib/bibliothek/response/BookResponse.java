
package com.dlib.bibliothek.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BookResponse {

	private Integer id;

	private Integer authorId;

	private Integer languageId;

	private Integer categoryId;

	private String status;

	private Boolean isDonated;

	private String coverUrl;

	private String coverThumbUrl;

	private String title;

	private String author;

	private String language;

	private String category;

	@JsonInclude(Include.NON_NULL)
	private String donatedBy;
	
	@JsonInclude(Include.NON_NULL)
	private String takenBy;

	private String description;

	private long createdAt;

	private Boolean isWatchListed;

	private Boolean isApproved;

	@JsonInclude(Include.NON_NULL)
	private Integer daysToReturn;

	@JsonInclude(Include.NON_NULL)
	private Boolean isRenewable;

	private Boolean isExpired;

	private long expiredAt;

}
