package ua.com.userdb.dao;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.userdb.model.CertificateType;
import ua.com.userdb.model.DBUser;
import ua.com.userdb.model.DBUserCertificate;

public interface DBUserCertificateRepository extends JpaRepository<DBUserCertificate, Integer>{
	List<DBUserCertificate> findDBUserCertificateByDBUser(DBUser dbUser);
	List<DBUserCertificate> findDBUserCertificateByCertificateType(CertificateType certificateType);
	List<DBUserCertificate> findDBUserCertificatesExpiringBefore(Date date);
	Optional<DBUserCertificate> findDBUserCertificateByNumber(String number);
}
