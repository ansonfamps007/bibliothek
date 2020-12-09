
package com.dlib.bibliothek.response;

import java.time.LocalDateTime;

import javax.persistence.Convert;

import com.dlib.bibliothek.util.LocalDateTimeConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserResponse {

	@JsonInclude(Include.NON_NULL)
	private Integer id;

	@JsonInclude(Include.NON_NULL)
	private String name;

	@JsonInclude(Include.NON_NULL)
	private String email;

	@JsonInclude(Include.NON_NULL)
	private String apiKey;

	@JsonInclude(Include.NON_NULL)
	private String refreshToken;

	@JsonInclude(Include.NON_NULL)
	private String role;

	// User will active after email verification
	@JsonInclude(Include.NON_NULL)
	private Boolean isActive;

	@Convert(converter = LocalDateTimeConverter.class)
	@JsonInclude(Include.NON_NULL)
	private LocalDateTime createdAt;

	@JsonInclude(Include.NON_NULL)
	private String fcmId;

	@JsonInclude(Include.NON_NULL)
	private String title;

	@JsonInclude(Include.NON_NULL)
	private Integer daysToReturn;

	@JsonInclude(Include.NON_NULL)
	private long returnDate;

	@JsonInclude(Include.NON_NULL)
	private long issuedAt;

	@Override
	public String toString() {
		return "UserResponse [id=" + id + ", name=" + name + ", email=" + email + ", apiKey=" + apiKey
				+ ", refreshToken=" + refreshToken + ", role=" + role + ", isActive=" + isActive + ", createdAt="
				+ createdAt + ", fcmId=" + fcmId + ", title=" + title + ", daysToReturn=" + daysToReturn
				+ ", returnDate=" + returnDate + ", issuedAt=" + issuedAt + "]";
	}
}
