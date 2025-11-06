package ua.com.userdb.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.userdb.dao.DBUserCertificateRepository;
import ua.com.userdb.model.CertificateType;
import ua.com.userdb.model.DBUser;
import ua.com.userdb.model.DBUserCertificate;

@Service
public class DBUserCertificateServiceImpl implements DBUserCertificateService{
	private DBUserCertificateRepository dbUserCertificateRepository;
	
	public DBUserCertificateServiceImpl(DBUserCertificateRepository dbUserCertificateRepository) {
		this.dbUserCertificateRepository = dbUserCertificateRepository;
	}
	
	@Override
	public List<DBUserCertificate> findAllDBUserCertificate() {
		return dbUserCertificateRepository.findAll();
	}

	@Override
	public Optional<DBUserCertificate> findDBUserCertificateById(Integer id) {
		return dbUserCertificateRepository.findById(id);
	}

	@Override
	public List<DBUserCertificate> findDBUserCertificateByDBUser(DBUser dbUser) {
		return dbUserCertificateRepository.findDBUserCertificateBydbUser(dbUser);
	}

	@Override
	public List<DBUserCertificate> findDBUserCertificateByCertificateType(CertificateType certificateType) {
		return dbUserCertificateRepository.findDBUserCertificateByCertificateType(certificateType);
	}

	@Override
	public List<DBUserCertificate> findDBUserCertificatesExpiringBefore(Date date) {
		return dbUserCertificateRepository.findByExpirationDateBefore(date);
	}

	@Override
	public Optional<DBUserCertificate> findDBUserCertificateByNumber(String number) {
		return dbUserCertificateRepository.findDBUserCertificateByNumber(number);
	}

	@Override
	public DBUserCertificate createDBUserCertificate(DBUserCertificate dbUserCertificate) {
		return dbUserCertificateRepository.save(dbUserCertificate);
	}

	@Override
	public Optional<DBUserCertificate> updateDBUserCertificate(Integer id, DBUserCertificate dbUserCertificate) {
		Optional<DBUserCertificate> exitingDBUserCertificate = dbUserCertificateRepository.findById(id);
		if(exitingDBUserCertificate.isPresent()) {
			DBUserCertificate updated = exitingDBUserCertificate.get();
			updated.setCertificateType(dbUserCertificate.getCertificateType());
			updated.setDbUser(dbUserCertificate.getDbUser());
			updated.setExpirationDate(dbUserCertificate.getExpirationDate());
			updated.setNumber(dbUserCertificate.getNumber());
			updated.setIsBlocked(dbUserCertificate.getIsBlocked());
			dbUserCertificateRepository.save(updated);
			return Optional.of(updated);
		}else {
			return Optional.empty();
		}
	}

	@Override
	public boolean deleteDBUserCertificate(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

}
