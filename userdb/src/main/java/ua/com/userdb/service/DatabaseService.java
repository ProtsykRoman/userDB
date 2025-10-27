package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import ua.com.userdb.model.Database;

public interface DatabaseService {
	List<Database> findAllDatabase();
	Optional<Database> findDatabaseById(Integer id);
	Optional<Database> findDatabaseByName(String name);
	Database createDatabase(Database database);
	Optional<Database> updateDatabase(Integer id, Database database);
	boolean deleteDatabase(Integer id);
}
