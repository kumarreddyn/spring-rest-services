package rest.services.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rest.services.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

	List<Role> findByIsActiveOrderByNameAsc(boolean active);
	
	Optional<Role> findByNameAndIsActive(String name, boolean active);

}
