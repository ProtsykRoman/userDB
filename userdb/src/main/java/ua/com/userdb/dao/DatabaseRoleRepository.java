package ua.com.userdb.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.userdb.model.Database;
import ua.com.userdb.model.DatabaseRole;

public interface DatabaseRoleRepository extends JpaRepository<DatabaseRole, Integer>{
	Optional<DatabaseRole> findByName(String name);
	List<DatabaseRole> findByDatabase(Database database);
}
