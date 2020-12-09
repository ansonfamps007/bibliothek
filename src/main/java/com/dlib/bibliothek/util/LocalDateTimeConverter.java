
package com.dlib.bibliothek.util;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 
 * The type Local date time converter.
 */
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
	@Override
	public Timestamp convertToDatabaseColumn(final LocalDateTime attribute) {
		return attribute != null ? Timestamp.valueOf(attribute) : null;
	}

	@Override
	public LocalDateTime convertToEntityAttribute(final Timestamp dbData) {
		return dbData != null ? dbData.toLocalDateTime() : null;
	}
}
