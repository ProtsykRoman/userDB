package ua.com.userdb.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.userdb.model.DBUser;
import ua.com.userdb.model.DBUserAccess;
import ua.com.userdb.model.Database;

public interface DBUserAccessRepository extends JpaRepository<DBUserAccess, Integer>{
	List<DBUserAccess> findAllByExpirationDate(Date date);
	List<DBUserAccess> findAllByDatabase(Database database);
	List<DBUserAccess> findAllByDBUser(DBUser dbUser);
}
