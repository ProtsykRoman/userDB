package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.userdb.dao.CertificateTypeRepository;
import ua.com.userdb.model.CertificateType;

@Service
public class CertificateTypeServiceImpl implements CertificateTypeService{

	private final CertificateTypeRepository certificateTypeRepository;
	
	public CertificateTypeServiceImpl(CertificateTypeRepository certificateTypeRepository) {
		this.certificateTypeRepository = certificateTypeRepository;
	}
	
	@Override
	public List<CertificateType> findAllCertificateType() {
		return certificateTypeRepository.findAll();
	}

	@Override
	public Optional<CertificateType> findCertificateTypeById(Integer id) {
		return certificateTypeRepository.findById(id);
	}

	@Override
	public Optional<CertificateType> findCertificateTypeByName(String name) {
		return certificateTypeRepository.findByName(name);
	}

	@Override
	public CertificateType createCertificateType(CertificateType certificateType) {
		return certificateTypeRepository.save(certificateType);
	}

	@Override
	public Optional<CertificateType> updateCertificateType(Integer id, CertificateType certificateType) {
		Optional<CertificateType> existingCertificateType = certificateTypeRepository.findById(id);

	    if (existingCertificateType.isPresent()) {
	        CertificateType updated = existingCertificateType.get();
	        updated.setName(certificateType.getName());
	        updated.setIsActive(certificateType.getIsActive());

	        certificateTypeRepository.save(updated);
	        return Optional.of(updated);
	    } else {
	        return Optional.empty();
	    }
	}

	@Override
	public boolean deleteCertificateType(Integer id) {
		if (certificateTypeRepository.existsById(id)) {
			certificateTypeRepository.deleteById(id);
            return true;
        }
        return false;
	}

}
