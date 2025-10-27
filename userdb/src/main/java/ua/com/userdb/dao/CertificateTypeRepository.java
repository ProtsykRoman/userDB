package ua.com.userdb.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.userdb.model.CertificateType;

public interface CertificateTypeRepository extends JpaRepository<CertificateType, Integer> {
	Optional<CertificateType> findByName(String name);
}
