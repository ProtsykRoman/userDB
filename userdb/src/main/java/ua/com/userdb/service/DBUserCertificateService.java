package ua.com.userdb.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import ua.com.userdb.model.CertificateType;
import ua.com.userdb.model.DBUser;
import ua.com.userdb.model.DBUserCertificate;

public interface DBUserCertificateService {
	List<DBUserCertificate> findAllDBUserCertificate();
	Optional<DBUserCertificate> findDBUserCertificateById(Integer id);
	List<DBUserCertificate> findDBUserCertificateByDBUser(DBUser dbUser);
	List<DBUserCertificate> findDBUserCertificateByCertificateType(CertificateType certificateType);
	List<DBUserCertificate> findDBUserCertificatesExpiringBefore(Date date);
	Optional<DBUserCertificate> findDBUserCertificateByNumber(String number);
	DBUserCertificate createDBUserCertificate(DBUserCertificate dbUserCertificate);
	Optional<DBUserCertificate> updateDBUserCertificate(Integer id, DBUserCertificate dbUserCertificate);
	boolean deleteDBUserCertificate(Integer id);
}
