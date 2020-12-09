
package com.dlib.bibliothek.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReturnResponse {

	private String title;
	private String fcmId;
	private int daysToReturn;
}
