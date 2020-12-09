
package com.dlib.bibliothek.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dlib.bibliothek.model.User;
import com.dlib.bibliothek.response.NotReturnedUsers;

/**
 * 
 * @author anson.f
 * 
 */
public interface UserRepository extends CrudRepository<User, Integer> {

	/**
	 * 
	 * Find all User.
	 * 
	 * @return user list
	 */
	List<User> findAll();

	/**
	 * 
	 * Find by user name optional.
	 * 
	 * @param username the user name
	 * @return the optional user
	 */
	Optional<User> findByUsername(String username);

	/**
	 * exists by user name.
	 * 
	 * @param username the user name
	 * @return the boolean value
	 */
	boolean existsByUsername(String username);

	/**
	 * 
	 * Find by email confirmation token.
	 * 
	 * @param emailToken the verification key
	 * @return the boolean value
	 */
	@Query(value = "select user from User user where user.confirmToken = ?1")
	Optional<User> findByEmailToken(String emailToken);

	/**
	 * 
	 * Find by reset token.
	 * 
	 * @param resetToken the verification key
	 * @return the optional user
	 */
	@Query(value = "select user from User user where user.resetToken = ?1")
	Optional<User> findByResetToken(String resetToken);

	@Query(value = "select u.name,b.title,details.return_dt as returnDt,details.issued_at as issuedAt,DATEDIFF(details.return_dt, now()) AS daysToReturn "
			+ "from users u inner join book_issue_details details on (u.id = details.user_id and details.returned_on is null) inner join "
			+ "book b on (b.id = details.book_id) " + " where return_dt<now() ", nativeQuery = true)
	List<NotReturnedUsers> findNotReturnedUsers();

	@Modifying(clearAutomatically = true)
	@Query(value = "update users set fcm_id=:fcm_id where username=:username", nativeQuery = true)
	int updateFcm(@Param("fcm_id") String fcmId, @Param("username") String username);

	@Query("select u from User u where u.id = 35")
	List<User> findAllUserBooks();
}
