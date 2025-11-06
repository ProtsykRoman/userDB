package ua.com.userdb.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.userdb.dao.DBUserAccessRepository;
import ua.com.userdb.model.DBUser;
import ua.com.userdb.model.DBUserAccess;
import ua.com.userdb.model.Database;

@Service
public class DBUserAccessServiceImpl implements DBUserAccessService{
	
	private DBUserAccessRepository dbUserAccessRepository;
	
	public DBUserAccessServiceImpl(DBUserAccessRepository dbUserAccessRepository) {
		this.dbUserAccessRepository = dbUserAccessRepository;
	}

	@Override
	public List<DBUserAccess> findAllDBUserAccess() {
		return dbUserAccessRepository.findAll();
	}

	@Override
	public Optional<DBUserAccess> findDBUserAccessById(Integer id) {
		return dbUserAccessRepository.findById(id);
	}

	@Override
	public List<DBUserAccess> findDBUserAccessByAccessExpirationDate(Date date) {
		return dbUserAccessRepository.findAllByAccessExpirationDate(date);
	}

	@Override
	public List<DBUserAccess> findDBUserAccessByDatabase(Database database) {
		return dbUserAccessRepository.findAllByDatabase(database);
	}

	@Override
	public List<DBUserAccess> findDBUserAccessByDBUser(DBUser dbUser) {
		return dbUserAccessRepository.findAllBydbUser(dbUser);
	}

	@Override
	public DBUserAccess createDBUserAccess(DBUserAccess dbUserAccess) {
		return dbUserAccessRepository.save(dbUserAccess);
	}

	@Override
	public Optional<DBUserAccess> updateDBUserAccess(Integer id, DBUserAccess dbUserAccess) {
		Optional<DBUserAccess> exitingDBUserAccess = dbUserAccessRepository.findById(id);
		
		if(exitingDBUserAccess.isPresent()) {
			DBUserAccess updated = exitingDBUserAccess.get();
			updated.setAccessExpirationDate(dbUserAccess.getAccessExpirationDate());
			updated.setDatabase(dbUserAccess.getDatabase());
			updated.setDbUser(dbUserAccess.getDbUser());
			dbUserAccessRepository.save(updated);
			return Optional.of(updated);
		}else {
			return Optional.empty();
		}
	}

	@Override
	public boolean deleteDBUserAccess(Integer id) {
		if(dbUserAccessRepository.existsById(id)) {
			dbUserAccessRepository.deleteById(id);
			return true;
		}else {
			return false;
		}
	}

	

}
