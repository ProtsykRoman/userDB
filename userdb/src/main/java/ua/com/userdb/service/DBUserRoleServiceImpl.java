package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.userdb.dao.DBUserRoleRepository;
import ua.com.userdb.model.DBUser;
import ua.com.userdb.model.DBUserRole;
import ua.com.userdb.model.DatabaseRole;

@Service
public class DBUserRoleServiceImpl implements DBUserRoleService {
	private DBUserRoleRepository dbUserRoleRepository;
	
	public DBUserRoleServiceImpl(DBUserRoleRepository dbUserRoleRepository) {
		this.dbUserRoleRepository = dbUserRoleRepository;
	}
	
	@Override
	public List<DBUserRole> findAllDbUserRoles() {
		return dbUserRoleRepository.findAll();
	}

	@Override
	public Optional<DBUserRole> findDBUserRoleById(Integer id) {
		return dbUserRoleRepository.findById(id);
	}

	@Override
	public List<DBUserRole> findDBUserRoleByDatabeseRole(DatabaseRole databaseRole) {
		return dbUserRoleRepository.findDBUserRoleByDatabaseRole(databaseRole);
	}

	@Override
	public List<DBUserRole> findDBUserRoleByDBUser(DBUser dbUser) {
		return dbUserRoleRepository.findDBUserRoleBydbUser(dbUser);
	}

	@Override
	public DBUserRole createDBDBUserRole(DBUserRole dbUserRole) {
		return dbUserRoleRepository.save(dbUserRole);
	}

	@Override
	public Optional<DBUserRole> updateDBUserRole(Integer id, DBUserRole dbUserRole) {
		Optional<DBUserRole> exitingDBUserRole = dbUserRoleRepository.findById(id);
		if(exitingDBUserRole.isPresent()) {
			DBUserRole updated = exitingDBUserRole.get();
			updated.setDatabaseRole(dbUserRole.getDatabaseRole());
			updated.setDbUser(dbUserRole.getDbUser());
			dbUserRoleRepository.save(updated);
			return Optional.of(updated);
		}else {
			return Optional.empty();
		}
	}

	@Override
	public boolean deleteDBUserRole(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

}
