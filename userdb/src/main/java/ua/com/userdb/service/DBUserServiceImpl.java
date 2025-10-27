package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import ua.com.userdb.dao.DBUserRepository;
import ua.com.userdb.model.DBUser;

public class DBUserServiceImpl implements DBUserService {

	private DBUserRepository dbUserRepository;
	
	public DBUserServiceImpl(DBUserRepository dbUserRepository) {
		this.dbUserRepository = dbUserRepository;
	}
	
	@Override
	public List<DBUser> findAll() {
		return dbUserRepository.findAll();
	}

	@Override
	public Optional<DBUser> findById(Integer id) {
		return dbUserRepository.findById(id);
	}

	@Override
	public Optional<DBUser> findByName(String name) {
		return dbUserRepository.findByName(name);
	}

	@Override
	public DBUser createDBUser(DBUser dbUser) {
		return dbUserRepository.save(dbUser);
	}

	@Override
	public Optional<DBUser> updateDBUser(Integer id, DBUser dbUser) {
		Optional<DBUser> exitingDBUser = dbUserRepository.findById(id);
		if(exitingDBUser.isPresent()) {
			DBUser updated = exitingDBUser.get();
			updated.setDepartment(dbUser.getDepartment());
			updated.setIdentificationNumber(dbUser.getIdentificationNumber());
			updated.setName(dbUser.getName());
			updated.setRank(dbUser.getRank());
			dbUserRepository.save(updated);
			return Optional.of(updated);
		}else {
			return Optional.empty();
		}
	}

	@Override
	public boolean deleteDBUser(Integer id) {
		if(dbUserRepository.existsById(id)) {
			dbUserRepository.deleteById(id);
			return true;
		}else {
			return false;
		}
	}

}
