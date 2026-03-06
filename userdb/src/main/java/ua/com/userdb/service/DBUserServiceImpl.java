package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.userdb.dao.CertificateTypeRepository;
import ua.com.userdb.dao.DBUserRepository;
import ua.com.userdb.dao.DatabaseRepository;
import ua.com.userdb.model.DBUser;
import ua.com.userdb.model.DBUserAccess;
import ua.com.userdb.model.DBUserCertificate;
import ua.com.userdb.model.DBUserRole;

@Service
public class DBUserServiceImpl implements DBUserService {

	private final DBUserRepository dbUserRepository;
	private final CertificateTypeRepository certificateTypeRepository;
	private final DatabaseRepository databaseRepository;

	public DBUserServiceImpl(DBUserRepository dbUserRepository, CertificateTypeRepository certificateTypeRepository,
			DatabaseRepository databaseRepository) {
		this.dbUserRepository = dbUserRepository;
		this.certificateTypeRepository = certificateTypeRepository;
		this.databaseRepository = databaseRepository;
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
	@Transactional
	public DBUser createDBUser(DBUser dbUser) {
		setBackReferences(dbUser);
		return dbUserRepository.save(dbUser);
	}

	@Override
	@Transactional
	public DBUser updateDBUser(Integer id, DBUser dbUser) {
		Optional<DBUser> existingOpt = dbUserRepository.findById(id);
		if (existingOpt.isPresent()) {
			DBUser existing = existingOpt.get();
			existing.setDepartment(dbUser.getDepartment());
			existing.setIdentificationNumber(dbUser.getIdentificationNumber());
			existing.setName(dbUser.getName());
			existing.setRank(dbUser.getRank());
			existing.setIsActive(dbUser.getIsActive());

			// --- оновлення доступів ---
			existing.getDbUserAccesses().clear();

			if (dbUser.getDbUserAccesses() != null) {
			    for (DBUserAccess access : dbUser.getDbUserAccesses()) {

			        if (access.getDatabase() == null ||
			            access.getDatabase().getId() == null) {
			            continue;
			        }

			        access.setDbUser(existing);

			        var database = databaseRepository
			                .findById(access.getDatabase().getId())
			                .orElseThrow();

			        access.setDatabase(database);

			        existing.getDbUserAccesses().add(access);
			    }
			}

			// --- оновлення сертифікатів ---
			existing.getDbUserCertificates().clear();
			if (dbUser.getDbUserCertificates() != null) {
			    for (DBUserCertificate cert : dbUser.getDbUserCertificates()) {

			    	if (cert.getCertificateType() == null ||
			                cert.getCertificateType().getId() == null) {
			                continue;
			            }
			    	
			        cert.setDbUser(existing);

			        if (cert.getCertificateType() != null &&
			            cert.getCertificateType().getId() != null) {

			            var type = certificateTypeRepository
			                    .findById(cert.getCertificateType().getId())
			                    .orElseThrow();

			            cert.setCertificateType(type);
			        }

			        existing.getDbUserCertificates().add(cert);
			    }
			}

			// --- оновлення ролей ---
			existing.getDbUserRoles().clear();
			if (dbUser.getDbUserRoles() != null) {
				for (DBUserRole role : dbUser.getDbUserRoles()) {
					role.setDbUser(existing);
					existing.getDbUserRoles().add(role);
				}
			}

			return dbUserRepository.save(existing);
		} else {
			return null;
		}
	}

	@Override
	public boolean deleteDBUser(Integer id) {
		if (dbUserRepository.existsById(id)) {
			dbUserRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean existsByIdentificationNumber(Long identificationNumber) {
	    return dbUserRepository.existsByIdentificationNumber(identificationNumber);
	}

	/** Встановлює зворотні посилання на DBUser у дочірніх колекціях */
	private void setBackReferences(DBUser dbUser) {
		if (dbUser.getDbUserAccesses() != null) {
			for (DBUserAccess access : dbUser.getDbUserAccesses()) {
				access.setDbUser(dbUser);
			}
		}
		if (dbUser.getDbUserCertificates() != null) {
			for (DBUserCertificate cert : dbUser.getDbUserCertificates()) {
				cert.setDbUser(dbUser);
			}
		}
		if (dbUser.getDbUserRoles() != null) {
			for (DBUserRole role : dbUser.getDbUserRoles()) {
				role.setDbUser(dbUser);
			}
		}
	}
}
