package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import ua.com.userdb.model.Database;
import ua.com.userdb.model.DatabaseRole;

public interface DatabaseRoleService {
	List<DatabaseRole> findAllDatabaseRole();
	Optional<DatabaseRole> findDatabaseRoleById(Integer Id);
	Optional<DatabaseRole> findDatabaseRoleByName(String name);
	List<DatabaseRole> findDatabaseRolesByDatabase(Database database);
	DatabaseRole createDatabaseRole(DatabaseRole databaseRole);
	Optional<DatabaseRole> updateDatabaseRole(Integer id, DatabaseRole databaseRole);
	boolean deleteDatabaseRole(Integer id);
}
