package ua.com.userdb.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.userdb.model.DBUser;
import ua.com.userdb.model.DBUserRole;
import ua.com.userdb.model.DatabaseRole;

public interface DBUserRoleRepository extends JpaRepository<DBUserRole, Integer> {
	List<DBUserRole> findDBUserRoleByDatabaseRole(DatabaseRole databaseRole);
	List<DBUserRole> findDBUserRoleBydbUser(DBUser dbUser);
}
