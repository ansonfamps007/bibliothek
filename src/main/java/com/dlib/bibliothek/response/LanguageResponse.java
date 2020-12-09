
package com.dlib.bibliothek.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class LanguageResponse {

	private int id;

	private String name;
}
