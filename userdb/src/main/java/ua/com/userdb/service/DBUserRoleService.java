package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import ua.com.userdb.model.DBUser;
import ua.com.userdb.model.DBUserRole;
import ua.com.userdb.model.DatabaseRole;

public interface DBUserRoleService {
	List<DBUserRole> findAllDbUserRoles();
	Optional<DBUserRole> findDBUserRoleById(Integer id);
	List<DBUserRole> findDBUserRoleByDatabeseRole(DatabaseRole databaseRole);
	List<DBUserRole> findDBUserRoleByDBUser(DBUser dbUser);
	DBUserRole createDBDBUserRole(DBUserRole dbUserRole);
	Optional<DBUserRole> updateDBUserRole(Integer id, DBUserRole dbUserRole);
	boolean deleteDBUserRole(Integer id);
}
