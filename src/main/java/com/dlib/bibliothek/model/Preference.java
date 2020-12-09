
package com.dlib.bibliothek.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@Table(name = "preferences")
@ToString
public class Preference {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Boolean isDown;

	private String downReason;

	private Integer latestVersionCode;

	private String latestVersionMessage;
}
