package rest.services.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rest.services.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByEmailAddressAndPassword(String emailAddress, String password);
	
	User findByEmailAddressAndPasswordOrMobileNumberAndPassword(String emailAddress, String password, String mobileNumber, String password2);

	List<User> findByIsActiveOrderByNameAsc(boolean active);
	
}
