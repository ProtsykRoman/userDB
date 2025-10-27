package ua.com.userdb.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import ua.com.userdb.model.DBUser;
import ua.com.userdb.model.DBUserAccess;
import ua.com.userdb.model.Database;

public interface DBUserAccessService {
	List<DBUserAccess> findAllDBUserAccess();
	Optional<DBUserAccess> findDBUserAccessById(Integer id);
	List<DBUserAccess> findDBUserAccessByAccessExpirationDate(Date date);
	List<DBUserAccess> findDBUserAccessByDatabase(Database database);
	List<DBUserAccess> findDBUserAccessByDBUser(DBUser dbUser);
	DBUserAccess createDBUserAccess(DBUserAccess dbUserAccess);
	Optional<DBUserAccess> updateDBUserAccess(Integer id, DBUserAccess dbUserAccess);
	boolean deleteDBUserAccess(Integer id);
}
