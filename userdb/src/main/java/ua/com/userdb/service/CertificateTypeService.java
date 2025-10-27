package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import ua.com.userdb.model.CertificateType;

public interface CertificateTypeService {
	List<CertificateType> findAllCertificateType();
	Optional<CertificateType> findCertificateTypeById(Integer id);
	Optional<CertificateType> findCertificateTypeByName(String name);
	CertificateType createCertificateType(CertificateType certificateType);
	Optional<CertificateType> updateCertificateType(Integer id, CertificateType certificateType);
	boolean deleteCertificateType(Integer id);
}
