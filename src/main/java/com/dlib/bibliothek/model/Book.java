
package com.dlib.bibliothek.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.dlib.bibliothek.util.LocalDateTimeConverter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@Table(name = "book")
@ToString
@SqlResultSetMapping(name = "BookMapping", entities = @EntityResult(entityClass = Book.class, fields = {
		@FieldResult(name = "description", column = "book_desc"), @FieldResult(name = "qrCode", column = "qr_code"),
		@FieldResult(name = "isDeleted", column = "is_deleted"), @FieldResult(name = "title", column = "title"),
		@FieldResult(name = "isApproved", column = "is_approved"),
		@FieldResult(name = "createdAt", column = "created_at"), @FieldResult(name = "donated", column = "donated_id"),
		@FieldResult(name = "language", column = "language_id"),
		@FieldResult(name = "category", column = "category_id"), @FieldResult(name = "author", column = "author_id"),
		@FieldResult(name = "coverThumbUrl", column = "cover_thumb_url"),
		@FieldResult(name = "coverUrl", column = "cover_url"), @FieldResult(name = "status", column = "status"),
		@FieldResult(name = "expiryDate", column = "expiry_date"),
		@FieldResult(name = "id", column = "id") }), columns = {
				@ColumnResult(name = "days_to_return", type = Integer.class),
				@ColumnResult(name = "is_renewable", type = Boolean.class) })
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String status;

	private String coverUrl;

	private String coverThumbUrl;

	private String title;

	@Transient
	private String feedType;

	@Transient
	private Integer daysToReturn;

	@Transient
	private Boolean isRenewable;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Author author;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Language language;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	private User donated;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "book_issue_details", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
	private Set<User> users;

	@CreationTimestamp
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime createdAt;

	private Boolean isApproved;

	private Boolean isDeleted;

	private String qrCode;

	@Column(name = "book_desc")
	private String description;

	@Column(name = "expiry_date")
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime expiryDate;

}
