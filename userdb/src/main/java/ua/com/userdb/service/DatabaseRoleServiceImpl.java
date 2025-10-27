package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import ua.com.userdb.dao.DatabaseRoleRepository;
import ua.com.userdb.model.Database;
import ua.com.userdb.model.DatabaseRole;

public class DatabaseRoleServiceImpl implements DatabaseRoleService {
	private DatabaseRoleRepository databaseRoleRepository;
	
	public DatabaseRoleServiceImpl(DatabaseRoleRepository databaseRoleRepository) {
		this.databaseRoleRepository = databaseRoleRepository;
	}
	
	@Override
	public List<DatabaseRole> findAllDatabaseRole() {
		return databaseRoleRepository.findAll();
	}

	@Override
	public Optional<DatabaseRole> findDatabaseRoleById(Integer Id) {
		return databaseRoleRepository.findById(Id);
	}

	@Override
	public Optional<DatabaseRole> findDatabaseRoleByName(String name) {
		return databaseRoleRepository.findByName(name);
	}

	@Override
	public List<DatabaseRole> findDatabaseRolesByDatabase(Database database) {
		return databaseRoleRepository.findByDatabase(database);
	}

	@Override
	public DatabaseRole createDatabaseRole(DatabaseRole databaseRole) {
		return databaseRoleRepository.save(databaseRole);
	}

	@Override
	public Optional<DatabaseRole> updateDatabaseRole(Integer id, DatabaseRole databaseRole) {
		Optional<DatabaseRole> exitingDatabaseRole = databaseRoleRepository.findById(id);
		
		if(exitingDatabaseRole.isPresent()) {
			DatabaseRole updated = exitingDatabaseRole.get();
			updated.setName(databaseRole.getName());
			updated.setDatabase(databaseRole.getDatabase());
			databaseRoleRepository.save(updated);
			
			return Optional.of(updated);
		}else {
			return Optional.empty();
		}
	}

	@Override
	public boolean deleteDatabaseRole(Integer id) {
		if(databaseRoleRepository.existsById(id)) {
			databaseRoleRepository.deleteById(id);
			return true;
		}else {
			return false;
		}
	}

}
