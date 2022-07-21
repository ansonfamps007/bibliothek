/*
 * package com.dlib.bibliothek.exception;
 * 
 * import javax.naming.ServiceUnavailableException;
 * 
 * import org.springframework.http.HttpStatus; import
 * org.springframework.security.authentication.BadCredentialsException; import
 * org.springframework.security.core.userdetails.UsernameNotFoundException;
 * import org.springframework.web.bind.MethodArgumentNotValidException; import
 * org.springframework.web.bind.annotation.ExceptionHandler; import
 * org.springframework.web.bind.annotation.ResponseStatus; import
 * org.springframework.web.bind.annotation.RestControllerAdvice;
 * 
 * import com.dlib.bibliothek.response.ApiResponse;
 * 
 * import io.jsonwebtoken.ExpiredJwtException; import
 * io.jsonwebtoken.JwtException; import io.jsonwebtoken.MalformedJwtException;
 * 
 *//**
	 * 
	 * The type Rest exception handler.
	 */
/*
 * @RestControllerAdvice public class RestExceptionHandler {
 * 
 *//**
	 * 
	 * Bad request exception handler api error response.
	 * 
	 * @param ex the ex
	 * @return the api error response
	 */
/*
 * @ExceptionHandler(value = { ValidationException.class })
 * 
 * @ResponseStatus(HttpStatus.OK) public ApiResponse
 * badRequestExceptionHandler(final ValidationException ex) { return
 * ApiResponse.builder().error(true).errorCode(HttpStatus.BAD_REQUEST.value()).
 * message(ex.getMessage()) .build(); }
 * 
 *//**
	 * 
	 * Bad request exception handler api error response.
	 * 
	 * @param ex the ex
	 * @return the api error response
	 */
/*
 * @ExceptionHandler(value = { IllegalArgumentException.class })
 * 
 * @ResponseStatus(HttpStatus.BAD_REQUEST) public ApiResponse
 * badRequestExceptionHandler(final IllegalArgumentException ex) { return
 * ApiResponse.builder().error(true).errorCode(HttpStatus.BAD_REQUEST.value()).
 * message(ex.getMessage()) .build(); }
 * 
 *//**
	 * 
	 * Unauthorized exception handler api error response.
	 * 
	 * @param ex the ex
	 * @return the api error response
	 */
/*
 * @ExceptionHandler(value = { BadCredentialsException.class })
 * 
 * @ResponseStatus(HttpStatus.OK) public ApiResponse
 * unauthorizedExceptionHandler(final BadCredentialsException ex) { return
 * ApiResponse.builder().error(true).errorCode(HttpStatus.UNAUTHORIZED.value()).
 * message(ex.getMessage()) .build(); }
 * 
 *//**
	 * 
	 * Unauthorized exception handler api error response.
	 * 
	 * @param ex the ex
	 * @return the api error response
	 */
/*
 * @ExceptionHandler(value = { UsernameNotFoundException.class })
 * 
 * @ResponseStatus(HttpStatus.OK) public ApiResponse
 * userNameExceptionHandler(final UsernameNotFoundException ex) { return
 * ApiResponse.builder().error(true).errorCode(HttpStatus.BAD_REQUEST.value()).
 * message(ex.getMessage()) .build(); }
 * 
 *//**
	 * 
	 * Method Argument Not Valid exception handler api error response.
	 * 
	 * @param ex the ex
	 * @return the api error response
	 */
/*
 * @ExceptionHandler(value = { MethodArgumentNotValidException.class })
 * 
 * @ResponseStatus(HttpStatus.OK) public ApiResponse
 * methodArgumentNotValidExceptionHandler(final MethodArgumentNotValidException
 * ex) { final String errorMsg =
 * ex.getBindingResult().getFieldError().getField() + " - " +
 * ex.getBindingResult().getFieldError().getDefaultMessage(); return
 * ApiResponse.builder().error(true).errorCode(HttpStatus.BAD_REQUEST.value()).
 * message(errorMsg).build(); }
 * 
 *//**
	 * 
	 * JWT exception handler api error response.
	 * 
	 * @param ex the ex
	 * @return the api error response
	 */
/*
 * @ExceptionHandler(value = { JwtException.class })
 * 
 * @ResponseStatus(HttpStatus.UNAUTHORIZED) public ApiResponse
 * jwtExceptionHandler(final JwtException ex) { return
 * ApiResponse.builder().error(true).errorCode(HttpStatus.UNAUTHORIZED.value()).
 * message(ex.getMessage()) .build(); }
 * 
 *//**
	 * 
	 * 
	 * JWT exception handler api error response.
	 * 
	 * @param ex the ex
	 * @return the api error response
	 */
/*
 * @ExceptionHandler(value = { MalformedJwtException.class })
 * 
 * @ResponseStatus(HttpStatus.BAD_REQUEST) public ApiResponse
 * malformedJwtException(final MalformedJwtException ex) { return
 * ApiResponse.builder().error(true).errorCode(HttpStatus.BAD_REQUEST.value()).
 * message(ex.getMessage()) .build(); }
 * 
 *//**
	 * 
	 * Expired JWT exception handler api error response.
	 * 
	 * @param ex the ex @return the api error response
	 */
/*
 * 
 * @ExceptionHandler(value = { ExpiredJwtException.class })
 * 
 * @ResponseStatus(HttpStatus.UNAUTHORIZED) public ApiResponse
 * expiredJwtExceptionHandler(final ExpiredJwtException ex) { return
 * ApiResponse.builder().error(true).errorCode(HttpStatus.UNAUTHORIZED.value()).
 * message(ex.getMessage()) .build(); }
 * 
 * @ExceptionHandler(value = { AuthenticationException.class })
 * 
 * @ResponseStatus(HttpStatus.UNAUTHORIZED) public ApiResponse
 * expiredJwtExceptionHandler(final AuthenticationException ex) { return
 * ApiResponse.builder().error(true).errorCode(HttpStatus.UNAUTHORIZED.value()).
 * message(ex.getMessage()) .build(); }
 * 
 *//**
	 * 
	 * Internal server exception handler api error response.
	 * 
	 * @param ex the ex @return the api error response
	 *//*
		 * 
		 * @ExceptionHandler(value = { Exception.class })
		 * 
		 * @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) public ApiResponse
		 * internalServerExceptionHandler(final Exception ex) { return
		 * ApiResponse.builder().error(true).errorCode(HttpStatus.INTERNAL_SERVER_ERROR.
		 * value()) .message(ex.getMessage()).build(); }
		 * 
		 * @ExceptionHandler(value = { ServiceUnavailableException.class })
		 * 
		 * @ResponseStatus(HttpStatus.NOT_ACCEPTABLE) public ApiResponse
		 * internalServerExceptionHandler(final ServiceUnavailableException ex) { return
		 * ApiResponse.builder().error(true).errorCode(HttpStatus.NOT_ACCEPTABLE.value()
		 * ).message(ex.getMessage()) .build(); } }
		 */