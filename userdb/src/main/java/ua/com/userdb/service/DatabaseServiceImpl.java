package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.userdb.dao.DatabaseRepository;
import ua.com.userdb.model.Database;

@Service
public class DatabaseServiceImpl implements DatabaseService{

	private DatabaseRepository databaseRepository;
	
	public DatabaseServiceImpl(DatabaseRepository databaseRepository) {
		this.databaseRepository = databaseRepository;
	}
	
	@Override
	public List<Database> findAllDatabase() {
		return databaseRepository.findAll();
	}

	@Override
	public Optional<Database> findDatabaseById(Integer id) {
		return databaseRepository.findById(id);
	}

	@Override
	public Optional<Database> findDatabaseByName(String name) {
		return databaseRepository.findByName(name);
	}

	@Override
	public Database createDatabase(Database database) {
		return databaseRepository.save(database);
	}

	@Override
	public Optional<Database> updateDatabase(Integer id, Database database) {
		Optional<Database> exitingDatabase = databaseRepository.findById(id);
		
		if(exitingDatabase.isPresent()) {
			Database updated = exitingDatabase.get();
			updated.setName(database.getName());
			updated.setIsActive(database.getIsActive());
			databaseRepository.save(updated);
			return Optional.of(updated);
		}else {
			return Optional.empty();
		}
	}

	@Override
	public boolean deleteDatabase(Integer id) {
		if (databaseRepository.existsById(id)) {
			databaseRepository.deleteById(id);
			return true;
		}else {
			return false;
		}
	}

}
